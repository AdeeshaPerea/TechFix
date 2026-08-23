package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentTechRepairDetailBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.FormatUtils;

public class TechRepairDetailFragment extends Fragment {

    private FragmentTechRepairDetailBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairDetailBinding.inflate(inflater, container, false);
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

        if (binding.btnBackHeader != null) {
            binding.btnBackHeader.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        }

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null) {
                bindRepairDetails(item);
            }
        });

        if (binding.btnMarkStage != null) {
            binding.btnMarkStage.setOnClickListener(v -> {
                viewModel.updateStatus(repairId, "COMPLETED");
                Toast.makeText(requireContext(), "Repair marked as COMPLETED!", Toast.LENGTH_SHORT).show();
            });
        }

        if (binding.btnUpdatePriority != null) {
            binding.btnUpdatePriority.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Priority set to URGENT", Toast.LENGTH_SHORT).show();
            });
        }

        if (binding.cardActionDiagnosis != null) {
            binding.cardActionDiagnosis.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("repairId", repairId);
                Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techDiagnosis, bundle);
            });
        }

        if (binding.cardActionStatus != null) {
            binding.cardActionStatus.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("repairId", repairId);
                Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techRepairStatus, bundle);
            });
        }

        if (binding.cardActionSpareParts != null) {
            binding.cardActionSpareParts.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("repairId", repairId);
                Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techSpareParts, bundle);
            });
        }

        if (binding.cardActionGallery != null) {
            binding.cardActionGallery.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("repairId", repairId);
                Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techGallery, bundle);
            });
        }
    }

    private void bindRepairDetails(RepairItem item) {
        if (binding.tvDetailRepairCode != null) binding.tvDetailRepairCode.setText("#" + (item.getRepairCode() != null ? item.getRepairCode() : "RF-1024"));
        if (binding.tvDetailDevice != null) binding.tvDetailDevice.setText((item.getDeviceName() != null ? item.getDeviceName() : "Device") + " • " + (item.getServiceRequested() != null ? item.getServiceRequested() : "Repair"));
        
        if (binding.tvCustomerName != null) binding.tvCustomerName.setText("👤 Customer: " + (item.getCustomerName() != null ? item.getCustomerName() : "Customer") + " (" + (item.getCustomerPhone() != null ? item.getCustomerPhone() : "0771234567") + ")");
        if (binding.tvServiceRequested != null) binding.tvServiceRequested.setText("📱 Device: " + (item.getDeviceName() != null ? item.getDeviceName() : "iPhone 14 Pro") + " • " + (item.getServiceRequested() != null ? item.getServiceRequested() : "Screen Repair"));
        if (binding.tvProblemDescription != null) binding.tvProblemDescription.setText("Issue: " + (item.getProblemDescription() != null ? item.getProblemDescription() : "Display issue reported"));
        if (binding.tvTotalCost != null) binding.tvTotalCost.setText("Estimated Cost: " + FormatUtils.formatCurrency(item.getEstimatedCost()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
