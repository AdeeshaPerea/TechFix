package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private CheckBox cbTerms;
    private Button btnCreateAccount;
    private ImageButton btnBack, btnNotification;
    private TextView tvLogin;

    private DatabaseHelper databaseHelper;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        databaseHelper = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);
        tvLogin = findViewById(R.id.tvLogin);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(SignUpActivity.this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        if (tvLogin != null) {
            tvLogin.setOnClickListener(v -> {
                Intent intent = new Intent(SignUpActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (btnCreateAccount != null) {
            btnCreateAccount.setOnClickListener(v -> handleSignUp());
        }
    }

    private void handleSignUp() {
        String fullName = etFullName != null && etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String email = etEmail != null && etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone != null && etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String password = etPassword != null && etPassword.getText() != null ? etPassword.getText().toString() : "";
        String confirmPassword = etConfirmPassword != null && etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

        if (TextUtils.isEmpty(fullName)) {
            if (etFullName != null) etFullName.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            if (etEmail != null) etEmail.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (etEmail != null) etEmail.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            if (etPhone != null) etPhone.setError("Mobile number is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            if (etPassword != null) etPassword.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            if (etConfirmPassword != null) etConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (cbTerms != null && !cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the Terms & Privacy Policy", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.isEmailExists(email)) {
            Toast.makeText(this, "An account with this email already exists!", Toast.LENGTH_LONG).show();
            return;
        }

        CustomerUser newUser = new CustomerUser(fullName, email, phone, password);
        boolean isInserted = databaseHelper.registerUser(newUser);

        if (isInserted) {
            firebaseHelper.saveUserToCloud(newUser, null);
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUpActivity.this, CustomerHomeActivity.class);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_NAME", fullName);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create account.", Toast.LENGTH_SHORT).show();
        }
    }
}
