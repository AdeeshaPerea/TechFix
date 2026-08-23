package com.example.techfix.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.customer.CustomerHomeActivity;
import com.example.techfix.databinding.FragmentRoleSelectBinding;
import com.example.techfix.ui.admin.AdminLoginActivity;
import com.example.techfix.ui.tech.TechLoginActivity;

public class RoleSelectActivity extends AppCompatActivity {

    private FragmentRoleSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentRoleSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerHomeActivity.class);
            startActivity(intent);
        });

        binding.cardTechnician.setOnClickListener(v -> {
            Intent intent = new Intent(this, TechLoginActivity.class);
            startActivity(intent);
        });

        binding.cardAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }
}
