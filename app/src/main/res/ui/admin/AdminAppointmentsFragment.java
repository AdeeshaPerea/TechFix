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
import com.example.techfix.databinding.FragmentAdminAppointmentsBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

public class AdminAppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener {

    private FragmentAdminAppointmentsBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;

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

        adapter = new AppointmentAdapter(this);
        binding.rvAppointmentsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAppointmentsList.setAdapter(adapter);

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                adapter.setAppointmentItems(appointmentItems);
                if (binding.tvSubtitle != null) {
                    binding.tvSubtitle.setText(appointmentItems.size() + " Total Appointments");
                }
            }
        });
    }

    @Override
    public void onAppointmentClick(AppointmentItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("appointmentId", item.getId());
        Navigation.findNavController(requireView()).navigate(
                R.id.action_adminAppointments_to_adminAppointmentDetail,
                bundle
        );
    }

    @Override
    public void onAcceptClick(AppointmentItem item) {
        viewModel.updateAppointmentStatus(item.getId(), "CONFIRMED");
        Toast.makeText(requireContext(), "Appointment #" + item.getAppointmentCode() + " Accepted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRejectClick(AppointmentItem item) {
        viewModel.updateAppointmentStatus(item.getId(), "REJECTED");
        Toast.makeText(requireContext(), "Appointment #" + item.getAppointmentCode() + " Rejected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
