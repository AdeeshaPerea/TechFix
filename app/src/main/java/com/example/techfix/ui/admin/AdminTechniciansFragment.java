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

import com.example.techfix.databinding.FragmentAdminTechniciansBinding;
import com.example.techfix.model.User;
import com.example.techfix.ui.common.TechnicianAdapter;

public class AdminTechniciansFragment extends Fragment {

    private FragmentAdminTechniciansBinding binding;
    private AdminViewModel viewModel;
    private TechnicianAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminTechniciansBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new TechnicianAdapter(user -> {
            Toast.makeText(requireContext(), "Editing " + user.getName(), Toast.LENGTH_SHORT).show();
        });

        binding.rvTechniciansList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTechniciansList.setAdapter(adapter);

        viewModel.getTechnicians().observe(getViewLifecycleOwner(), technicians -> {
            if (technicians != null) {
                adapter.setTechnicians(technicians);
            }
        });

        binding.btnAddTechnician.setOnClickListener(v -> {
            User newTech = new User(
                    "TECH_005",
                    "Nalin Jayawardena",
                    "nalin@techfix.com",
                    "+94 71 555 6677",
                    "TECH",
                    "Display & Micro-soldering Expert",
                    "BRANCH_01",
                    "TechFix Colombo",
                    "08:30 AM - 05:30 PM",
                    1
            );
            viewModel.addTechnician(newTech);
            Toast.makeText(requireContext(), "Added Technician Nalin Jayawardena!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
