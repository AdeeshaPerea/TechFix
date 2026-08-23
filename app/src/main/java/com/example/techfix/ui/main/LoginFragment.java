package com.example.techfix.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.techfix.customer.SignUpActivity;
import com.example.techfix.databinding.LoginBinding;

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
                Toast.makeText(requireContext(), "Password reset link sent to your email", Toast.LENGTH_SHORT).show();
            });
        }

        if (binding.btnGoogleLogin != null) {
            binding.btnGoogleLogin.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Google sign-in successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), CustomerHomeActivity.class);
                startActivity(intent);
            });
        }
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Password is required");
            return;
        }

        String lowerEmail = email.toLowerCase();
        if (lowerEmail.contains("admin")) {
            Toast.makeText(requireContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_adminDashboard);
        } else if (lowerEmail.contains("tech")) {
            Toast.makeText(requireContext(), "Welcome Technician!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_techDashboard);
        } else {
            Toast.makeText(requireContext(), "Welcome Customer!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), CustomerHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
