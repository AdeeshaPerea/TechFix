package com.example.techfix.ui.tech;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.data.firebase.FirebasePhotoRepository;
import com.example.techfix.data.firebase.FirebaseRepairRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.FragmentTechRepairGalleryBinding;
import com.example.techfix.model.RepairPhotoItem;
import com.example.techfix.ui.common.PhotoPreviewDialog;
import com.example.techfix.ui.common.RepairPhotoAdapter;
import com.example.techfix.utils.ImagePickerHelper;

import java.util.ArrayList;
import java.util.List;

public class TechRepairGalleryFragment extends Fragment {

    private FragmentTechRepairGalleryBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    private RepairPhotoAdapter beforeAdapter;
    private RepairPhotoAdapter afterAdapter;

    private Uri pendingCameraUri;

    // Activity Result Launchers
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> getContentLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Camera Contract
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success && pendingCameraUri != null) {
                uploadPhotoToFirebase(pendingCameraUri);
            }
        });

        // Gallery Contract
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadPhotoToFirebase(uri);
            }
        });

        // Permission Contract
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                launchCamera();
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TechViewModel.class);

        if (getArguments() != null) {
            repairId = getArguments().getString("repairId", "REP_001");
        } else {
            repairId = "REP_001";
        }

        if (binding.txtRepairId != null) {
            binding.txtRepairId.setText("#" + repairId);
        }

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        setupAdapters();
        observePhotos();

        if (binding.btnTakePhotoAfter != null) {
            binding.btnTakePhotoAfter.setOnClickListener(v -> checkCameraPermissionAndLaunch());
        }

        if (binding.btnChooseGalleryAfter != null) {
            binding.btnChooseGalleryAfter.setOnClickListener(v -> getContentLauncher.launch("image/*"));
        }

        if (binding.btnMarkCompleted != null) {
            binding.btnMarkCompleted.setOnClickListener(v -> {
                FirebaseRepairRepository.getInstance().updateStatus(repairId, FirestoreConstants.STATUS_COMPLETED, new FirebaseRepairRepository.RepositoryCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Repair marked as COMPLETED!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(requireContext(), "Failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
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

        afterAdapter = new RepairPhotoAdapter(true, new RepairPhotoAdapter.OnPhotoActionListener() {
            @Override
            public void onPhotoClick(RepairPhotoItem item) {
                PhotoPreviewDialog.show(requireContext(), item);
            }

            @Override
            public void onRemoveClick(RepairPhotoItem item) {
                FirebasePhotoRepository.getInstance().deletePhoto(item, new FirebasePhotoRepository.RepositoryCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Photo removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(requireContext(), "Failed to delete: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        pendingCameraUri = ImagePickerHelper.createTempCameraUri(requireContext());
        if (pendingCameraUri != null) {
            takePictureLauncher.launch(pendingCameraUri);
        } else {
            Toast.makeText(requireContext(), "Could not create image file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotoToFirebase(Uri imageUri) {
        if (binding.layoutUploadingProgress != null) {
            binding.layoutUploadingProgress.setVisibility(View.VISIBLE);
        }
        if (binding.btnTakePhotoAfter != null) binding.btnTakePhotoAfter.setEnabled(false);
        if (binding.btnChooseGalleryAfter != null) binding.btnChooseGalleryAfter.setEnabled(false);

        FirebasePhotoRepository.getInstance().uploadRepairPhoto(
                requireContext(),
                repairId,
                imageUri,
                FirestoreConstants.PHOTO_TYPE_AFTER,
                FirestoreConstants.ROLE_TECHNICIAN,
                new FirebasePhotoRepository.PhotoUploadCallback() {
                    @Override
                    public void onSuccess(RepairPhotoItem photoItem) {
                        if (isAdded()) {
                            if (binding.layoutUploadingProgress != null) {
                                binding.layoutUploadingProgress.setVisibility(View.GONE);
                            }
                            if (binding.btnTakePhotoAfter != null) binding.btnTakePhotoAfter.setEnabled(true);
                            if (binding.btnChooseGalleryAfter != null) binding.btnChooseGalleryAfter.setEnabled(true);
                            Toast.makeText(requireContext(), "Photo uploaded successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        if (isAdded()) {
                            if (binding.layoutUploadingProgress != null) {
                                binding.layoutUploadingProgress.setVisibility(View.GONE);
                            }
                            if (binding.btnTakePhotoAfter != null) binding.btnTakePhotoAfter.setEnabled(true);
                            if (binding.btnChooseGalleryAfter != null) binding.btnChooseGalleryAfter.setEnabled(true);
                            Toast.makeText(requireContext(), "Photo upload failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
