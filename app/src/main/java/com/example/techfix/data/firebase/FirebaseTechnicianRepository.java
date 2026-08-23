package com.example.techfix.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseTechnicianRepository {

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebaseTechnicianRepository instance;
    private FirebaseFirestore firestore;
    private final MutableLiveData<List<User>> techniciansLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseTechnicianRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            techniciansLiveData.setValue(MockDataGenerator.getMockTechnicians());
        }
    }

    public static synchronized FirebaseTechnicianRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseTechnicianRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_TECHNICIANS)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) {
                        if (techniciansLiveData.getValue() == null || techniciansLiveData.getValue().isEmpty()) {
                            techniciansLiveData.setValue(MockDataGenerator.getMockTechnicians());
                        }
                        return;
                    }

                    List<User> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setId(doc.getId());
                            list.add(user);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockTechnicians();
                        seedInitialTechnicians(list);
                    }
                    techniciansLiveData.setValue(list);
                });
    }

    private void seedInitialTechnicians(List<User> initialList) {
        if (firestore == null) return;
        for (User user : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_TECHNICIANS)
                    .document(user.getId())
                    .set(user);
        }
    }

    public LiveData<List<User>> getTechniciansLiveData() {
        return techniciansLiveData;
    }

    public void addTechnician(User technician, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_TECHNICIANS)
                    .document(technician.getId())
                    .set(technician)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<User> current = techniciansLiveData.getValue();
            if (current != null) {
                current.add(technician);
                techniciansLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }

    public void updateTechnician(User technician, Callback callback) {
        addTechnician(technician, callback);
    }

    public void deleteTechnician(String id, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_TECHNICIANS)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<User> current = techniciansLiveData.getValue();
            if (current != null) {
                current.removeIf(u -> u.getId().equals(id));
                techniciansLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }
}
