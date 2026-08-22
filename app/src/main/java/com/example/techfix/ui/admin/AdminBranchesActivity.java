package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminBranchesBinding;
import com.example.techfix.model.BranchItem;
import com.example.techfix.ui.common.BranchAdapter;

public class AdminBranchesActivity extends AppCompatActivity {

    private FragmentAdminBranchesBinding binding;
    private AdminViewModel viewModel;
    private BranchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminBranchesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new BranchAdapter(new BranchAdapter.OnBranchActionListener() {
            @Override
            public void onBranchToggle(BranchItem branch, boolean isActive) {
                viewModel.toggleBranchActive(branch.getId(), isActive);
            }

            @Override
            public void onEditBranch(BranchItem branch) {
                Toast.makeText(AdminBranchesActivity.this, "Editing Branch: " + branch.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onViewTechnicians(BranchItem branch) {
                Toast.makeText(AdminBranchesActivity.this, "Viewing Techs for " + branch.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvBranchesList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBranchesList.setAdapter(adapter);

        viewModel.getBranches().observe(this, branchItems -> {
            if (branchItems != null) {
                adapter.setBranchItems(branchItems);
            }
        });

        binding.btnAddBranch.setOnClickListener(v -> {
            Toast.makeText(this, "Add Branch Dialog Opened", Toast.LENGTH_SHORT).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
