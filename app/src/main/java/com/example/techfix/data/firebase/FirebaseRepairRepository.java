package com.example.techfix.data.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.RepairNoteItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepairRepository {

    public interface RepositoryCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface ImageUploadCallback {
        void onSuccess(String downloadUrl);
        void onFailure(String error);
    }

    private static FirebaseRepairRepository instance;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private final MutableLiveData<List<RepairItem>> repairsLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseRepairRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            storage = null;
            repairsLiveData.setValue(MockDataGenerator.getMockRepairs());
        }
    }

    public static synchronized FirebaseRepairRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepairRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_REPAIRS)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null || queryDocumentSnapshots == null) {
                        Log.e("FirebaseRepairRepo", "Firestore error: " + (e != null ? e.getMessage() : "null"));
                        if (repairsLiveData.getValue() == null || repairsLiveData.getValue().isEmpty()) {
                            repairsLiveData.setValue(MockDataGenerator.getMockRepairs());
                        }
                        return;
                    }

                    List<RepairItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        RepairItem item = doc.toObject(RepairItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            list.add(item);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockRepairs();
                        seedInitialRepairs(list);
                    } else {
                        // Sort in-memory by ID or status
                        list.sort((a, b) -> b.getId().compareTo(a.getId()));
                    }
                    repairsLiveData.setValue(list);
                });
    }

    private void seedInitialRepairs(List<RepairItem> initialList) {
        if (firestore == null) return;
        for (RepairItem item : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIRS)
                    .document(item.getId())
                    .set(item);
        }
    }

    public LiveData<List<RepairItem>> getRepairsLiveData() {
        return repairsLiveData;
    }

    public RepairItem getRepairById(String id) {
        List<RepairItem> current = repairsLiveData.getValue();
        if (current != null) {
            for (RepairItem item : current) {
                if (item.getId().equals(id)) return item;
            }
        }
        return null;
    }

    public void updateStatus(String repairId, String newStatus, RepositoryCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", newStatus);
        updates.put("updatedAt", System.currentTimeMillis());

        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIRS)
                    .document(repairId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        updateLocalStatus(repairId, newStatus);
                        if (callback != null) callback.onSuccess();
                    });
        } else {
            updateLocalStatus(repairId, newStatus);
            if (callback != null) callback.onSuccess();
        }
    }

    private void updateLocalStatus(String repairId, String newStatus) {
        List<RepairItem> current = repairsLiveData.getValue();
        if (current != null) {
            for (RepairItem item : current) {
                if (item.getId().equals(repairId)) {
                    item.setStatus(newStatus);
                    break;
                }
            }
            repairsLiveData.setValue(current);
        }
    }

    public void saveDiagnosis(String repairId, String diagnosisSummary, String problemFound, String recommendedFix, int estDurationHours, RepositoryCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("diagnosisSummary", diagnosisSummary);
        updates.put("problemFound", problemFound);
        updates.put("recommendedFix", recommendedFix);
        updates.put("estimatedDurationHours", estDurationHours);
        updates.put("updatedAt", System.currentTimeMillis());

        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIRS)
                    .document(repairId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            if (callback != null) callback.onSuccess();
        }
    }

    public void addRepairNote(String repairId, String techName, String status, String noteText, RepositoryCallback callback) {
        String noteId = "NOTE_" + System.currentTimeMillis();
        RepairNoteItem note = new RepairNoteItem(noteId, repairId, techName, status, noteText, "Just now");

        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_REPAIRS)
                    .document(repairId)
                    .collection(FirestoreConstants.COLLECTION_REPAIR_NOTES)
                    .document(noteId)
                    .set(note)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            if (callback != null) callback.onSuccess();
        }
    }

    public void addSparePartUsed(String repairId, String partId, String partName, int quantity, double unitPrice, RepositoryCallback callback) {
        if (firestore != null) {
            firestore.runTransaction(transaction -> {
                // Read stock in spareParts collection
                var partRef = firestore.collection(FirestoreConstants.COLLECTION_SPARE_PARTS).document(partId);
                DocumentSnapshot partDoc = transaction.get(partRef);

                long currentQty = partDoc.exists() && partDoc.contains("quantity") ? partDoc.getLong("quantity") : 10;
                if (currentQty < quantity) {
                    throw new RuntimeException("Insufficient stock available (" + currentQty + " left)");
                }

                transaction.update(partRef, "quantity", currentQty - quantity);

                // Add record to repairParts collection
                var repairPartRef = firestore.collection(FirestoreConstants.COLLECTION_REPAIR_PARTS).document();
                Map<String, Object> repairPart = new HashMap<>();
                repairPart.put("repairId", repairId);
                repairPart.put("partId", partId);
                repairPart.put("partName", partName);
                repairPart.put("quantity", quantity);
                repairPart.put("unitPrice", unitPrice);
                repairPart.put("totalPrice", unitPrice * quantity);
                repairPart.put("createdAt", System.currentTimeMillis());

                transaction.set(repairPartRef, repairPart);
                return null;
            }).addOnSuccessListener(aVoid -> {
                if (callback != null) callback.onSuccess();
            }).addOnFailureListener(e -> {
                if (callback != null) callback.onFailure(e.getLocalizedMessage());
            });
        } else {
            if (callback != null) callback.onSuccess();
        }
    }

    public void uploadRepairImage(String repairId, String type, Uri imageUri, ImageUploadCallback callback) {
        if (storage != null && imageUri != null) {
            StorageReference ref = storage.getReference().child("repair-images/" + repairId + "/" + type + "/" + System.currentTimeMillis() + ".jpg");
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        saveImageRecordToFirestore(repairId, type, downloadUrl);
                        if (callback != null) callback.onSuccess(downloadUrl);
                    }))
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            if (callback != null) callback.onSuccess("https://via.placeholder.com/600x400.png?text=" + type + "+Image");
        }
    }

    private void saveImageRecordToFirestore(String repairId, String type, String url) {
        if (firestore == null) return;
        Map<String, Object> imgRecord = new HashMap<>();
        imgRecord.put("repairId", repairId);
        imgRecord.put("type", type);
        imgRecord.put("imageUrl", url);
        imgRecord.put("createdAt", System.currentTimeMillis());

        firestore.collection(FirestoreConstants.COLLECTION_GALLERY).add(imgRecord);
    }
}
