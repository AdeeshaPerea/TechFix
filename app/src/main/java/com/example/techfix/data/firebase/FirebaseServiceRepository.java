package com.example.techfix.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.RepairServiceItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseServiceRepository {

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebaseServiceRepository instance;
    private FirebaseFirestore firestore;
    private final MutableLiveData<List<RepairServiceItem>> servicesLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseServiceRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            servicesLiveData.setValue(MockDataGenerator.getMockServices());
        }
    }

    public static synchronized FirebaseServiceRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseServiceRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_SERVICES)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) {
                        if (servicesLiveData.getValue() == null || servicesLiveData.getValue().isEmpty()) {
                            servicesLiveData.setValue(MockDataGenerator.getMockServices());
                        }
                        return;
                    }

                    List<RepairServiceItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        RepairServiceItem item = doc.toObject(RepairServiceItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            list.add(item);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockServices();
                        seedInitialServices(list);
                    }
                    servicesLiveData.setValue(list);
                });
    }

    private void seedInitialServices(List<RepairServiceItem> initialList) {
        if (firestore == null) return;
        for (RepairServiceItem service : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_SERVICES)
                    .document(service.getId())
                    .set(service);
        }
    }

    public LiveData<List<RepairServiceItem>> getServicesLiveData() {
        return servicesLiveData;
    }

    public void addOrUpdateService(RepairServiceItem service, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_SERVICES)
                    .document(service.getId())
                    .set(service)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<RepairServiceItem> current = servicesLiveData.getValue();
            if (current != null) {
                boolean found = false;
                for (int i = 0; i < current.size(); i++) {
                    if (current.get(i).getId().equals(service.getId())) {
                        current.set(i, service);
                        found = true;
                        break;
                    }
                }
                if (!found) current.add(service);
                servicesLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }

    public void deleteService(String id, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_SERVICES)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<RepairServiceItem> current = servicesLiveData.getValue();
            if (current != null) {
                current.removeIf(s -> s.getId().equals(id));
                servicesLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }
}
