package com.example.techfix.customer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.R;
import com.example.techfix.data.firebase.FirebasePhotoRepository;
import com.example.techfix.data.firebase.FirestoreConstants;
import com.example.techfix.databinding.ActivityCustomerRepairPhotosBinding;
import com.example.techfix.model.RepairPhotoItem;
import com.example.techfix.ui.common.PhotoPreviewDialog;
import com.example.techfix.ui.common.RepairPhotoAdapter;
import com.example.techfix.utils.ImagePickerHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepairPhotosActivity extends AppCompatActivity {

    private ActivityCustomerRepairPhotosBinding binding;
    private String repairId;

    private RepairPhotoAdapter beforeAdapter;
    private RepairPhotoAdapter afterAdapter;

    private Uri pendingCameraUri;

    // Activity Result Launchers
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> getContentLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCustomerRepairPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootCustomerPhotos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null && getIntent().hasExtra("REPAIR_ID")) {
            repairId = getIntent().getStringExtra("REPAIR_ID");
        } else {
            repairId = "REP_001";
        }

        binding.txtRepairId.setText("#" + repairId);
        binding.btnBack.setOnClickListener(v -> finish());

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
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        });

        setupAdapters();
        observePhotos();

        binding.btnTakePhotoBefore.setOnClickListener(v -> checkCameraPermissionAndLaunch());
        binding.btnChooseGalleryBefore.setOnClickListener(v -> getContentLauncher.launch("image/*"));
    }

    private void setupAdapters() {
        beforeAdapter = new RepairPhotoAdapter(true, new RepairPhotoAdapter.OnPhotoActionListener() {
            @Override
            public void onPhotoClick(RepairPhotoItem item) {
                PhotoPreviewDialog.show(CustomerRepairPhotosActivity.this, item);
            }

            @Override
            public void onRemoveClick(RepairPhotoItem item) {
                FirebasePhotoRepository.getInstance().deletePhoto(item, new FirebasePhotoRepository.RepositoryCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(CustomerRepairPhotosActivity.this, "Photo removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(CustomerRepairPhotosActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        afterAdapter = new RepairPhotoAdapter(false, new RepairPhotoAdapter.OnPhotoActionListener() {
            @Override
            public void onPhotoClick(RepairPhotoItem item) {
                PhotoPreviewDialog.show(CustomerRepairPhotosActivity.this, item);
            }

            @Override
            public void onRemoveClick(RepairPhotoItem item) {}
        });

        binding.rvBeforePhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvBeforePhotos.setAdapter(beforeAdapter);

        binding.rvAfterPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvAfterPhotos.setAdapter(afterAdapter);
    }

    private void observePhotos() {
        FirebasePhotoRepository.getInstance().getPhotosForRepairLiveData(repairId).observe(this, photos -> {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        pendingCameraUri = ImagePickerHelper.createTempCameraUri(this);
        if (pendingCameraUri != null) {
            takePictureLauncher.launch(pendingCameraUri);
        } else {
            Toast.makeText(this, "Could not create image file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotoToFirebase(Uri imageUri) {
        binding.layoutUploadingProgress.setVisibility(View.VISIBLE);
        binding.btnTakePhotoBefore.setEnabled(false);
        binding.btnChooseGalleryBefore.setEnabled(false);

        FirebasePhotoRepository.getInstance().uploadRepairPhoto(
                this,
                repairId,
                imageUri,
                FirestoreConstants.PHOTO_TYPE_BEFORE,
                FirestoreConstants.ROLE_CUSTOMER,
                new FirebasePhotoRepository.PhotoUploadCallback() {
                    @Override
                    public void onSuccess(RepairPhotoItem photoItem) {
                        binding.layoutUploadingProgress.setVisibility(View.GONE);
                        binding.btnTakePhotoBefore.setEnabled(true);
                        binding.btnChooseGalleryBefore.setEnabled(true);
                        Toast.makeText(CustomerRepairPhotosActivity.this, "Photo uploaded successfully.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        binding.layoutUploadingProgress.setVisibility(View.GONE);
                        binding.btnTakePhotoBefore.setEnabled(true);
                        binding.btnChooseGalleryBefore.setEnabled(true);
                        Toast.makeText(CustomerRepairPhotosActivity.this, "Photo upload failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
