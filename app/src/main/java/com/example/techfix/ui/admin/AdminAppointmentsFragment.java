package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminAppointmentsBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener {

    private FragmentAdminAppointmentsBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;
    private List<AppointmentItem> fullList = new ArrayList<>();
    private String currentFilter = "ALL";
    private String searchQuery = "";

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

        // Search TextWatcher
        if (binding.etSearchAppts != null) {
            binding.etSearchAppts.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchQuery = s.toString().trim().toLowerCase();
                    applyFilter();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Chip Filters
        if (binding.chipFilterAll != null) binding.chipFilterAll.setOnClickListener(v -> setFilter("ALL"));
        if (binding.chipFilterPending != null) binding.chipFilterPending.setOnClickListener(v -> setFilter("PENDING"));
        if (binding.chipFilterInProgress != null) binding.chipFilterInProgress.setOnClickListener(v -> setFilter("IN PROGRESS"));
        if (binding.chipFilterCompleted != null) binding.chipFilterCompleted.setOnClickListener(v -> setFilter("COMPLETED"));

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                fullList = appointmentItems;
                applyFilter();
            }
        });
    }

    private void setFilter(String filter) {
        currentFilter = filter;

        int activeBg = R.drawable.bg_pill_orange_solid;
        int inactiveBg = R.drawable.bg_white_pill;
        int activeText = ContextCompat.getColor(requireContext(), R.color.techfix_white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.techfix_navy);

        if (binding.chipFilterAll != null) {
            binding.chipFilterAll.setBackgroundResource("ALL".equals(filter) ? activeBg : inactiveBg);
            binding.chipFilterAll.setTextColor("ALL".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipFilterPending != null) {
            binding.chipFilterPending.setBackgroundResource("PENDING".equals(filter) ? activeBg : inactiveBg);
            binding.chipFilterPending.setTextColor("PENDING".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipFilterInProgress != null) {
            binding.chipFilterInProgress.setBackgroundResource("IN PROGRESS".equals(filter) ? activeBg : inactiveBg);
            binding.chipFilterInProgress.setTextColor("IN PROGRESS".equals(filter) ? activeText : inactiveText);
        }
        if (binding.chipFilterCompleted != null) {
            binding.chipFilterCompleted.setBackgroundResource("COMPLETED".equals(filter) ? activeBg : inactiveBg);
            binding.chipFilterCompleted.setTextColor("COMPLETED".equals(filter) ? activeText : inactiveText);
        }

        applyFilter();
    }

    private void applyFilter() {
        List<AppointmentItem> filtered = new ArrayList<>();
        for (AppointmentItem appt : fullList) {
            String status = appt.getStatus() != null ? appt.getStatus().toUpperCase() : "";
            boolean matchesFilter = true;

            if ("PENDING".equals(currentFilter)) {
                matchesFilter = status.contains("PENDING") || status.contains("NEW");
            } else if ("IN PROGRESS".equals(currentFilter)) {
                matchesFilter = status.contains("PROGRESS") || status.contains("ACTIVE") || status.contains("ASSIGNED") || status.contains("CONFIRMED");
            } else if ("COMPLETED".equals(currentFilter)) {
                matchesFilter = status.contains("COMPLETED") || status.contains("DONE");
            }

            if (matchesFilter) {
                if (!searchQuery.isEmpty()) {
                    String name = appt.getCustomerName() != null ? appt.getCustomerName().toLowerCase() : "";
                    String device = appt.getDeviceModel() != null ? appt.getDeviceModel().toLowerCase() : "";
                    String code = appt.getAppointmentCode() != null ? appt.getAppointmentCode().toLowerCase() : "";
                    if (name.contains(searchQuery) || device.contains(searchQuery) || code.contains(searchQuery)) {
                        filtered.add(appt);
                    }
                } else {
                    filtered.add(appt);
                }
            }
        }

        adapter.setAppointmentItems(filtered);
        if (binding.tvSubtitle != null) {
            binding.tvSubtitle.setText(filtered.size() + " Appointments Found");
        }
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
