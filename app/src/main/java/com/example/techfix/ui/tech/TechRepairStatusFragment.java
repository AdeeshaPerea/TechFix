package com.example.techfix.ui.tech;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techfix.databinding.FragmentTechRepairStatusBinding;
import com.example.techfix.model.RepairItem;

public class TechRepairStatusFragment extends Fragment {

    private FragmentTechRepairStatusBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    private static final String[] STAGES = {
            "RECEIVED",
            "DIAGNOSING",
            "REPAIRING",
            "QUALITY_CHECK",
            "READY_FOR_PICKUP",
            "COMPLETED"
    };

    private static final String[] STAGE_LABELS = {
            "Received",
            "Diagnosing",
            "Repairing",
            "Quality Check",
            "Ready for Pickup",
            "Completed"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TechViewModel.class);

        if (getArguments() != null) {
            repairId = getArguments().getString("repairId", "REP_001");
        } else {
            repairId = "REP_001";
        }

        RepairItem item = viewModel.getRepairById(repairId);
        bindStatusData(item);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        if (binding.btnMarkNextStage != null) {
            binding.btnMarkNextStage.setOnClickListener(v -> showStageSelectionDialog(item));
        }

        if (binding.btnUpdatePriority != null) {
            binding.btnUpdatePriority.setOnClickListener(v -> showStageSelectionDialog(item));
        }
    }

    private void bindStatusData(RepairItem item) {
        if (item == null) return;
        String currentStatus = item.getStatus() != null ? item.getStatus().toUpperCase() : "RECEIVED";

        if (binding.txtRepairId != null) binding.txtRepairId.setText("Repair #" + item.getRepairCode());
        if (binding.txtRepairTitle != null) binding.txtRepairTitle.setText(item.getDeviceName() + " — " + item.getServiceRequested());
        if (binding.txtCustomerInfo != null) binding.txtCustomerInfo.setText(item.getCustomerName() + " · " + item.getCustomerPhone());
        if (binding.txtDeviceInfo != null) binding.txtDeviceInfo.setText(item.getDeviceName() + " — " + item.getDeviceModel());

        int currentStageIndex = getStageIndex(currentStatus);
        int nextStageIndex = Math.min(currentStageIndex + 1, STAGES.length - 1);

        if (binding.btnMarkNextStage != null) {
            binding.btnMarkNextStage.setText("MARK AS: " + STAGE_LABELS[nextStageIndex].toUpperCase());
        }

        int progress = (int) (((currentStageIndex + 1) / (float) STAGES.length) * 100);
        if (binding.progressStatus != null) {
            binding.progressStatus.setProgress(progress);
        }
    }

    private int getStageIndex(String status) {
        for (int i = 0; i < STAGES.length; i++) {
            if (STAGES[i].equalsIgnoreCase(status)) return i;
        }
        return 0;
    }

    private void showStageSelectionDialog(RepairItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Update Repair Status");
        builder.setItems(STAGE_LABELS, (dialog, which) -> {
            String selectedStatus = STAGES[which];
            String label = STAGE_LABELS[which];

            viewModel.updateStatus(repairId, selectedStatus);
            if (item != null) item.setStatus(selectedStatus);
            bindStatusData(item);

            Toast.makeText(requireContext(), "Status updated to: " + label, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
