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
import com.example.techfix.databinding.FragmentTechRepairsBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.RepairAdapter;

import java.util.ArrayList;
import java.util.List;

public class TechRepairsFragment extends Fragment {

    private FragmentTechRepairsBinding binding;
    private TechViewModel viewModel;
    private RepairAdapter adapter;

    private List<RepairItem> allRepairs = new ArrayList<>();
    private String selectedFilter = "ALL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TechViewModel.class);

        adapter = new RepairAdapter(repairItem -> {
            Bundle bundle = new Bundle();
            bundle.putString("repairId", repairItem.getId());
            Navigation.findNavController(requireView()).navigate(R.id.action_techRepairs_to_techRepairDetail, bundle);
        });

        binding.rvAssignedRepairs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAssignedRepairs.setAdapter(adapter);

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            if (repairItems != null) {
                allRepairs = repairItems;
                filterAndRender();
            }
        });

        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty() || checkedIds.contains(R.id.chipAll)) {
                selectedFilter = "ALL";
            } else if (checkedIds.contains(R.id.chipUrgent)) {
                selectedFilter = "URGENT";
            } else if (checkedIds.contains(R.id.chipToday)) {
                selectedFilter = "TODAY";
            } else if (checkedIds.contains(R.id.chipInProgress)) {
                selectedFilter = "IN PROGRESS";
            }
            filterAndRender();
        });
    }

    private void filterAndRender() {
        List<RepairItem> filtered = new ArrayList<>();
        for (RepairItem item : allRepairs) {
            boolean matches = false;
            if ("ALL".equalsIgnoreCase(selectedFilter)) {
                matches = true;
            } else if ("URGENT".equalsIgnoreCase(selectedFilter)) {
                matches = "URGENT".equalsIgnoreCase(item.getStatus()) || "High".equalsIgnoreCase(item.getPriority());
            } else if ("TODAY".equalsIgnoreCase(selectedFilter)) {
                matches = item.getAppointmentDate() != null && item.getAppointmentDate().contains("2026-08-22");
            } else if ("IN PROGRESS".equalsIgnoreCase(selectedFilter)) {
                matches = "IN PROGRESS".equalsIgnoreCase(item.getStatus()) || "REPAIRING".equalsIgnoreCase(item.getStatus());
            }

            if (matches) {
                filtered.add(item);
            }
        }

        adapter.setRepairItems(filtered);
        if (filtered.isEmpty()) {
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.rvAssignedRepairs.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyState.setVisibility(View.GONE);
            binding.rvAssignedRepairs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
