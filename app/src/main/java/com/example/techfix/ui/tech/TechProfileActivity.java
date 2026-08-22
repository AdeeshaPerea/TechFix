package com.example.techfix.ui.tech;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.databinding.FragmentTechProfileBinding;
import com.example.techfix.ui.main.RoleSelectActivity;

public class TechProfileActivity extends AppCompatActivity {

    private FragmentTechProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogoutTech.setOnClickListener(v -> {
            FirebaseAuthRepository.getInstance().logout();
            Intent intent = new Intent(this, RoleSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
