package com.example.techfix.ui.tech;

import android.content.res.ColorStateList;
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

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null) {
                bindRepairDetails(item);
            }
        });

        binding.btnStartDiagnosis.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techDiagnosis, bundle);
        });

        binding.btnUpdateStatus.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techRepairStatus, bundle);
        });

        binding.btnAddNotes.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techRepairNotes, bundle);
        });

        binding.btnManageParts.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techSpareParts, bundle);
        });

        binding.btnBeforeAfterPhotos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techGallery, bundle);
        });

        binding.btnCompleteRepair.setOnClickListener(v -> {
            viewModel.updateStatus(repairId, "COMPLETED");
            Toast.makeText(requireContext(), "Repair marked as COMPLETED!", Toast.LENGTH_SHORT).show();
        });
    }

    private void bindRepairDetails(RepairItem item) {
        binding.tvDetailRepairCode.setText(item.getRepairCode());
        binding.tvDetailPriority.setText(item.getPriority().toUpperCase());
        binding.tvDetailPriority.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getPriorityColor(item.getPriority())));

        binding.tvDetailDevice.setText(item.getDeviceName() + " (" + (item.getDeviceModel() != null ? item.getDeviceModel() : "") + ")");
        binding.tvDetailService.setText("Service: " + item.getServiceRequested());
        binding.tvDetailProblem.setText(item.getProblemDescription());

        binding.tvDetailCustomer.setText(item.getCustomerName() + " (" + item.getCustomerPhone() + ")");
        binding.tvDetailBranch.setText(item.getBranchName());
        binding.tvDetailAppointment.setText(item.getAppointmentDate() + " at " + item.getAppointmentTime());

        binding.tvDetailStatus.setText(item.getStatus());
        binding.tvDetailStatus.setTextColor(FormatUtils.getStatusTextColor(item.getStatus()));

        binding.tvDetailCost.setText(FormatUtils.formatCurrency(item.getEstimatedCost()));
        binding.tvDetailTech.setText(item.getAssignedTechName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
