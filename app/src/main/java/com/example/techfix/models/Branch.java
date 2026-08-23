package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class Branch {

    private String branchId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String contactNumber;
    private String openingHours;
    private long lastModified;

    public Branch() {
    }

    public Branch(String branchId, String name, String address, double latitude, double longitude, String contactNumber, String openingHours, long lastModified) {
        this.branchId = branchId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactNumber = contactNumber;
        this.openingHours = openingHours;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("branchId", branchId);
        map.put("name", name);
        map.put("address", address);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("contactNumber", contactNumber);
        map.put("openingHours", openingHours);
        map.put("lastModified", lastModified);
        return map;
    }
}
