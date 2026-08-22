package com.example.techfix.ui.tech;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentTechRepairsBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.RepairAdapter;

public class TechRepairsActivity extends AppCompatActivity {

    private FragmentTechRepairsBinding binding;
    private TechViewModel viewModel;
    private RepairAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechRepairsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TechViewModel.class);

        adapter = new RepairAdapter(new RepairAdapter.OnRepairClickListener() {
            @Override
            public void onRepairClick(RepairItem repairItem) {
                Intent intent = new Intent(TechRepairsActivity.this, TechRepairDetailActivity.class);
                intent.putExtra("repairId", repairItem.getId());
                startActivity(intent);
            }
        });

        binding.rvAssignedRepairs.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAssignedRepairs.setAdapter(adapter);

        viewModel.getAssignedRepairs().observe(this, repairItems -> {
            if (repairItems != null) {
                adapter.setRepairItems(repairItems);
            }
        });
    }
}
