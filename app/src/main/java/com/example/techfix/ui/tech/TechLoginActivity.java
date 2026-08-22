package com.example.techfix.ui.tech;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.FragmentTechLoginBinding;
import com.example.techfix.model.User;
import com.example.techfix.ui.main.RoleSelectActivity;

public class TechLoginActivity extends AppCompatActivity {

    private FragmentTechLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email / Staff ID is required");
                return;
            }
            binding.etEmail.setError(null);

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required");
                return;
            }
            binding.etPassword.setError(null);

            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setText("Authenticating...");

            FirebaseAuthRepository.getInstance().login(email, password, FirestoreConstants.ROLE_TECHNICIAN, new FirebaseAuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user, String role) {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("LOG IN");
                    Toast.makeText(TechLoginActivity.this, "Welcome " + user.getName() + " (Technician)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TechLoginActivity.this, TechRepairsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("LOG IN");
                    Toast.makeText(TechLoginActivity.this, "Authentication Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Password reset instructions sent to " + binding.etEmail.getText().toString(), Toast.LENGTH_LONG).show();
        });

        binding.tvSwitchRole.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoleSelectActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
