package com.example.techfix.ui.admin;

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
import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.FragmentAdminLoginBinding;
import com.example.techfix.model.User;

public class AdminLoginFragment extends Fragment {

    private FragmentAdminLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Admin Email is required");
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

            FirebaseAuthRepository.getInstance().login(email, password, FirestoreConstants.ROLE_ADMIN, new FirebaseAuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user, String role) {
                    if (binding == null) return;
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("LOG IN");
                    Toast.makeText(requireContext(), "Welcome " + user.getName() + " (Admin)", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_adminLogin_to_adminDashboard);
                }

                @Override
                public void onFailure(String errorMessage) {
                    if (binding == null) return;
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("LOG IN");
                    Toast.makeText(requireContext(), "Authentication Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Admin password reset link dispatched to " + binding.etEmail.getText().toString(), Toast.LENGTH_LONG).show();
        });

        binding.tvSwitchRole.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.roleSelectFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
