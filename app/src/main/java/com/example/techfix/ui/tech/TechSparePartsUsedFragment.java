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

import com.example.techfix.databinding.FragmentTechSparePartsUsedBinding;
import com.example.techfix.ui.common.FormatUtils;
import com.example.techfix.ui.common.SparePartAdapter;

public class TechSparePartsUsedFragment extends Fragment {

    private FragmentTechSparePartsUsedBinding binding;
    private TechViewModel viewModel;
    private SparePartAdapter adapter;
    private String repairId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechSparePartsUsedBinding.inflate(inflater, container, false);
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

        adapter = new SparePartAdapter(part -> {
            viewModel.addSparePartUsed(repairId, part.getId(), part.getName(), 1, part.getUnitPriceLkr());
            Toast.makeText(requireContext(), "Added 1x " + part.getName() + " (" + FormatUtils.formatCurrency(part.getUnitPriceLkr()) + ") to Repair", Toast.LENGTH_LONG).show();
        });

        binding.rvPartsCatalog.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPartsCatalog.setAdapter(adapter);

        viewModel.getSparePartsCatalog().observe(getViewLifecycleOwner(), parts -> {
            if (parts != null) {
                adapter.setParts(parts);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
