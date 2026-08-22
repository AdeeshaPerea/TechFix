package com.example.techfix.ui.tech;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.techfix.databinding.FragmentTechRepairDetailBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.FormatUtils;

public class TechRepairDetailActivity extends AppCompatActivity {

    private FragmentTechRepairDetailBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTechRepairDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TechViewModel.class);
        repairId = getIntent() != null ? getIntent().getStringExtra("repairId") : "REP_001";
        if (repairId == null) repairId = "REP_001";

        binding.btnBackHeader.setOnClickListener(v -> finish());

        viewModel.getRepairs().observe(this, repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null) {
                bindRepairDetails(item);
            }
        });

        binding.cardActionDiagnosis.setOnClickListener(v -> {
            Intent intent = new Intent(this, TechRepairNotesActivity.class);
            intent.putExtra("repairId", repairId);
            startActivity(intent);
        });

        binding.cardActionStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, TechRepairNotesActivity.class);
            intent.putExtra("repairId", repairId);
            startActivity(intent);
        });

        binding.cardActionSpareParts.setOnClickListener(v -> {
            Intent intent = new Intent(this, TechRepairNotesActivity.class);
            intent.putExtra("repairId", repairId);
            startActivity(intent);
        });

        binding.cardActionGallery.setOnClickListener(v -> {
            Intent intent = new Intent(this, TechRepairGalleryActivity.class);
            intent.putExtra("repairId", repairId);
            startActivity(intent);
        });
    }

    private void bindRepairDetails(RepairItem item) {
        binding.tvDetailRepairCode.setText("#" + item.getRepairCode());
        binding.tvDetailDevice.setText(item.getDeviceName() + " • " + (item.getDeviceModel() != null ? item.getDeviceModel() : "Mobile"));
        binding.tvDetailStatusBadge.setText(item.getStatus());
        binding.tvDetailStatusBadge.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getStatusBgColor(item.getStatus())));
        binding.tvDetailStatusBadge.setTextColor(FormatUtils.getStatusTextColor(item.getStatus()));

        binding.tvCustomerName.setText("Customer: " + item.getCustomerName() + " (" + item.getCustomerPhone() + ")");
        binding.tvServiceRequested.setText("Service Requested: " + item.getServiceRequested());
        binding.tvProblemDescription.setText("Issue: " + item.getProblemDescription());
        binding.tvTotalCost.setText(FormatUtils.formatCurrency(item.getEstimatedCost()));
    }
}
