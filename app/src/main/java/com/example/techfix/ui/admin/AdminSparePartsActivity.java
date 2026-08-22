package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminSparePartsBinding;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.ui.common.SparePartAdapter;

public class AdminSparePartsActivity extends AppCompatActivity {

    private FragmentAdminSparePartsBinding binding;
    private AdminViewModel viewModel;
    private SparePartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminSparePartsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new SparePartAdapter(new SparePartAdapter.OnSparePartClickListener() {
            @Override
            public void onSparePartClick(SparePartItem item) {
                Toast.makeText(AdminSparePartsActivity.this, "Editing Part: " + item.getPartName(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvSparePartsList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSparePartsList.setAdapter(adapter);

        viewModel.getSpareParts().observe(this, sparePartItems -> {
            if (sparePartItems != null) {
                adapter.setSpareParts(sparePartItems);
            }
        });

        binding.btnAddSparePart.setOnClickListener(v -> {
            Toast.makeText(this, "Add Spare Part Dialog Opened", Toast.LENGTH_SHORT).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
