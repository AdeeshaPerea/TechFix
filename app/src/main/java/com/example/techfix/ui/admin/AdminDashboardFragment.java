package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter appointmentAdapter;

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

        appointmentAdapter = new AppointmentAdapter(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId", item.getId());
                Navigation.findNavController(requireView()).navigate(R.id.action_adminDashboard_to_adminAppointmentDetail, bundle);
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

        binding.rvRecentAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentAppointments.setAdapter(appointmentAdapter);

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                appointmentAdapter.setAppointmentItems(appointmentItems);
                int pending = 0;
                for (AppointmentItem a : appointmentItems) {
                    if ("PENDING".equals(a.getStatus())) pending++;
                }
                binding.tvPendingAppointments.setText(String.valueOf(pending));
            }
        });

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            if (repairItems != null) {
                binding.tvTotalRepairs.setText(String.valueOf(repairItems.size()));
                int completed = 0;
                for (var r : repairItems) {
                    if ("COMPLETED".equals(r.getStatus())) completed++;
                }
                binding.tvCompletedRepairs.setText(String.valueOf(completed));
            }
        });

        viewModel.getTechnicians().observe(getViewLifecycleOwner(), technicians -> {
            if (technicians != null) {
                binding.tvActiveTechs.setText(String.valueOf(technicians.size()));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
