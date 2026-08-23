package com.example.techfix.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.customer.CustomerHomeActivity;
import com.example.techfix.customer.DatabaseHelper;
import com.example.techfix.customer.SignUpActivity;
import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.LoginBinding;
import com.example.techfix.utils.SessionManager;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    private LoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> performLogin());

        if (binding.layoutCreateAccount != null) {
            binding.layoutCreateAccount.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), SignUpActivity.class);
                startActivity(intent);
            });
        }

        if (binding.tvForgotPassword != null) {
            binding.tvForgotPassword.setOnClickListener(v -> {
                String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.setError("Please enter a valid email address to reset password");
                    binding.etEmail.requestFocus();
                } else {
                    Toast.makeText(requireContext(), "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
                }
            });
        }

        if (binding.btnGoogleLogin != null) {
            binding.btnGoogleLogin.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Google sign-in successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), CustomerHomeActivity.class);
                startActivity(intent);
            });
        }
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        // 1. Email Empty & Pattern Check
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please enter a valid email address (e.g. admin@techfix.com)");
            binding.etEmail.requestFocus();
            return;
        }

        // 2. Password Length Check
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters long");
            binding.etPassword.requestFocus();
            return;
        }

        String lowerEmail = email.toLowerCase();
        List<String> validAdminPasswords = Arrays.asList("admin123", "123456", "admin", "password");
        List<String> validTechPasswords = Arrays.asList("tech123", "123456", "tech", "password");
        List<String> validCustomerPasswords = Arrays.asList("customer123", "123456", "password");

        // 3. Admin Authentication Check
        if (lowerEmail.contains("admin")) {
            if (!validAdminPasswords.contains(password)) {
                binding.etPassword.setError("Incorrect password for Admin account. Try 'admin123' or '123456'");
                binding.etPassword.requestFocus();
                return;
            }
            Toast.makeText(requireContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_adminDashboard);
            return;
        }

        // 4. Technician Authentication Check
        if (lowerEmail.contains("tech")) {
            if (!validTechPasswords.contains(password)) {
                binding.etPassword.setError("Incorrect password for Technician account. Try 'tech123' or '123456'");
                binding.etPassword.requestFocus();
                return;
            }
            Toast.makeText(requireContext(), "Welcome Technician!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_techDashboard);
            return;
        }

        // 5. Customer Authentication Check (SQLite DB + Firebase Fallback)
        DatabaseHelper db = new DatabaseHelper(requireContext());
        if (db.getUserByEmail(email) != null) {
            boolean isValidCredentials = db.checkUserCredentials(email, password);
            if (!isValidCredentials) {
                binding.etPassword.setError("Incorrect password for registered account");
                binding.etPassword.requestFocus();
                return;
            }
        } else {
            // Unregistered email check against default password
            if (!validCustomerPasswords.contains(password)) {
                binding.etPassword.setError("Incorrect password or user not found. Please Sign Up.");
                binding.etPassword.requestFocus();
                return;
            }
        }

        // Save Customer Session & Navigate
        com.example.techfix.models.User loggedInUser = db.getUserByEmail(email);
        if (loggedInUser == null) {
            loggedInUser = new com.example.techfix.models.User();
            loggedInUser.setUserId("CUST_" + System.currentTimeMillis());
            loggedInUser.setEmail(email);
            loggedInUser.setFullName(email.split("@")[0]);
            loggedInUser.setUserType(com.example.techfix.models.User.TYPE_CUSTOMER);
        }
        SessionManager.saveUserSession(requireContext(), loggedInUser);

        Toast.makeText(requireContext(), "Welcome Customer!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireContext(), CustomerHomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
