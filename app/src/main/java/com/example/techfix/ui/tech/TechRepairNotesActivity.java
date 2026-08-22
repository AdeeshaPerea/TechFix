package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.databinding.FragmentTechRepairNotesBinding;

public class TechRepairNotesActivity extends AppCompatActivity {

    private FragmentTechRepairNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechRepairNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddNoteConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Repair notes and parts saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
