package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class RepairPartUsed {

    private String id;
    private String appointmentId;
    private String partId;
    private int quantityUsed;
    private long lastModified;

    public RepairPartUsed() {
    }

    public RepairPartUsed(String id, String appointmentId, String partId, int quantityUsed, long lastModified) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.partId = partId;
        this.quantityUsed = quantityUsed;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public int getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(int quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("appointmentId", appointmentId);
        map.put("partId", partId);
        map.put("quantityUsed", quantityUsed);
        map.put("lastModified", lastModified);
        return map;
    }
}
