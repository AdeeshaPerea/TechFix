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

import com.example.techfix.databinding.FragmentAdminBranchesBinding;
import com.example.techfix.model.BranchItem;
import com.example.techfix.ui.common.BranchAdapter;

import java.util.UUID;

public class AdminBranchesFragment extends Fragment {

    private FragmentAdminBranchesBinding binding;
    private AdminViewModel viewModel;
    private BranchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBranchesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        adapter = new BranchAdapter(this::showEditOrDeleteBranchDialog);
        binding.rvBranchesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBranchesList.setAdapter(adapter);

        viewModel.getBranches().observe(getViewLifecycleOwner(), branches -> {
            if (branches != null) {
                adapter.setBranches(branches);
            }
        });

        binding.btnAddBranch.setOnClickListener(v -> showAddBranchDialog());
    }

    private void showAddBranchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Branch");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setHint("Branch Name (e.g. TechFix Kandy)");
        layout.addView(etName);

        final EditText etAddress = new EditText(requireContext());
        etAddress.setHint("Address (e.g. 45 Peradeniya Road, Kandy)");
        layout.addView(etAddress);

        final EditText etPhone = new EditText(requireContext());
        etPhone.setHint("Phone (e.g. +94 81 222 3344)");
        layout.addView(etPhone);

        final EditText etHours = new EditText(requireContext());
        etHours.setHint("Opening Hours (e.g. 08:30 AM - 06:00 PM)");
        layout.addView(etHours);

        final EditText etLocation = new EditText(requireContext());
        etLocation.setHint("GPS Lat, Long (e.g. 7.2906, 80.6337)");
        layout.addView(etLocation);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String hours = etHours.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Branch name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            double lat = 6.9271;
            double lon = 79.8612;
            if (!loc.isEmpty() && loc.contains(",")) {
                try {
                    String[] parts = loc.split(",");
                    lat = Double.parseDouble(parts[0].trim());
                    lon = Double.parseDouble(parts[1].trim());
                } catch (Exception ignored) {}
            }

            BranchItem branch = new BranchItem(
                    "BRANCH_" + UUID.randomUUID().toString().substring(0, 5),
                    name,
                    address.isEmpty() ? "Main Road" : address,
                    phone.isEmpty() ? "+94 11 000 0000" : phone,
                    hours.isEmpty() ? "08:30 AM - 06:00 PM" : hours,
                    lat,
                    lon,
                    3,
                    2
            );

            viewModel.addBranch(branch);
            Toast.makeText(requireContext(), "Added branch " + name, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditOrDeleteBranchDialog(BranchItem branch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Branch: " + branch.getName());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setText(branch.getName());
        layout.addView(etName);

        final EditText etAddress = new EditText(requireContext());
        etAddress.setText(branch.getAddress());
        etAddress.setHint("Address");
        layout.addView(etAddress);

        final EditText etPhone = new EditText(requireContext());
        etPhone.setText(branch.getPhone());
        etPhone.setHint("Phone");
        layout.addView(etPhone);

        final EditText etHours = new EditText(requireContext());
        etHours.setText(branch.getOpeningHours());
        etHours.setHint("Opening Hours");
        layout.addView(etHours);

        final EditText etLocation = new EditText(requireContext());
        etLocation.setText(branch.getLatitude() + ", " + branch.getLongitude());
        etLocation.setHint("GPS Lat, Long");
        layout.addView(etLocation);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            branch.setName(etName.getText().toString().trim());
            branch.setAddress(etAddress.getText().toString().trim());
            branch.setPhone(etPhone.getText().toString().trim());
            branch.setOpeningHours(etHours.getText().toString().trim());

            String loc = etLocation.getText().toString().trim();
            if (!loc.isEmpty() && loc.contains(",")) {
                try {
                    String[] parts = loc.split(",");
                    branch.setLatitude(Double.parseDouble(parts[0].trim()));
                    branch.setLongitude(Double.parseDouble(parts[1].trim()));
                } catch (Exception ignored) {}
            }

            viewModel.updateBranch(branch);
            Toast.makeText(requireContext(), "Updated branch details!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Delete", (dialog, which) -> {
            viewModel.deleteBranch(branch.getId());
            Toast.makeText(requireContext(), "Branch deleted!", Toast.LENGTH_SHORT).show();
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
