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

import com.example.techfix.databinding.FragmentAdminBranchesBinding;
import com.example.techfix.model.BranchItem;
import com.example.techfix.ui.common.BranchAdapter;

public class AdminBranchesFragment extends Fragment {

    private FragmentAdminBranchesBinding binding;
    private AdminViewModel viewModel;
    private BranchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBranchesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new BranchAdapter();
        binding.rvBranchesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBranchesList.setAdapter(adapter);

        viewModel.getBranches().observe(getViewLifecycleOwner(), branches -> {
            if (branches != null) {
                adapter.setBranches(branches);
            }
        });

        binding.btnAddBranch.setOnClickListener(v -> {
            BranchItem newBranch = new BranchItem(
                    "BRANCH_03",
                    "TechFix Kandy",
                    "45 Peradeniya Road, Kandy",
                    "+94 81 222 3344",
                    "08:30 AM - 06:00 PM",
                    7.2906,
                    80.6337,
                    4,
                    3
            );
            viewModel.addBranch(newBranch);
            Toast.makeText(requireContext(), "Added TechFix Kandy Branch!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
