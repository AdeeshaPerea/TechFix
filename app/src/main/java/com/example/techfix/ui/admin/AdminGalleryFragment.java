package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.data.firebase.FirebasePhotoRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.FragmentAdminGalleryBinding;
import com.example.techfix.model.RepairPhotoItem;
import com.example.techfix.ui.common.PhotoPreviewDialog;
import com.example.techfix.ui.common.RepairPhotoAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminGalleryFragment extends Fragment {

    private FragmentAdminGalleryBinding binding;
    private String repairId;

    private RepairPhotoAdapter beforeAdapter;
    private RepairPhotoAdapter afterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            repairId = getArguments().getString("repairId", "REP_001");
        } else {
            repairId = "REP_001";
        }

        if (binding.txtRepairId != null) {
            binding.txtRepairId.setText("Repair #" + repairId + " • Photo Audit View");
        }

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        setupAdapters();
        observePhotos();
    }

    private void setupAdapters() {
        beforeAdapter = new RepairPhotoAdapter(false, new RepairPhotoAdapter.OnPhotoActionListener() {
            @Override
            public void onPhotoClick(RepairPhotoItem item) {
                PhotoPreviewDialog.show(requireContext(), item);
            }

            @Override
            public void onRemoveClick(RepairPhotoItem item) {}
        });

        afterAdapter = new RepairPhotoAdapter(false, new RepairPhotoAdapter.OnPhotoActionListener() {
            @Override
            public void onPhotoClick(RepairPhotoItem item) {
                PhotoPreviewDialog.show(requireContext(), item);
            }

            @Override
            public void onRemoveClick(RepairPhotoItem item) {}
        });

        binding.rvBeforePhotos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvBeforePhotos.setAdapter(beforeAdapter);

        binding.rvAfterPhotos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvAfterPhotos.setAdapter(afterAdapter);
    }

    private void observePhotos() {
        FirebasePhotoRepository.getInstance().getPhotosForRepairLiveData(repairId).observe(getViewLifecycleOwner(), photos -> {
            List<RepairPhotoItem> beforeList = new ArrayList<>();
            List<RepairPhotoItem> afterList = new ArrayList<>();

            if (photos != null) {
                for (RepairPhotoItem photo : photos) {
                    if (FirestoreConstants.PHOTO_TYPE_BEFORE.equalsIgnoreCase(photo.getPhotoType())) {
                        beforeList.add(photo);
                    } else {
                        afterList.add(photo);
                    }
                }
            }

            beforeAdapter.setPhotoList(beforeList);
            afterAdapter.setPhotoList(afterList);

            binding.tvEmptyBefore.setVisibility(beforeList.isEmpty() ? View.VISIBLE : View.GONE);
            binding.tvEmptyAfter.setVisibility(afterList.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
