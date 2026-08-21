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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminDashboardBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new AppointmentAdapter(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId", item.getId());
                Navigation.findNavController(requireView()).navigate(R.id.action_adminDashboard_to_adminAppointmentDetail, bundle);
            }

            @Override
            public void onAcceptClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "CONFIRMED");
                Toast.makeText(requireContext(), "Appointment #" + item.getAppointmentCode() + " Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRejectClick(AppointmentItem item) {
                viewModel.updateAppointmentStatus(item.getId(), "REJECTED");
                Toast.makeText(requireContext(), "Appointment #" + item.getAppointmentCode() + " Rejected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvRecentAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentAppointments.setAdapter(adapter);

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
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
                binding.tvPendingAppointments.setText(pendingCount + " Appointments");
            }
        });

        viewModel.getTechnicians().observe(getViewLifecycleOwner(), technicians -> {
            if (technicians != null) {
                binding.tvTotalTechnicians.setText(technicians.size() + " Staff");
            }
        });

        binding.tvViewAllAppointments.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
