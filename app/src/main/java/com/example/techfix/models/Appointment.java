package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class Appointment {

    public static final String STATUS_APPOINTMENT = "Appointment";
    public static final String STATUS_RECEIVED = "Received";
    public static final String STATUS_DIAGNOSING = "Diagnosing";
    public static final String STATUS_REPAIRING = "Repairing";
    public static final String STATUS_QUALITY_CHECK = "Quality Check";
    public static final String STATUS_READY = "Ready";
    public static final String STATUS_COMPLETED = "Completed";

    private String appointmentId;
    private String userId;
    private String branchId;
    private String serviceId;
    private String technicianId; // Nullable
    private String deviceType;   // Mobile Phone, Laptop, Computer
    private String deviceBrand;
    private String deviceModel;
    private String problemDescription;
    private String preferredDate;
    private String preferredTime;
    private String status;
    private long createdAt;
    private long lastModified;

    public Appointment() {
    }

    public Appointment(String appointmentId, String userId, String branchId, String serviceId, String technicianId, String deviceType, String deviceBrand, String deviceModel, String problemDescription, String preferredDate, String preferredTime, String status, long createdAt, long lastModified) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.branchId = branchId;
        this.serviceId = serviceId;
        this.technicianId = technicianId;
        this.deviceType = deviceType != null ? deviceType : "Mobile Phone";
        this.deviceBrand = deviceBrand;
        this.deviceModel = deviceModel;
        this.problemDescription = problemDescription;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.status = status != null ? status : STATUS_APPOINTMENT;
        this.createdAt = createdAt > 0 ? createdAt : System.currentTimeMillis();
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public Appointment(String appointmentId, String userId, String branchId, String serviceId, String technicianId, String deviceBrand, String deviceModel, String problemDescription, String preferredDate, String preferredTime, String status, long createdAt, long lastModified) {
        this(appointmentId, userId, branchId, serviceId, technicianId, "Mobile Phone", deviceBrand, deviceModel, problemDescription, preferredDate, preferredTime, status, createdAt, lastModified);
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("appointmentId", appointmentId);
        map.put("userId", userId);
        map.put("branchId", branchId);
        map.put("serviceId", serviceId);
        map.put("technicianId", technicianId);
        map.put("deviceType", deviceType != null ? deviceType : "Mobile Phone");
        map.put("deviceBrand", deviceBrand);
        map.put("deviceModel", deviceModel);
        map.put("problemDescription", problemDescription);
        map.put("preferredDate", preferredDate);
        map.put("preferredTime", preferredTime);
        map.put("status", status);
        map.put("createdAt", createdAt);
        map.put("lastModified", lastModified);
        return map;
    }
}
