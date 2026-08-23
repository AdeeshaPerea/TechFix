package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techfix.R;
import com.example.techfix.MainActivity;
import com.example.techfix.models.User;
import com.example.techfix.utils.SessionManager;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize helpers
        databaseHelper = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();

        // Initialize UI components
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

        // Click listeners
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
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        if (btnCreateAccount != null) {
            btnCreateAccount.setOnClickListener(v -> handleSignUp());
        }
    }

    private void handleSignUp() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Mobile number is required");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the Terms & Privacy Policy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email is already registered locally
        if (databaseHelper.isEmailExists(email)) {
            etEmail.setError("Email is already registered");
            etEmail.requestFocus();
            Toast.makeText(this, "An account with this email already exists!", Toast.LENGTH_LONG).show();
            return;
        }

        // Create User object
        User newUser = new User(fullName, email, phone, password);

        // 1. Save to local SQLite database
        boolean isInserted = databaseHelper.registerUser(newUser);

        if (isInserted) {
            SessionManager.saveUserSession(this, newUser);

            // 2. Sync to Firebase Cloud Firestore asynchronously
            firebaseHelper.saveUserToCloud(newUser, new FirebaseHelper.CloudSyncCallback() {
                @Override
                public void onSuccess(String message) {
                    // Cloud synced silently or logged
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Offline fallback: data is already safely stored in local SQLite
                }
            });

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            // Redirect to Login (MainActivity) or CustomerHomeActivity
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.putExtra("PREFILL_EMAIL", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
