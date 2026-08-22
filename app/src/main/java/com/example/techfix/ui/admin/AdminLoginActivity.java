package com.example.techfix.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.FragmentAdminLoginBinding;
import com.example.techfix.model.User;
import com.example.techfix.ui.main.RoleSelectActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private FragmentAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLoginAdmin.setOnClickListener(v -> {
            String email = binding.etAdminEmail.getText() != null ? binding.etAdminEmail.getText().toString().trim() : "";
            String password = binding.etAdminPassword.getText() != null ? binding.etAdminPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email)) {
                binding.etAdminEmail.setError("Email is required");
                return;
            }
            binding.etAdminEmail.setError(null);

            if (TextUtils.isEmpty(password)) {
                binding.etAdminPassword.setError("Password is required");
                return;
            }
            binding.etAdminPassword.setError(null);

            binding.btnLoginAdmin.setEnabled(false);
            binding.btnLoginAdmin.setText("Authenticating Admin...");

            FirebaseAuthRepository.getInstance().login(email, password, FirestoreConstants.ROLE_ADMIN, new FirebaseAuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user, String role) {
                    binding.btnLoginAdmin.setEnabled(true);
                    binding.btnLoginAdmin.setText("LOG IN TO DASHBOARD");
                    Toast.makeText(AdminLoginActivity.this, "Welcome " + user.getName() + " (Admin)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    binding.btnLoginAdmin.setEnabled(true);
                    binding.btnLoginAdmin.setText("LOG IN TO DASHBOARD");
                    Toast.makeText(AdminLoginActivity.this, "Admin Auth Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.tvSwitchRoleAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoleSelectActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
