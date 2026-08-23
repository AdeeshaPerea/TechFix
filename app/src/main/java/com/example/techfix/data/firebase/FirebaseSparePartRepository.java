package com.example.techfix.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.SparePartItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseSparePartRepository {

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebaseSparePartRepository instance;
    private FirebaseFirestore firestore;
    private final MutableLiveData<List<SparePartItem>> sparePartsLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseSparePartRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            sparePartsLiveData.setValue(MockDataGenerator.getMockSpareParts());
        }
    }

    public static synchronized FirebaseSparePartRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseSparePartRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_SPARE_PARTS)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) {
                        if (sparePartsLiveData.getValue() == null || sparePartsLiveData.getValue().isEmpty()) {
                            sparePartsLiveData.setValue(MockDataGenerator.getMockSpareParts());
                        }
                        return;
                    }

                    List<SparePartItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        SparePartItem item = doc.toObject(SparePartItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            list.add(item);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockSpareParts();
                        seedInitialSpareParts(list);
                    }
                    sparePartsLiveData.setValue(list);
                });
    }

    private void seedInitialSpareParts(List<SparePartItem> initialList) {
        if (firestore == null) return;
        for (SparePartItem item : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_SPARE_PARTS)
                    .document(item.getId())
                    .set(item);
        }
    }

    public LiveData<List<SparePartItem>> getSparePartsLiveData() {
        return sparePartsLiveData;
    }

    public void addOrUpdatePart(SparePartItem part, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_SPARE_PARTS)
                    .document(part.getId())
                    .set(part)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<SparePartItem> current = sparePartsLiveData.getValue();
            if (current != null) {
                boolean found = false;
                for (int i = 0; i < current.size(); i++) {
                    if (current.get(i).getId().equals(part.getId())) {
                        current.set(i, part);
                        found = true;
                        break;
                    }
                }
                if (!found) current.add(part);
                sparePartsLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }

    public void deleteSparePart(String id, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_SPARE_PARTS)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<SparePartItem> current = sparePartsLiveData.getValue();
            if (current != null) {
                current.removeIf(p -> p.getId().equals(id));
                sparePartsLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }
}
