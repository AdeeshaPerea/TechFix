package com.example.techfix.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role; // "TECH" or "ADMIN"
    private String specialization;
    private String branchId;
    private String branchName;
    private String workingHours;
    private int activeRepairsCount;
    private String profileImageUrl;

    public User() {}

    public User(String id, String name, String email, String phone, String role, String specialization, String branchId, String branchName, String workingHours, int activeRepairsCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.specialization = specialization;
        this.branchId = branchId;
        this.branchName = branchName;
        this.workingHours = workingHours;
        this.activeRepairsCount = activeRepairsCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public int getActiveRepairsCount() { return activeRepairsCount; }
    public void setActiveRepairsCount(int activeRepairsCount) { this.activeRepairsCount = activeRepairsCount; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
