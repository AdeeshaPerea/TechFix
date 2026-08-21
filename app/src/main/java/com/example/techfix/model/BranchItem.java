package com.example.techfix.model;

public class BranchItem {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private double latitude;
    private double longitude;
    private int technicianCount;
    private int activeRepairsCount;

    public BranchItem() {}

    public BranchItem(String id, String name, String address, String phone, String openingHours, double latitude, double longitude, int technicianCount, int activeRepairsCount) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.openingHours = openingHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.technicianCount = technicianCount;
        this.activeRepairsCount = activeRepairsCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getTechnicianCount() { return technicianCount; }
    public void setTechnicianCount(int technicianCount) { this.technicianCount = technicianCount; }

    public int getActiveRepairsCount() { return activeRepairsCount; }
    public void setActiveRepairsCount(int activeRepairsCount) { this.activeRepairsCount = activeRepairsCount; }
}
