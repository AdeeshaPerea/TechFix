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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminServicesBinding;
import com.example.techfix.model.RepairServiceItem;
import com.example.techfix.ui.common.FormatUtils;
import com.example.techfix.ui.common.ServiceAdapter;

public class AdminServicesFragment extends Fragment {

    private FragmentAdminServicesBinding binding;
    private AdminViewModel viewModel;
    private ServiceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminServicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new ServiceAdapter(service -> {
            service.setPriceLkr(service.getPriceLkr() + 1000);
            viewModel.updateService(service);
            Toast.makeText(requireContext(), "Updated Price for " + service.getTitle() + " to " + FormatUtils.formatCurrency(service.getPriceLkr()), Toast.LENGTH_SHORT).show();
        });

        binding.rvServicesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvServicesList.setAdapter(adapter);

        viewModel.getServices().observe(getViewLifecycleOwner(), repairServiceItems -> {
            if (repairServiceItems != null) {
                adapter.setServices(repairServiceItems);
            }
        });

        binding.btnAddService.setOnClickListener(v -> {
            RepairServiceItem newService = new RepairServiceItem(
                    "SERV_006",
                    "Water Damage Ultrasonic Cleaning",
                    "Mobile/Laptop",
                    15000.0,
                    "3 Hours",
                    "De-oxidation board treatment in isopropyl bath."
            );
            viewModel.addService(newService);
            Toast.makeText(requireContext(), "Added Water Damage Service!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
