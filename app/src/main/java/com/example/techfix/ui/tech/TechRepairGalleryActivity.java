package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.databinding.FragmentTechRepairGalleryBinding;

public class TechRepairGalleryActivity extends AppCompatActivity {

    private FragmentTechRepairGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechRepairGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMarkCompleted.setOnClickListener(v -> {
            Toast.makeText(this, "Repair marked as Completed!", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
