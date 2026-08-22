package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminTechniciansBinding;
import com.example.techfix.model.TechnicianItem;
import com.example.techfix.ui.common.TechnicianAdapter;

public class AdminTechniciansActivity extends AppCompatActivity {

    private FragmentAdminTechniciansBinding binding;
    private AdminViewModel viewModel;
    private TechnicianAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminTechniciansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new TechnicianAdapter(new TechnicianAdapter.OnTechnicianClickListener() {
            @Override
            public void onTechnicianClick(TechnicianItem item) {
                Toast.makeText(AdminTechniciansActivity.this, "Selected Tech: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvTechniciansList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTechniciansList.setAdapter(adapter);

        viewModel.getTechnicians().observe(this, technicianItems -> {
            if (technicianItems != null) {
                adapter.setTechnicians(technicianItems);
            }
        });

        binding.btnAddTechnician.setOnClickListener(v -> {
            Toast.makeText(this, "Add Technician Form Opened", Toast.LENGTH_SHORT).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
