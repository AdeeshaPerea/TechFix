package com.example.techfix.ui.tech;

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
import com.example.techfix.databinding.FragmentTechLoginBinding;

public class TechLoginFragment extends Fragment {

    private FragmentTechLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

            Toast.makeText(requireContext(), "Technician Login Successful", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.action_techLogin_to_techDashboard);
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Password reset instructions sent to " + binding.etEmail.getText().toString(), Toast.LENGTH_LONG).show();
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
