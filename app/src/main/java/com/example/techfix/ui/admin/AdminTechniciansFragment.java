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

import com.example.techfix.databinding.FragmentAdminTechniciansBinding;
import com.example.techfix.model.User;
import com.example.techfix.ui.common.TechnicianAdapter;

import java.util.UUID;

public class AdminTechniciansFragment extends Fragment {

    private FragmentAdminTechniciansBinding binding;
    private AdminViewModel viewModel;
    private TechnicianAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminTechniciansBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        adapter = new TechnicianAdapter(this::showEditOrDeleteDialog);

        binding.rvTechniciansList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTechniciansList.setAdapter(adapter);

        viewModel.getTechnicians().observe(getViewLifecycleOwner(), technicians -> {
            if (technicians != null) {
                adapter.setTechnicians(technicians);
            }
        });

        binding.btnAddTechnician.setOnClickListener(v -> showAddTechnicianDialog());
    }

    private void showAddTechnicianDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Technician");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setHint("Technician Name (e.g. Nalin Jayawardena)");
        layout.addView(etName);

        final EditText etEmail = new EditText(requireContext());
        etEmail.setHint("Email Address");
        layout.addView(etEmail);

        final EditText etPhone = new EditText(requireContext());
        etPhone.setHint("Phone Number");
        layout.addView(etPhone);

        final EditText etSpecialization = new EditText(requireContext());
        etSpecialization.setHint("Specialization (e.g. Mobile & Micro-soldering)");
        layout.addView(etSpecialization);

        final EditText etAvailability = new EditText(requireContext());
        etAvailability.setHint("Availability / Hours (e.g. 08:30 AM - 05:30 PM)");
        layout.addView(etAvailability);

        final EditText etBranch = new EditText(requireContext());
        etBranch.setHint("Branch (e.g. TechFix Colombo)");
        layout.addView(etBranch);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String spec = etSpecialization.getText().toString().trim();
            String hours = etAvailability.getText().toString().trim();
            String branch = etBranch.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            User tech = new User(
                    "TECH_" + UUID.randomUUID().toString().substring(0, 5),
                    name,
                    email.isEmpty() ? name.toLowerCase().replace(" ", "") + "@techfix.com" : email,
                    phone.isEmpty() ? "+94 77 123 4567" : phone,
                    "TECH",
                    spec.isEmpty() ? "General Hardware & Software" : spec,
                    "BRANCH_01",
                    branch.isEmpty() ? "TechFix Main Branch" : branch,
                    hours.isEmpty() ? "09:00 AM - 06:00 PM (AVAILABLE)" : hours,
                    0
            );

            viewModel.addTechnician(tech);
            Toast.makeText(requireContext(), "Added technician " + name, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditOrDeleteDialog(User tech) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Manage Technician: " + tech.getName());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etName = new EditText(requireContext());
        etName.setText(tech.getName());
        layout.addView(etName);

        final EditText etSpecialization = new EditText(requireContext());
        etSpecialization.setText(tech.getSpecialization());
        etSpecialization.setHint("Specialization");
        layout.addView(etSpecialization);

        final EditText etAvailability = new EditText(requireContext());
        etAvailability.setText(tech.getWorkingHours());
        etAvailability.setHint("Availability / Working Hours");
        layout.addView(etAvailability);

        final EditText etBranch = new EditText(requireContext());
        etBranch.setText(tech.getBranchName());
        etBranch.setHint("Branch");
        layout.addView(etBranch);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            tech.setName(etName.getText().toString().trim());
            tech.setSpecialization(etSpecialization.getText().toString().trim());
            tech.setWorkingHours(etAvailability.getText().toString().trim());
            tech.setBranchName(etBranch.getText().toString().trim());

            viewModel.updateTechnician(tech);
            Toast.makeText(requireContext(), "Updated technician details!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Delete", (dialog, which) -> {
            viewModel.deleteTechnician(tech.getId());
            Toast.makeText(requireContext(), "Technician deleted!", Toast.LENGTH_SHORT).show();
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
