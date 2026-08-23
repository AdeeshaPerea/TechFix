package com.example.techfix.data.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.RepairPhotoItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FirebasePhotoRepository {

    private static final String TAG = "FirebasePhotoRepo";

    public interface PhotoUploadCallback {
        void onSuccess(RepairPhotoItem photoItem);
        void onFailure(String errorMessage);
    }

    public interface RepositoryCallback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebasePhotoRepository instance;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    // In-memory cache for instant UI responsiveness across fragments/activities
    private final Map<String, List<RepairPhotoItem>> localPhotosCache = new HashMap<>();
    private final Map<String, MutableLiveData<List<RepairPhotoItem>>> liveDataMap = new HashMap<>();

    private FirebasePhotoRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
            firestore = null;
            storage = null;
        }
    }

    public static synchronized FirebasePhotoRepository getInstance() {
        if (instance == null) {
            instance = new FirebasePhotoRepository();
        }
        return instance;
    }

    /**
     * Reusable photo upload function with cloud storage & local fallback resilience.
     * Path: repairPhotos/{repairId}/{before|after}/{photoId}.jpg
     */
    public void uploadRepairPhoto(Context context, String repairId, Uri imageUri, String photoType, String userRole, PhotoUploadCallback callback) {
        if (repairId == null || repairId.trim().isEmpty()) {
            if (callback != null) callback.onFailure("Repair ID is missing.");
            return;
        }

        if (imageUri == null) {
            if (callback != null) callback.onFailure("Image source is invalid.");
            return;
        }

        String photoId = "photo_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 5);
        String subFolder = FirestoreConstants.PHOTO_TYPE_BEFORE.equalsIgnoreCase(photoType) ? "before" : "after";
        String storagePath = "repairPhotos/" + repairId + "/" + subFolder + "/" + photoId + ".jpg";

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uploaderUid = (currentUser != null && currentUser.getUid() != null) ? currentUser.getUid() : "user_guest";

        if (storage != null && firestore != null) {
            byte[] compressedData = compressImage(context, imageUri);
            if (compressedData == null || compressedData.length == 0) {
                fallbackSavePhoto(photoId, repairId, imageUri.toString(), storagePath, photoType, uploaderUid, userRole, callback);
                return;
            }

            StorageReference ref = storage.getReference().child(storagePath);

            ref.putBytes(compressedData)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        long uploadedAt = System.currentTimeMillis();

                        RepairPhotoItem item = new RepairPhotoItem(
                                photoId,
                                repairId,
                                downloadUrl,
                                storagePath,
                                photoType,
                                uploaderUid,
                                userRole,
                                uploadedAt
                        );

                        saveMetadataToFirestore(item, callback);
                    }).addOnFailureListener(e -> {
                        Log.w(TAG, "Download URL fetch failed: " + e.getMessage() + ". Using fallback URI.");
                        fallbackSavePhoto(photoId, repairId, imageUri.toString(), storagePath, photoType, uploaderUid, userRole, callback);
                    }))
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Storage upload failed: " + e.getMessage() + ". Using local URI fallback.");
                        fallbackSavePhoto(photoId, repairId, imageUri.toString(), storagePath, photoType, uploaderUid, userRole, callback);
                    });
        } else {
            fallbackSavePhoto(photoId, repairId, imageUri.toString(), storagePath, photoType, uploaderUid, userRole, callback);
        }
    }

    private void fallbackSavePhoto(String photoId, String repairId, String photoUrl, String storagePath, String photoType, String uploaderUid, String userRole, PhotoUploadCallback callback) {
        long uploadedAt = System.currentTimeMillis();
        RepairPhotoItem item = new RepairPhotoItem(
                photoId,
                repairId,
                photoUrl,
                storagePath,
                photoType,
                uploaderUid,
                userRole,
                uploadedAt
        );

        saveMetadataToFirestore(item, callback);
    }

    private void saveMetadataToFirestore(RepairPhotoItem item, PhotoUploadCallback callback) {
        // Always add to local memory cache first for immediate UI refresh
        addLocalPhoto(item);

        if (firestore == null) {
            if (callback != null) callback.onSuccess(item);
            return;
        }

        firestore.collection(FirestoreConstants.COLLECTION_REPAIR_PHOTOS)
                .document(item.getPhotoId())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess(item);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Firestore write failed: " + e.getMessage() + ". Photo cached locally.");
                    if (callback != null) callback.onSuccess(item);
                });
    }

    private void addLocalPhoto(RepairPhotoItem item) {
        if (item == null || item.getRepairId() == null) return;
        List<RepairPhotoItem> list = localPhotosCache.get(item.getRepairId());
        if (list == null) {
            list = new ArrayList<>();
            localPhotosCache.put(item.getRepairId(), list);
        }
        
        // Prevent duplicate photoId
        boolean exists = false;
        for (RepairPhotoItem existing : list) {
            if (existing.getPhotoId().equals(item.getPhotoId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            list.add(0, item); // Newest photo at top
        }

        MutableLiveData<List<RepairPhotoItem>> liveData = liveDataMap.get(item.getRepairId());
        if (liveData != null) {
            liveData.setValue(new ArrayList<>(list));
        }
    }

    /**
     * Observe repair photos in real-time by repairId.
     */
    public LiveData<List<RepairPhotoItem>> getPhotosForRepairLiveData(String repairId) {
        if (!liveDataMap.containsKey(repairId)) {
            liveDataMap.put(repairId, new MutableLiveData<>(new ArrayList<>()));
        }

        MutableLiveData<List<RepairPhotoItem>> liveData = liveDataMap.get(repairId);

        // Populate with initial local cache if available
        List<RepairPhotoItem> cached = localPhotosCache.get(repairId);
        if (cached != null && !cached.isEmpty()) {
            liveData.setValue(new ArrayList<>(cached));
        }

        if (firestore != null && repairId != null) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIR_PHOTOS)
                    .whereEqualTo("repairId", repairId)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null || queryDocumentSnapshots == null) {
                            return;
                        }

                        List<RepairPhotoItem> cloudList = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            RepairPhotoItem item = doc.toObject(RepairPhotoItem.class);
                            if (item != null) {
                                cloudList.add(item);
                            }
                        }

                        // Merge cloud list with local cache
                        List<RepairPhotoItem> currentCached = localPhotosCache.get(repairId);
                        if (currentCached != null) {
                            for (RepairPhotoItem localItem : currentCached) {
                                boolean found = false;
                                for (RepairPhotoItem cloudItem : cloudList) {
                                    if (cloudItem.getPhotoId().equals(localItem.getPhotoId())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    cloudList.add(localItem);
                                }
                            }
                        }

                        localPhotosCache.put(repairId, new ArrayList<>(cloudList));
                        liveData.setValue(cloudList);
                    });
        }

        return liveData;
    }

    /**
     * Delete photo from Storage, Firestore, and Local Cache.
     */
    public void deletePhoto(RepairPhotoItem item, RepositoryCallback callback) {
        if (item == null) return;

        // Remove from local cache
        List<RepairPhotoItem> cached = localPhotosCache.get(item.getRepairId());
        if (cached != null) {
            for (int i = 0; i < cached.size(); i++) {
                if (cached.get(i).getPhotoId().equals(item.getPhotoId())) {
                    cached.remove(i);
                    break;
                }
            }
            MutableLiveData<List<RepairPhotoItem>> liveData = liveDataMap.get(item.getRepairId());
            if (liveData != null) {
                liveData.setValue(new ArrayList<>(cached));
            }
        }

        if (storage != null && item.getStoragePath() != null && item.getStoragePath().startsWith("repairPhotos/")) {
            try {
                storage.getReference().child(item.getStoragePath()).delete();
            } catch (Exception ignored) {}
        }

        if (firestore != null && item.getPhotoId() != null) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIR_PHOTOS)
                    .document(item.getPhotoId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onSuccess(); // Graceful fallback
                    });
        } else {
            if (callback != null) callback.onSuccess();
        }
    }

    /**
     * Downscaling & image compression helper (Max 1920px width/height, 80% JPEG quality)
     */
    private byte[] compressImage(Context context, Uri uri) {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            int maxDim = Math.max(options.outWidth, options.outHeight);
            int sampleSize = 1;
            if (maxDim > 1920) {
                sampleSize = Math.round((float) maxDim / 1920f);
            }

            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inSampleSize = sampleSize;

            try (InputStream stream2 = context.getContentResolver().openInputStream(uri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, decodeOptions);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    return baos.toByteArray();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error compressing image: " + e.getMessage());
        }

        // Fallback: read raw bytes if compression fails
        try (InputStream rawStream = context.getContentResolver().openInputStream(uri);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = rawStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
