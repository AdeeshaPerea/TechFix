package com.example.techfix.customer;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";
    public static final String COLLECTION_USERS = "users";

    private final FirebaseFirestore firestore;

    public interface CloudSyncCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public FirebaseHelper() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveUserToCloud(CustomerUser user, CloudSyncCallback callback) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            if (callback != null) callback.onFailure("Invalid user data for cloud sync");
            return;
        }

        String docId = user.getEmail().trim().toLowerCase();

        firestore.collection(COLLECTION_USERS)
                .document(docId)
                .set(user.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User synced to Firebase Firestore successfully: " + docId);
                    if (callback != null) {
                        callback.onSuccess("Synced with Cloud Database");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync user to Firebase Firestore: " + e.getMessage(), e);
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    public void syncAllFromCloud(DatabaseHelper dbHelper, CloudSyncCallback callback) {
        firestore.collection(COLLECTION_USERS)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int count = 0;
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            CustomerUser cloudUser = documentToUser(doc);
                            if (cloudUser.getEmail() != null && !cloudUser.getEmail().isEmpty()) {
                                dbHelper.upsertUser(cloudUser);
                                count++;
                            }
                        }
                    }
                    if (callback != null) {
                        callback.onSuccess("Synced " + count + " users from cloud");
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    public void pushAllToCloud(DatabaseHelper dbHelper, CloudSyncCallback callback) {
        List<CustomerUser> localUsers = dbHelper.getAllUsers();
        if (localUsers.isEmpty()) {
            if (callback != null) callback.onSuccess("No local users to push");
            return;
        }

        final int[] successCount = {0};
        final int[] failCount = {0};
        final int total = localUsers.size();

        for (CustomerUser user : localUsers) {
            String docId = user.getEmail().trim().toLowerCase();
            firestore.collection(COLLECTION_USERS)
                    .document(docId)
                    .set(user.toMap(), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        successCount[0]++;
                        checkPushComplete(successCount[0], failCount[0], total, callback);
                    })
                    .addOnFailureListener(e -> {
                        failCount[0]++;
                        checkPushComplete(successCount[0], failCount[0], total, callback);
                    });
        }
    }

    private CustomerUser documentToUser(DocumentSnapshot doc) {
        String userId = doc.getString("userId");
        String fullName = doc.getString("fullName");
        String email = doc.getString("email");
        String phone = doc.getString("phone");
        String password = doc.getString("password");
        String userType = doc.getString("userType");
        Long createdAt = doc.getLong("createdAt");
        Long lastModified = doc.getLong("lastModified");

        CustomerUser user = new CustomerUser();
        user.setUserId(userId != null ? userId : java.util.UUID.randomUUID().toString());
        user.setFullName(fullName != null ? fullName : "");
        user.setEmail(email != null ? email : doc.getId());
        user.setPhone(phone != null ? phone : "");
        user.setPassword(password != null ? password : "");
        user.setUserType(userType != null ? userType : CustomerUser.TYPE_CUSTOMER);
        user.setCreatedAt(createdAt != null ? createdAt : 0);
        user.setLastModified(lastModified != null ? lastModified : (createdAt != null ? createdAt : 0));
        return user;
    }

    private void checkPushComplete(int success, int fail, int total, CloudSyncCallback callback) {
        if (success + fail >= total) {
            if (callback != null) {
                if (fail == 0) {
                    callback.onSuccess("Pushed " + success + " users to cloud");
                } else {
                    callback.onFailure("Pushed " + success + "/" + total + " users.");
                }
            }
        }
    }
}
