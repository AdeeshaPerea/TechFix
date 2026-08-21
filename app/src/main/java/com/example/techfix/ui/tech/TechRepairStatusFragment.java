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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentTechRepairStatusBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.StatusTimelineAdapter;

public class TechRepairStatusFragment extends Fragment {

    private FragmentTechRepairStatusBinding binding;
    private TechViewModel viewModel;
    private StatusTimelineAdapter adapter;
    private String repairId;

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
        String currentStatus = item != null ? item.getStatus() : "DIAGNOSING";

        if (item != null) {
            binding.tvTimelineCode.setText(item.getRepairCode() + " • " + item.getDeviceName());
            binding.tvTimelineCurrentStatus.setText("Current Status: " + currentStatus);
        }

        adapter = new StatusTimelineAdapter(currentStatus, newStatus -> {
            viewModel.updateStatus(repairId, newStatus);
            adapter.setCurrentStatus(newStatus);
            binding.tvTimelineCurrentStatus.setText("Current Status: " + newStatus);
            Toast.makeText(requireContext(), "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
        });

        binding.rvStatusTimeline.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvStatusTimeline.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
