package com.example.techfix.ui.admin;

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

import com.example.techfix.databinding.FragmentAdminSparePartsBinding;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.ui.common.SparePartAdapter;

public class AdminSparePartsFragment extends Fragment {

    private FragmentAdminSparePartsBinding binding;
    private AdminViewModel viewModel;
    private SparePartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminSparePartsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new SparePartAdapter(part -> {
            int newQty = part.getQuantity() + 5;
            part.setQuantity(newQty);
            viewModel.updateSparePart(part);
            Toast.makeText(requireContext(), "Restocked +5 units for " + part.getName() + " (Total: " + newQty + ")", Toast.LENGTH_SHORT).show();
        });

        binding.rvSparePartsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSparePartsList.setAdapter(adapter);

        viewModel.getSpareParts().observe(getViewLifecycleOwner(), sparePartItems -> {
            if (sparePartItems != null) {
                adapter.setParts(sparePartItems);
            }
        });

        if (binding.btnAddPart != null) {
            binding.btnAddPart.setOnClickListener(v -> {
                SparePartItem newPart = new SparePartItem(
                        "PART_007",
                        "iPhone 15 Pro OLED Display",
                        "Mobile Display",
                        "iPhone 15 Pro",
                        6,
                        45000.0,
                        2
                );
                viewModel.addSparePart(newPart);
                Toast.makeText(requireContext(), "Added iPhone 15 Pro OLED Display!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
