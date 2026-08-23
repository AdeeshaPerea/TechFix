package com.example.techfix.customer;

import android.util.Log;

import com.example.techfix.models.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";

    // Firestore Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_BRANCHES = "branches";
    public static final String COLLECTION_DEVICE_CATEGORIES = "device_categories";
    public static final String COLLECTION_REPAIR_SERVICES = "repair_services";
    public static final String COLLECTION_TECHNICIANS = "technicians";
    public static final String COLLECTION_APPOINTMENTS = "appointments";
    public static final String COLLECTION_DEVICE_IMAGES = "device_images";
    public static final String COLLECTION_SPARE_PARTS = "spare_parts";
    public static final String COLLECTION_PAYMENTS = "payments";
    public static final String COLLECTION_REPAIR_PARTS_USED = "repair_parts_used";

    private final FirebaseFirestore firestore;

    public interface CloudSyncCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface CloudFetchCallback {
        void onUserFetched(User user);
        void onUserNotFound();
        void onError(String errorMessage);
    }

    public FirebaseHelper() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Bi-directional sync: Sync users and entity records between SQLite and Cloud Firestore
     */
    public void performFullSync(DatabaseHelper dbHelper, CloudSyncCallback callback) {
        if (dbHelper == null) {
            if (callback != null) callback.onFailure("DatabaseHelper is null");
            return;
        }

        // Step 1: Pull users from Cloud to Local SQLite
        firestore.collection(COLLECTION_USERS)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int count = 0;
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            User cloudUser = documentToUser(doc);
                            if (cloudUser != null && cloudUser.getEmail() != null && !cloudUser.getEmail().isEmpty()) {
                                dbHelper.upsertUser(cloudUser);
                                count++;
                            }
                        }
                    }
                    final int pulledCount = count;

                    // Push local users from SQLite to Cloud
                    java.util.List<User> localUsers = dbHelper.getAllUsers();
                    if (localUsers != null && !localUsers.isEmpty()) {
                        for (User localUser : localUsers) {
                            saveUserToCloud(localUser, null);
                        }
                    }

                    // Step 2: Sync Appointments from Cloud Firestore to local SQLite
                    firestore.collection(COLLECTION_APPOINTMENTS)
                            .get()
                            .addOnSuccessListener(apptSnapshot -> {
                                int apptCount = 0;
                                if (apptSnapshot != null && !apptSnapshot.isEmpty()) {
                                    for (DocumentSnapshot doc : apptSnapshot.getDocuments()) {
                                        Appointment appt = documentToAppointment(doc);
                                        if (appt != null && appt.getAppointmentId() != null) {
                                            dbHelper.insertAppointment(appt);
                                            apptCount++;
                                        }
                                    }
                                }

                                if (callback != null) {
                                    callback.onSuccess("Full sync complete. Synced " + pulledCount + " users & " + apptCount + " appointments.");
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Appointment sync failed: " + e.getMessage());
                                if (callback != null) {
                                    callback.onSuccess("Users synced successfully.");
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Full sync failed: " + e.getMessage(), e);
                    if (callback != null) callback.onFailure(e.getMessage());
                });
    }

    // ==================== USERS ====================
    public void saveUserToCloud(User user, CloudSyncCallback callback) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            if (callback != null) callback.onFailure("Invalid user data for cloud sync");
            return;
        }

        String docId = user.getEmail().trim().toLowerCase();

        firestore.collection(COLLECTION_USERS)
                .document(docId)
                .set(user.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User synced to Cloud: " + docId);
                    if (callback != null) callback.onSuccess("Synced with Cloud Database");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync user to Cloud: " + e.getMessage(), e);
                    if (callback != null) callback.onFailure(e.getMessage());
                });
    }

    public void fetchUserFromCloud(String email, CloudFetchCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            if (callback != null) callback.onError("Invalid email");
            return;
        }

        String docId = email.trim().toLowerCase();

        firestore.collection(COLLECTION_USERS)
                .document(docId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentToUser(documentSnapshot);
                        if (callback != null) callback.onUserFetched(user);
                    } else {
                        if (callback != null) callback.onUserNotFound();
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
    }

    // ==================== BRANCHES ====================
    public void saveBranchToCloud(Branch branch, CloudSyncCallback callback) {
        if (branch == null || branch.getBranchId() == null) return;
        firestore.collection(COLLECTION_BRANCHES)
                .document(branch.getBranchId())
                .set(branch.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> { if (callback != null) callback.onSuccess("Branch synced"); })
                .addOnFailureListener(e -> { if (callback != null) callback.onFailure(e.getMessage()); });
    }

    // ==================== APPOINTMENTS ====================
    public void saveAppointmentToCloud(Appointment appointment, CloudSyncCallback callback) {
        if (appointment == null || appointment.getAppointmentId() == null) return;
        firestore.collection(COLLECTION_APPOINTMENTS)
                .document(appointment.getAppointmentId())
                .set(appointment.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment synced to Cloud: " + appointment.getAppointmentId());
                    if (callback != null) callback.onSuccess("Appointment synced to cloud");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync appointment: " + e.getMessage(), e);
                    if (callback != null) callback.onFailure(e.getMessage());
                });
    }

    // ==================== PAYMENTS ====================
    public void savePaymentToCloud(Payment payment, CloudSyncCallback callback) {
        if (payment == null || payment.getPaymentId() == null || payment.getPaymentId().isEmpty()) {
            if (callback != null) callback.onFailure("Invalid payment data");
            return;
        }

        firestore.collection(COLLECTION_PAYMENTS)
                .document(payment.getPaymentId())
                .set(payment.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Payment synced to Cloud: " + payment.getPaymentId());
                    if (callback != null) callback.onSuccess("Payment synced to cloud");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync payment: " + e.getMessage(), e);
                    if (callback != null) callback.onFailure(e.getMessage());
                });
    }

    // ==================== DEVICE IMAGES ====================
    public void saveDeviceImageToCloud(DeviceImage image, CloudSyncCallback callback) {
        if (image == null || image.getImageId() == null) return;
        firestore.collection(COLLECTION_DEVICE_IMAGES)
                .document(image.getImageId())
                .set(image.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> { if (callback != null) callback.onSuccess("Device image synced"); })
                .addOnFailureListener(e -> { if (callback != null) callback.onFailure(e.getMessage()); });
    }

    // ==================== SPARE PARTS ====================
    public void saveSparePartToCloud(SparePart part, CloudSyncCallback callback) {
        if (part == null || part.getPartId() == null) return;
        firestore.collection(COLLECTION_SPARE_PARTS)
                .document(part.getPartId())
                .set(part.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> { if (callback != null) callback.onSuccess("Spare part synced"); })
                .addOnFailureListener(e -> { if (callback != null) callback.onFailure(e.getMessage()); });
    }

    public void deleteSparePartFromCloud(String partId, CloudSyncCallback callback) {
        if (partId == null || partId.isEmpty()) return;
        firestore.collection(COLLECTION_SPARE_PARTS)
                .document(partId)
                .delete()
                .addOnSuccessListener(aVoid -> { if (callback != null) callback.onSuccess("Spare part deleted"); })
                .addOnFailureListener(e -> { if (callback != null) callback.onFailure(e.getMessage()); });
    }

    public SparePart documentToSparePart(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) return null;
        String partId = doc.getString("partId");
        if (partId == null || partId.isEmpty()) partId = doc.getId();
        String branchId = doc.getString("branchId");
        String partName = doc.getString("partName");
        String category = doc.getString("category");
        Long quantity = doc.getLong("quantity");
        Double price = doc.getDouble("price");
        Long lowStockThreshold = doc.getLong("lowStockThreshold");
        Long lastModified = doc.getLong("lastModified");

        return new SparePart(
                partId,
                branchId != null ? branchId : "colombo_01",
                partName != null ? partName : "General Part",
                category != null ? category : "General",
                quantity != null ? quantity.intValue() : 0,
                price != null ? price : 0.0,
                lowStockThreshold != null ? lowStockThreshold.intValue() : 3,
                lastModified != null ? lastModified : System.currentTimeMillis()
        );
    }

    // ==================== REPAIR PARTS USED ====================
    public void saveRepairPartUsedToCloud(RepairPartUsed partUsed, CloudSyncCallback callback) {
        if (partUsed == null || partUsed.getId() == null) return;
        firestore.collection(COLLECTION_REPAIR_PARTS_USED)
                .document(partUsed.getId())
                .set(partUsed.toMap(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> { if (callback != null) callback.onSuccess("Repair part used synced"); })
                .addOnFailureListener(e -> { if (callback != null) callback.onFailure(e.getMessage()); });
    }

    private User documentToUser(DocumentSnapshot doc) {
        String userId = doc.getString("userId");
        String fullName = doc.getString("fullName");
        String email = doc.getString("email");
        String phone = doc.getString("phone");
        String password = doc.getString("password");
        String userType = doc.getString("userType");
        Long createdAt = doc.getLong("createdAt");
        Long lastModified = doc.getLong("lastModified");

        User user = new User();
        user.setUserId(userId != null ? userId : java.util.UUID.randomUUID().toString());
        user.setFullName(fullName != null ? fullName : "");
        user.setEmail(email != null ? email : doc.getId());
        user.setPhone(phone != null ? phone : "");
        user.setPassword(password != null ? password : "");
        user.setUserType(userType != null ? userType : User.TYPE_CUSTOMER);
        user.setCreatedAt(createdAt != null ? createdAt : 0);
        user.setLastModified(lastModified != null ? lastModified : (createdAt != null ? createdAt : 0));
        return user;
    }

    public Appointment documentToAppointment(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) return null;

        String appointmentId = doc.getString("appointmentId");
        if (appointmentId == null || appointmentId.isEmpty()) {
            appointmentId = doc.getId();
        }
        String userId = doc.getString("userId");
        String branchId = doc.getString("branchId");
        String serviceId = doc.getString("serviceId");
        String technicianId = doc.getString("technicianId");
        String deviceType = doc.getString("deviceType");
        String deviceBrand = doc.getString("deviceBrand");
        String deviceModel = doc.getString("deviceModel");
        String problemDescription = doc.getString("problemDescription");
        String preferredDate = doc.getString("preferredDate");
        String preferredTime = doc.getString("preferredTime");
        String status = doc.getString("status");
        Long createdAt = doc.getLong("createdAt");
        Long lastModified = doc.getLong("lastModified");

        long now = System.currentTimeMillis();

        return new Appointment(
                appointmentId,
                userId != null ? userId : "",
                branchId != null ? branchId : "colombo_01",
                serviceId != null ? serviceId : "svc_screen_mobile",
                technicianId,
                deviceType != null ? deviceType : "Mobile Phone",
                deviceBrand != null ? deviceBrand : "",
                deviceModel != null ? deviceModel : "",
                problemDescription != null ? problemDescription : "",
                preferredDate != null ? preferredDate : "",
                preferredTime != null ? preferredTime : "",
                status != null ? status : Appointment.STATUS_APPOINTMENT,
                createdAt != null ? createdAt : now,
                lastModified != null ? lastModified : now
        );
    }
}
