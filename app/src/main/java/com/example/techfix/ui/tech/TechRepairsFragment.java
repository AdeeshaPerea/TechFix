package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private String searchQuery = "";

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

        // Search text watcher
        if (binding.etSearchRepairs != null) {
            binding.etSearchRepairs.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchQuery = s.toString().trim().toLowerCase();
                    filterAndRender();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Chip Filters
        if (binding.chipAll != null) binding.chipAll.setOnClickListener(v -> setFilter("ALL"));
        if (binding.chipUrgent != null) binding.chipUrgent.setOnClickListener(v -> setFilter("PENDING"));
        if (binding.chipInProgress != null) binding.chipInProgress.setOnClickListener(v -> setFilter("IN PROGRESS"));
        if (binding.chipToday != null) binding.chipToday.setOnClickListener(v -> setFilter("COMPLETED"));

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            if (repairItems != null) {
                allRepairs = repairItems;
                filterAndRender();
            }
        });
    }

    private void setFilter(String filter) {
        selectedFilter = filter;

        int activeBg = R.drawable.bg_pill_orange_solid;
        int inactiveBg = R.drawable.bg_white_pill;
        int activeText = ContextCompat.getColor(requireContext(), R.color.techfix_white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.techfix_navy);

        if (binding.chipAll != null) {
            binding.chipAll.setBackgroundResource("ALL".equals(filter) ? activeBg : inactiveBg);
            binding.chipAll.setTextColor("ALL".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipUrgent != null) {
            binding.chipUrgent.setBackgroundResource("PENDING".equals(filter) ? activeBg : inactiveBg);
            binding.chipUrgent.setTextColor("PENDING".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipInProgress != null) {
            binding.chipInProgress.setBackgroundResource("IN PROGRESS".equals(filter) ? activeBg : inactiveBg);
            binding.chipInProgress.setTextColor("IN PROGRESS".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipToday != null) {
            binding.chipToday.setBackgroundResource("COMPLETED".equals(filter) ? activeBg : inactiveBg);
            binding.chipToday.setTextColor("COMPLETED".equals(filter) ? activeText : inactiveText);
        }

        filterAndRender();
    }

    private void filterAndRender() {
        List<RepairItem> filtered = new ArrayList<>();
        for (RepairItem item : allRepairs) {
            String status = item.getStatus() != null ? item.getStatus().toUpperCase() : "";
            boolean matchesFilter = true;

            if ("PENDING".equalsIgnoreCase(selectedFilter)) {
                matchesFilter = status.contains("PENDING") || status.contains("NEW");
            } else if ("IN PROGRESS".equalsIgnoreCase(selectedFilter)) {
                matchesFilter = status.contains("PROGRESS") || status.contains("ACTIVE") || status.contains("REPAIRING");
            } else if ("COMPLETED".equalsIgnoreCase(selectedFilter)) {
                matchesFilter = status.contains("COMPLETED") || status.contains("DONE");
            }

            if (matchesFilter) {
                if (!searchQuery.isEmpty()) {
                    String customer = item.getCustomerName() != null ? item.getCustomerName().toLowerCase() : "";
                    String device = item.getDeviceName() != null ? item.getDeviceName().toLowerCase() : "";
                    String code = item.getRepairCode() != null ? item.getRepairCode().toLowerCase() : "";
                    if (customer.contains(searchQuery) || device.contains(searchQuery) || code.contains(searchQuery)) {
                        filtered.add(item);
                    }
                } else {
                    filtered.add(item);
                }
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
