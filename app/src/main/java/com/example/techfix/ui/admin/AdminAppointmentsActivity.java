package com.example.techfix.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminAppointmentsBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

public class AdminAppointmentsActivity extends AppCompatActivity {

    private FragmentAdminAppointmentsBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new AppointmentAdapter(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentItem item) {
                Intent intent = new Intent(AdminAppointmentsActivity.this, AdminAppointmentDetailActivity.class);
                intent.putExtra("appointmentId", item.getId());
                startActivity(intent);
            }

            @Override
            public void onAcceptClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "CONFIRMED");
            }

            @Override
            public void onRejectClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "REJECTED");
            }
        });

        binding.rvAppointmentsList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAppointmentsList.setAdapter(adapter);

        viewModel.getAppointments().observe(this, appointmentItems -> {
            if (appointmentItems != null) {
                adapter.setAppointmentItems(appointmentItems);
            }
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
