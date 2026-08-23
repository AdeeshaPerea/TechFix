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

import com.example.techfix.databinding.FragmentAdminAppointmentDetailBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.FormatUtils;

public class AdminAppointmentDetailFragment extends Fragment {

    private FragmentAdminAppointmentDetailBinding binding;
    private AdminViewModel viewModel;
    private String appointmentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminAppointmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (getArguments() != null) {
            appointmentId = getArguments().getString("appointmentId", "APT_001");
        } else {
            appointmentId = "APT_001";
        }

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            for (AppointmentItem item : appointmentItems) {
                if (item.getId().equals(appointmentId)) {
                    bindDetails(item);
                    break;
                }
            }
        });

        binding.btnAcceptApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "CONFIRMED");
            Toast.makeText(requireContext(), "Appointment Accepted!", Toast.LENGTH_SHORT).show();
        });

        binding.btnRejectApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "REJECTED");
            Toast.makeText(requireContext(), "Appointment Rejected!", Toast.LENGTH_SHORT).show();
        });

        binding.btnAssignTechnician.setOnClickListener(v -> {
            viewModel.assignTechnicianToAppointment(appointmentId, "TECH_001", "Alex Perera");
            Toast.makeText(requireContext(), "Assigned to Tech Alex Perera!", Toast.LENGTH_SHORT).show();
        });

        binding.btnChangeBranch.setOnClickListener(v -> {
            viewModel.changeAppointmentBranch(appointmentId, "BRANCH_02", "TechFix Galle");
            Toast.makeText(requireContext(), "Branch re-assigned to TechFix Galle!", Toast.LENGTH_SHORT).show();
        });
    }

    private void bindDetails(AppointmentItem item) {
        binding.tvAptCode.setText(item.getAppointmentCode());
        binding.tvAptStatusBadge.setText(item.getStatus());
        binding.tvAptStatusBadge.setTextColor(FormatUtils.getStatusTextColor(item.getStatus()));

        binding.tvAptDevice.setText(item.getDeviceModel());
        binding.tvAptService.setText("Service Requested: " + item.getServiceRequested());
        binding.tvAptCustomer.setText(item.getCustomerName() + " (" + item.getCustomerPhone() + ")");
        binding.tvAptDateTime.setText(item.getPreferredDate() + " at " + item.getPreferredTime());
        binding.tvAptBranchTech.setText(item.getBranchName() + " • " + (item.getAssignedTechName() != null ? item.getAssignedTechName() : "Unassigned"));
        binding.tvAptProblem.setText(item.getProblemDescription());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
