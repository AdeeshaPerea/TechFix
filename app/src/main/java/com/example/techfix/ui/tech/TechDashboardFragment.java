package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentTechDashboardBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.RepairAdapter;

import java.util.List;

public class TechDashboardFragment extends Fragment {

    private FragmentTechDashboardBinding binding;
    private TechViewModel viewModel;
    private RepairAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TechViewModel.class);

        adapter = new RepairAdapter(repairItem -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairItem.getId());
            Navigation.findNavController(requireView()).navigate(R.id.action_techDashboard_to_techRepairDetail, bundle);
        });

        if (binding.rvRecentRepairs != null) {
            binding.rvRecentRepairs.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.rvRecentRepairs.setAdapter(adapter);
        }

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            if (repairItems != null) {
                adapter.setRepairItems(repairItems);
                updateMetrics(repairItems);
            }
        });

        if (binding.tvViewAll != null) {
            binding.tvViewAll.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_techDashboard_to_techRepairs);
            });
        }
    }

    private void updateMetrics(List<RepairItem> items) {
        int today = items.size();
        int inProgress = 0;
        int completed = 0;

        for (RepairItem item : items) {
            String status = item.getStatus() != null ? item.getStatus().toUpperCase() : "";
            if (status.contains("COMPLETED") || status.contains("DONE")) {
                completed++;
            } else if (status.contains("PROGRESS") || status.contains("ACTIVE") || status.contains("ASSIGNED") || status.contains("DIAGNOSING")) {
                inProgress++;
            } else {
                inProgress++;
            }
        }

        if (binding.tvTodayCount != null) binding.tvTodayCount.setText(String.valueOf(today));
        if (binding.tvInProgressCount != null) binding.tvInProgressCount.setText(String.valueOf(inProgress));
        if (binding.tvCompletedCount != null) binding.tvCompletedCount.setText(String.valueOf(completed));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
