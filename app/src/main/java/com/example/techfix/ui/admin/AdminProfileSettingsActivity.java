package com.example.techfix.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.databinding.FragmentAdminProfileSettingsBinding;
import com.example.techfix.ui.main.RoleSelectActivity;

public class AdminProfileSettingsActivity extends AppCompatActivity {

    private FragmentAdminProfileSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogoutAdmin.setOnClickListener(v -> {
            FirebaseAuthRepository.getInstance().logout();
            Intent intent = new Intent(this, RoleSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
