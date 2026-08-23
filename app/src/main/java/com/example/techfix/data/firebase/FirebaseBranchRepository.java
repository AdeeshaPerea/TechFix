package com.example.techfix.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.model.BranchItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseBranchRepository {

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    private static FirebaseBranchRepository instance;
    private FirebaseFirestore firestore;
    private final MutableLiveData<List<BranchItem>> branchesLiveData = new MutableLiveData<>(new ArrayList<>());

    private FirebaseBranchRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
            attachRealtimeListener();
        } catch (Exception e) {
            firestore = null;
            branchesLiveData.setValue(MockDataGenerator.getMockBranches());
        }
    }

    public static synchronized FirebaseBranchRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseBranchRepository();
        }
        return instance;
    }

    private void attachRealtimeListener() {
        if (firestore == null) return;

        firestore.collection(FirestoreConstants.COLLECTION_BRANCHES)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) {
                        if (branchesLiveData.getValue() == null || branchesLiveData.getValue().isEmpty()) {
                            branchesLiveData.setValue(MockDataGenerator.getMockBranches());
                        }
                        return;
                    }

                    List<BranchItem> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        BranchItem item = doc.toObject(BranchItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            list.add(item);
                        }
                    }

                    if (list.isEmpty()) {
                        list = MockDataGenerator.getMockBranches();
                        seedInitialBranches(list);
                    }
                    branchesLiveData.setValue(list);
                });
    }

    private void seedInitialBranches(List<BranchItem> initialList) {
        if (firestore == null) return;
        for (BranchItem branch : initialList) {
            firestore.collection(FirestoreConstants.COLLECTION_BRANCHES)
                    .document(branch.getId())
                    .set(branch);
        }
    }

    public LiveData<List<BranchItem>> getBranchesLiveData() {
        return branchesLiveData;
    }

    public void addBranch(BranchItem branch, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_BRANCHES)
                    .document(branch.getId())
                    .set(branch)
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<BranchItem> current = branchesLiveData.getValue();
            if (current != null) {
                current.add(branch);
                branchesLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }

    public void updateBranch(BranchItem branch, Callback callback) {
        addBranch(branch, callback);
    }

    public void deleteBranch(String id, Callback callback) {
        if (firestore != null) {
            firestore.collection(FirestoreConstants.COLLECTION_BRANCHES)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e.getLocalizedMessage());
                    });
        } else {
            List<BranchItem> current = branchesLiveData.getValue();
            if (current != null) {
                current.removeIf(b -> b.getId().equals(id));
                branchesLiveData.setValue(current);
            }
            if (callback != null) callback.onSuccess();
        }
    }
}
