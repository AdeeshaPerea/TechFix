package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.techfix.databinding.FragmentAdminAppointmentDetailBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.FormatUtils;

public class AdminAppointmentDetailActivity extends AppCompatActivity {

    private FragmentAdminAppointmentDetailBinding binding;
    private AdminViewModel viewModel;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminAppointmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        appointmentId = getIntent() != null ? getIntent().getStringExtra("appointmentId") : "APT_001";
        if (appointmentId == null) appointmentId = "APT_001";

        viewModel.getAppointments().observe(this, appointmentItems -> {
            for (AppointmentItem item : appointmentItems) {
                if (item.getId().equals(appointmentId)) {
                    bindDetails(item);
                    break;
                }
            }
        });

        binding.btnAcceptApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "CONFIRMED");
            Toast.makeText(this, "Appointment Accepted!", Toast.LENGTH_SHORT).show();
        });

        binding.btnRejectApt.setOnClickListener(v -> {
            viewModel.updateAppointmentStatus(appointmentId, "REJECTED");
            Toast.makeText(this, "Appointment Rejected!", Toast.LENGTH_SHORT).show();
        });

        binding.btnAssignTechnician.setOnClickListener(v -> {
            viewModel.assignTechnicianToAppointment(appointmentId, "TECH_001", "Alex Perera");
            Toast.makeText(this, "Assigned to Tech Alex Perera!", Toast.LENGTH_SHORT).show();
        });

        binding.btnChangeBranch.setOnClickListener(v -> {
            viewModel.changeAppointmentBranch(appointmentId, "BRANCH_02", "TechFix Galle");
            Toast.makeText(this, "Branch re-assigned to TechFix Galle!", Toast.LENGTH_SHORT).show();
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
}
