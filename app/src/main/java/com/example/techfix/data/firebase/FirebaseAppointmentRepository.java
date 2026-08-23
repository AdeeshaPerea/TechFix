package com.example.techfix.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.AppointmentItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAppointmentRepository {

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebaseAppointmentRepository instance;
    private FirebaseFirestore firestore;
    private final MutableLiveData<List<AppointmentItem>> appointmentsLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseAppointmentRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            appointmentsLiveData.setValue(MockDataGenerator.getMockAppointments());
        }
    }

    public static synchronized FirebaseAppointmentRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseAppointmentRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_APPOINTMENTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) {
                        if (appointmentsLiveData.getValue() == null || appointmentsLiveData.getValue().isEmpty()) {
                            appointmentsLiveData.setValue(MockDataGenerator.getMockAppointments());
                        }
                        return;
                    }

                    List<AppointmentItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        AppointmentItem item = doc.toObject(AppointmentItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            list.add(item);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockAppointments();
                        seedInitialAppointments(list);
                    }
                    appointmentsLiveData.setValue(list);
                });
    }

    private void seedInitialAppointments(List<AppointmentItem> initialList) {
        if (firestore == null) return;
        for (AppointmentItem item : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_APPOINTMENTS)
                    .document(item.getId())
                    .set(item);
        }
    }

    public LiveData<List<AppointmentItem>> getAppointmentsLiveData() {
        return appointmentsLiveData;
    }

    public AppointmentItem getAppointmentById(String id) {
        List<AppointmentItem> list = appointmentsLiveData.getValue();
        if (list != null) {
            for (AppointmentItem appt : list) {
                if (appt.getId().equals(id)) return appt;
            }
        }
        return null;
    }

    public void updateStatus(String appointmentId, String newStatus, Callback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", newStatus);
        updates.put("updatedAt", System.currentTimeMillis());

        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_APPOINTMENTS)
                    .document(appointmentId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        updateLocalStatus(appointmentId, newStatus);
                        if (callback != null) callback.onSuccess();
                    });
        } else {
            updateLocalStatus(appointmentId, newStatus);
            if (callback != null) callback.onSuccess();
        }
    }

    private void updateLocalStatus(String id, String status) {
        List<AppointmentItem> list = appointmentsLiveData.getValue();
        if (list != null) {
            for (AppointmentItem appt : list) {
                if (appt.getId().equals(id)) {
                    appt.setStatus(status);
                    break;
                }
            }
            appointmentsLiveData.setValue(list);
        }
    }

    public void assignTechnician(String appointmentId, String techId, String techName, Callback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("assignedTechId", techId);
        updates.put("assignedTechName", techName);
        updates.put("updatedAt", System.currentTimeMillis());

        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_APPOINTMENTS)
                    .document(appointmentId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    });
        } else {
            if (callback != null) callback.onSuccess();
        }
    }
}
