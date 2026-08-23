package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class Technician {

    private String technicianId;
    private String name;
    private String specialization;
    private String availabilityStatus; // Available, Busy, On Leave
    private String branchId;
    private long lastModified;

    public Technician() {
    }

    public Technician(String technicianId, String name, String specialization, String availabilityStatus, String branchId, long lastModified) {
        this.technicianId = technicianId;
        this.name = name;
        this.specialization = specialization;
        this.availabilityStatus = availabilityStatus;
        this.branchId = branchId;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("technicianId", technicianId);
        map.put("name", name);
        map.put("specialization", specialization);
        map.put("availabilityStatus", availabilityStatus);
        map.put("branchId", branchId);
        map.put("lastModified", lastModified);
        return map;
    }
}
