package com.example.techfix.ui.tech;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        binding.btnBackHeader.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null) {
                bindRepairDetails(item);
            }
        });

        binding.cardActionDiagnosis.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techDiagnosis, bundle);
        });

        binding.cardActionStatus.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techRepairStatus, bundle);
        });

        binding.cardActionSpareParts.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techSpareParts, bundle);
        });

        binding.cardActionGallery.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairId);
            Navigation.findNavController(v).navigate(R.id.action_techRepairDetail_to_techGallery, bundle);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
