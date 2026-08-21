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

import com.example.techfix.databinding.FragmentAdminGalleryBinding;
import com.example.techfix.model.GalleryItem;
import com.example.techfix.ui.common.GalleryAdapter;

public class AdminGalleryFragment extends Fragment {

    private FragmentAdminGalleryBinding binding;
    private AdminViewModel viewModel;
    private GalleryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new GalleryAdapter();
        binding.rvGalleryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGalleryList.setAdapter(adapter);

        viewModel.getGalleryItems().observe(getViewLifecycleOwner(), galleryItems -> {
            if (galleryItems != null) {
                adapter.setGalleryItems(galleryItems);
            }
        });

        binding.btnAddGalleryItem.setOnClickListener(v -> {
            GalleryItem newItem = new GalleryItem(
                    "GAL_004",
                    "ROG Strix Thermal Overhaul",
                    "Laptop",
                    "Liquid metal thermal compound replacement.",
                    "Overheating 95C",
                    "Cooling 65C Peak",
                    "2026-08-21"
            );
            viewModel.addGalleryItem(newItem);
            Toast.makeText(requireContext(), "Added ROG Strix Portfolio Item!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
