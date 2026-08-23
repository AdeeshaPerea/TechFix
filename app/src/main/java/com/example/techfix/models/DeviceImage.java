package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class DeviceImage {

    public static final String TYPE_DAMAGE = "damage photo";
    public static final String TYPE_BEFORE = "before";
    public static final String TYPE_AFTER = "after";

    private String imageId;
    private String appointmentId;
    private String imagePath;
    private String imageType;
    private long uploadedAt;
    private long lastModified;

    public DeviceImage() {
    }

    public DeviceImage(String imageId, String appointmentId, String imagePath, String imageType, long uploadedAt, long lastModified) {
        this.imageId = imageId;
        this.appointmentId = appointmentId;
        this.imagePath = imagePath;
        this.imageType = imageType;
        this.uploadedAt = uploadedAt > 0 ? uploadedAt : System.currentTimeMillis();
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public long getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(long uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("imageId", imageId);
        map.put("appointmentId", appointmentId);
        map.put("imagePath", imagePath);
        map.put("imageType", imageType);
        map.put("uploadedAt", uploadedAt);
        map.put("lastModified", lastModified);
        return map;
    }
}
