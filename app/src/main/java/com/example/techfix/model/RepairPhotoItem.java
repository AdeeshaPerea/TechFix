package com.example.techfix.model;

public class RepairPhotoItem {
    private String photoId;
    private String repairId;
    private String photoUrl;
    private String storagePath;
    private String photoType; // BEFORE_REPAIR or AFTER_REPAIR
    private String uploadedBy;
    private String uploadedByRole; // CUSTOMER or TECHNICIAN
    private long uploadedAt;

    public RepairPhotoItem() {
        // Required empty constructor for Firestore deserialization
    }

    public RepairPhotoItem(String photoId, String repairId, String photoUrl, String storagePath, String photoType, String uploadedBy, String uploadedByRole, long uploadedAt) {
        this.photoId = photoId;
        this.repairId = repairId;
        this.photoUrl = photoUrl;
        this.storagePath = storagePath;
        this.photoType = photoType;
        this.uploadedBy = uploadedBy;
        this.uploadedByRole = uploadedByRole;
        this.uploadedAt = uploadedAt;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedByRole() {
        return uploadedByRole;
    }

    public void setUploadedByRole(String uploadedByRole) {
        this.uploadedByRole = uploadedByRole;
    }

    public long getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(long uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
