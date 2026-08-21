package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.techfix.databinding.FragmentAdminAppointmentsBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.ui.common.AppointmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentsFragment extends Fragment {

    private FragmentAdminAppointmentsBinding binding;
    private AdminViewModel viewModel;
    private AppointmentAdapter adapter;

    private List<AppointmentItem> allAppointments = new ArrayList<>();
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

        adapter = new AppointmentAdapter(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId", item.getId());
                Navigation.findNavController(requireView()).navigate(R.id.action_adminAppointments_to_adminAppointmentDetail, bundle);
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

        binding.rvAppointmentsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAppointmentsList.setAdapter(adapter);

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                allAppointments = appointmentItems;
                filterAndRender();
            }
        });

        binding.etSearchAppointments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s != null ? s.toString().trim().toLowerCase() : "";
                filterAndRender();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterAndRender() {
        List<AppointmentItem> filtered = new ArrayList<>();
        for (AppointmentItem item : allAppointments) {
            boolean matchesSearch = searchQuery.isEmpty()
                    || item.getAppointmentCode().toLowerCase().contains(searchQuery)
                    || item.getCustomerName().toLowerCase().contains(searchQuery)
                    || item.getDeviceModel().toLowerCase().contains(searchQuery);

            if (matchesSearch) {
                filtered.add(item);
            }
        }
        adapter.setAppointmentItems(filtered);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
