package com.example.techfix.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminAppointmentDetailBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.model.BranchItem;
import com.example.techfix.model.User;
import com.example.techfix.ui.common.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentDetailFragment extends Fragment {

    private FragmentAdminAppointmentDetailBinding binding;
    private AdminViewModel viewModel;
    private String appointmentId;

    private List<User> availableTechs = new ArrayList<>();
    private List<BranchItem> availableBranches = new ArrayList<>();

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

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                for (AppointmentItem item : appointmentItems) {
                    if (item.getId().equals(appointmentId)) {
                        bindDetails(item);
                        break;
                    }
                }
            }
        });

        viewModel.getTechnicians().observe(getViewLifecycleOwner(), techs -> {
            if (techs != null) availableTechs = techs;
        });

        viewModel.getBranches().observe(getViewLifecycleOwner(), branches -> {
            if (branches != null) availableBranches = branches;
        });

        binding.btnAcceptApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "CONFIRMED");
            Toast.makeText(requireContext(), "Appointment Accepted!", Toast.LENGTH_SHORT).show();
        });

        binding.btnRejectApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "REJECTED");
            Toast.makeText(requireContext(), "Appointment Rejected!", Toast.LENGTH_SHORT).show();
        });

        binding.btnAssignTechnician.setOnClickListener(v -> showAssignTechnicianDialog());

        binding.btnChangeBranch.setOnClickListener(v -> showChangeBranchDialog());

        if (binding.btnViewRepairPhotos != null) {
            binding.btnViewRepairPhotos.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("repairId", appointmentId != null ? appointmentId : "REP_001");
                androidx.navigation.Navigation.findNavController(v).navigate(R.id.action_adminAppointmentDetail_to_adminGallery, bundle);
            });
        }
    }

    private void showAssignTechnicianDialog() {
        if (availableTechs.isEmpty()) {
            Toast.makeText(requireContext(), "No technicians available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] techNames = new String[availableTechs.size()];
        for (int i = 0; i < availableTechs.size(); i++) {
            User tech = availableTechs.get(i);
            techNames[i] = tech.getName() + " (" + tech.getSpecialization() + ")";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Assign Technician");
        builder.setItems(techNames, (dialog, which) -> {
            User selected = availableTechs.get(which);
            viewModel.assignTechnicianToAppointment(appointmentId, selected.getId(), selected.getName());
            Toast.makeText(requireContext(), "Assigned to " + selected.getName(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showChangeBranchDialog() {
        if (availableBranches.isEmpty()) {
            Toast.makeText(requireContext(), "No branches available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] branchNames = new String[availableBranches.size()];
        for (int i = 0; i < availableBranches.size(); i++) {
            branchNames[i] = availableBranches.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Assign Branch");
        builder.setItems(branchNames, (dialog, which) -> {
            BranchItem selected = availableBranches.get(which);
            viewModel.changeAppointmentBranch(appointmentId, selected.getId(), selected.getName());
            Toast.makeText(requireContext(), "Branch updated to " + selected.getName(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void bindDetails(AppointmentItem item) {
        binding.tvAptCode.setText(item.getAppointmentCode() != null ? item.getAppointmentCode() : item.getId());
        binding.tvAptStatusBadge.setText(item.getStatus() != null ? item.getStatus() : "PENDING");
        binding.tvAptStatusBadge.setTextColor(FormatUtils.getStatusTextColor(item.getStatus()));

        binding.tvAptDevice.setText(item.getDeviceModel() != null ? item.getDeviceModel() : "Device");
        binding.tvAptService.setText("Service Requested: " + (item.getServiceRequested() != null ? item.getServiceRequested() : "General Checkup"));
        binding.tvAptCustomer.setText((item.getCustomerName() != null ? item.getCustomerName() : "Customer") +
                (item.getCustomerPhone() != null ? " (" + item.getCustomerPhone() + ")" : ""));
        binding.tvAptDateTime.setText((item.getPreferredDate() != null ? item.getPreferredDate() : "Date TBD") +
                " at " + (item.getPreferredTime() != null ? item.getPreferredTime() : "Time TBD"));
        binding.tvAptBranchTech.setText((item.getBranchName() != null ? item.getBranchName() : "Main Branch") +
                " • " + (item.getAssignedTechName() != null && !item.getAssignedTechName().isEmpty() ? item.getAssignedTechName() : "Unassigned"));
        binding.tvAptProblem.setText(item.getProblemDescription() != null ? item.getProblemDescription() : "No details provided.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
