package com.example.techfix.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminDashboardBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private FragmentAdminDashboardBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new AppointmentAdapter(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentItem item) {
                Intent intent = new Intent(AdminDashboardActivity.this, AdminAppointmentDetailActivity.class);
                intent.putExtra("appointmentId", item.getId());
                startActivity(intent);
            }

            @Override
            public void onAcceptClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "CONFIRMED");
                Toast.makeText(AdminDashboardActivity.this, "Appointment #" + item.getAppointmentCode() + " Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRejectClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "REJECTED");
                Toast.makeText(AdminDashboardActivity.this, "Appointment #" + item.getAppointmentCode() + " Rejected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvRecentAppointments.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecentAppointments.setAdapter(adapter);

        viewModel.getAppointments().observe(this, appointmentItems -> {
            if (appointmentItems != null) {
                List<AppointmentItem> recent = new ArrayList<>();
                int limit = Math.min(appointmentItems.size(), 3);
                for (int i = 0; i < limit; i++) {
                    recent.add(appointmentItems.get(i));
                }
                adapter.setAppointmentItems(recent);

                int pendingCount = 0;
                for (AppointmentItem appt : appointmentItems) {
                    if ("PENDING".equalsIgnoreCase(appt.getStatus())) {
                        pendingCount++;
                    }
                }
                binding.tvPendingAppointments.setText(String.valueOf(pendingCount));
            }
        });

        viewModel.getTechnicians().observe(this, technicians -> {
            if (technicians != null) {
                binding.tvTotalTechnicians.setText(String.valueOf(technicians.size()));
            }
        });

        binding.tvViewAllAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAppointmentsActivity.class);
            startActivity(intent);
        });
    }
}
