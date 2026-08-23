package com.example.techfix.ui.tech;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techfix.databinding.FragmentTechSparePartsUsedBinding;
import com.example.techfix.model.SparePartItem;

import java.util.ArrayList;
import java.util.List;

public class TechSparePartsUsedFragment extends Fragment {

    private FragmentTechSparePartsUsedBinding binding;
    private TechViewModel viewModel;
    private String repairId;
    private List<SparePartItem> catalog = new ArrayList<>();

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

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        viewModel.getSparePartsCatalog().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                catalog = items;
            }
        });

        if (binding.btnAddPart != null) {
            binding.btnAddPart.setOnClickListener(v -> showSelectPartDialog());
        }

        if (binding.btnSaveNotes != null) {
            binding.btnSaveNotes.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Spare Parts Logged & Saved!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            });
        }
    }

    private void showSelectPartDialog() {
        if (catalog.isEmpty()) {
            Toast.makeText(requireContext(), "Spare parts catalog loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] partNames = new String[catalog.size()];
        for (int i = 0; i < catalog.size(); i++) {
            SparePartItem p = catalog.get(i);
            partNames[i] = p.getName() + " (Rs. " + (int) p.getUnitPriceLkr() + ")";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Spare Part Used");
        builder.setItems(partNames, (dialog, which) -> {
            SparePartItem selectedPart = catalog.get(which);
            promptForQuantity(selectedPart);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void promptForQuantity(SparePartItem part) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Quantity for " + part.getName());

        final EditText etQty = new EditText(requireContext());
        etQty.setInputType(InputType.TYPE_CLASS_NUMBER);
        etQty.setHint("Quantity (Default: 1)");
        
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setPadding(40, 20, 40, 20);
        layout.addView(etQty);
        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String qStr = etQty.getText().toString().trim();
            int qty = qStr.isEmpty() ? 1 : Integer.parseInt(qStr);

            viewModel.addSparePartUsed(repairId, part.getId(), part.getName(), qty, part.getUnitPriceLkr());
            Toast.makeText(requireContext(), "Added " + qty + "x " + part.getName(), Toast.LENGTH_SHORT).show();
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
