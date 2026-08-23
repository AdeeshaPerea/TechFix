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

import com.example.techfix.databinding.FragmentAdminAppointmentsBinding;

public class AdminAppointmentsFragment extends Fragment {

    private FragmentAdminAppointmentsBinding binding;
    private AdminViewModel viewModel;
    private String currentAppointmentId = "APT_001";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminAppointmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        if (binding.btnAccept != null) {
            binding.btnAccept.setOnClickListener(v -> {
                viewModel.updateAppointmentStatus(currentAppointmentId, "CONFIRMED");
                Toast.makeText(requireContext(), "Appointment Accepted!", Toast.LENGTH_SHORT).show();
            });
        }

        if (binding.btnReject != null) {
            binding.btnReject.setOnClickListener(v -> {
                viewModel.updateAppointmentStatus(currentAppointmentId, "REJECTED");
                Toast.makeText(requireContext(), "Appointment Rejected", Toast.LENGTH_SHORT).show();
            });
        }

        if (binding.btnConfirmAppointment != null) {
            binding.btnConfirmAppointment.setOnClickListener(v -> {
                String selectedTech = binding.spinnerTechnician != null && binding.spinnerTechnician.getSelectedItem() != null ?
                        binding.spinnerTechnician.getSelectedItem().toString() : "Nuwan Silva";

                String selectedBranch = binding.spinnerBranch != null && binding.spinnerBranch.getSelectedItem() != null ?
                        binding.spinnerBranch.getSelectedItem().toString() : "Colombo 03";

                viewModel.assignTechnicianToAppointment(currentAppointmentId, "TECH_001", selectedTech);
                viewModel.changeAppointmentBranch(currentAppointmentId, "BRANCH_01", selectedBranch);

                Toast.makeText(requireContext(), "Assigned to " + selectedTech + " at " + selectedBranch, Toast.LENGTH_LONG).show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
