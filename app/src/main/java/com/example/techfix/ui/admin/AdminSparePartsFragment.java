package com.example.techfix.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminSparePartsBinding;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.ui.common.SparePartAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminSparePartsFragment extends Fragment {

    private FragmentAdminSparePartsBinding binding;
    private AdminViewModel viewModel;
    private SparePartAdapter adapter;

    private List<SparePartItem> allPartsList = new ArrayList<>();
    private boolean showingLowStockOnly = false;

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

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        adapter = new SparePartAdapter(this::showEditOrDeletePartDialog);

        binding.rvSparePartsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSparePartsList.setAdapter(adapter);

        viewModel.getSpareParts().observe(getViewLifecycleOwner(), sparePartItems -> {
            if (sparePartItems != null) {
                allPartsList = sparePartItems;
                updateListAndCounts();
            }
        });

        if (binding.chipAllParts != null) {
            binding.chipAllParts.setOnClickListener(v -> {
                showingLowStockOnly = false;
                binding.chipAllParts.setBackgroundResource(R.drawable.bg_chip_selected);
                binding.chipAllParts.setTextColor(requireContext().getColor(R.color.techfix_white));
                binding.chipLowStock.setBackgroundResource(R.drawable.bg_chip_unselected);
                binding.chipLowStock.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
                updateListAndCounts();
            });
        }

        if (binding.chipLowStock != null) {
            binding.chipLowStock.setOnClickListener(v -> {
                showingLowStockOnly = true;
                binding.chipLowStock.setBackgroundResource(R.drawable.bg_chip_selected);
                binding.chipLowStock.setTextColor(requireContext().getColor(R.color.techfix_white));
                binding.chipAllParts.setBackgroundResource(R.drawable.bg_chip_unselected);
                binding.chipAllParts.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
                updateListAndCounts();
            });
        }

        if (binding.btnAddPart != null) {
            binding.btnAddPart.setOnClickListener(v -> showAddPartDialog());
        }
    }

    private void updateListAndCounts() {
        int lowStockCount = 0;
        List<SparePartItem> filtered = new ArrayList<>();

        for (SparePartItem item : allPartsList) {
            if (item.getQuantity() <= item.getMinStockThreshold() || item.getQuantity() <= 5) {
                lowStockCount++;
                if (showingLowStockOnly) {
                    filtered.add(item);
                }
            }
            if (!showingLowStockOnly) {
                filtered.add(item);
            }
        }

        if (binding.chipLowStock != null) {
            binding.chipLowStock.setText("Low Stock (" + lowStockCount + ")");
        }

        adapter.setParts(filtered);
    }

    private void showAddPartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Spare Part");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setHint("Part Name (e.g. iPhone 15 Pro OLED Panel)");
        layout.addView(etName);

        final EditText etCategory = new EditText(requireContext());
        etCategory.setHint("Category (e.g. Mobile Display)");
        layout.addView(etCategory);

        final EditText etModel = new EditText(requireContext());
        etModel.setHint("Compatible Model (e.g. iPhone 15 Pro)");
        layout.addView(etModel);

        final EditText etQuantity = new EditText(requireContext());
        etQuantity.setHint("Stock Quantity (e.g. 10)");
        etQuantity.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etQuantity);

        final EditText etPrice = new EditText(requireContext());
        etPrice.setHint("Price LKR (e.g. 28500)");
        etPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etPrice);

        final EditText etMinThreshold = new EditText(requireContext());
        etMinThreshold.setHint("Low Stock Threshold (e.g. 3)");
        etMinThreshold.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etMinThreshold);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String qtyStr = etQuantity.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String thresholdStr = etMinThreshold.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Part name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty = qtyStr.isEmpty() ? 5 : Integer.parseInt(qtyStr);
            double price = priceStr.isEmpty() ? 15000.0 : Double.parseDouble(priceStr);
            int threshold = thresholdStr.isEmpty() ? 3 : Integer.parseInt(thresholdStr);

            SparePartItem part = new SparePartItem(
                    "PART_" + UUID.randomUUID().toString().substring(0, 5),
                    name,
                    category.isEmpty() ? "General Component" : category,
                    model.isEmpty() ? "Universal" : model,
                    qty,
                    price,
                    threshold
            );

            viewModel.addSparePart(part);
            Toast.makeText(requireContext(), "Added spare part " + name, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditOrDeletePartDialog(SparePartItem part) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Manage Spare Part: " + part.getName());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setText(part.getName());
        layout.addView(etName);

        final EditText etQuantity = new EditText(requireContext());
        etQuantity.setText(String.valueOf(part.getQuantity()));
        etQuantity.setHint("Stock Quantity");
        etQuantity.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etQuantity);

        final EditText etPrice = new EditText(requireContext());
        etPrice.setText(String.valueOf(part.getUnitPriceLkr()));
        etPrice.setHint("Price (LKR)");
        etPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etPrice);

        final EditText etMinThreshold = new EditText(requireContext());
        etMinThreshold.setText(String.valueOf(part.getMinStockThreshold()));
        etMinThreshold.setHint("Low Stock Threshold");
        etMinThreshold.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etMinThreshold);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            part.setName(etName.getText().toString().trim());

            String qtyStr = etQuantity.getText().toString().trim();
            if (!qtyStr.isEmpty()) part.setQuantity(Integer.parseInt(qtyStr));

            String priceStr = etPrice.getText().toString().trim();
            if (!priceStr.isEmpty()) part.setUnitPriceLkr(Double.parseDouble(priceStr));

            String thresholdStr = etMinThreshold.getText().toString().trim();
            if (!thresholdStr.isEmpty()) part.setMinStockThreshold(Integer.parseInt(thresholdStr));

            viewModel.updateSparePart(part);
            Toast.makeText(requireContext(), "Updated stock & price details!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Delete", (dialog, which) -> {
            viewModel.deleteSparePart(part.getId());
            Toast.makeText(requireContext(), "Spare part deleted!", Toast.LENGTH_SHORT).show();
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
