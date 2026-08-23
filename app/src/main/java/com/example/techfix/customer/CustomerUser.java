package com.example.techfix.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerUser {

    public static final String TYPE_CUSTOMER = "customer";

    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String userType;
    private long createdAt;
    private long lastModified;

    public CustomerUser() {
    }

    public CustomerUser(String fullName, String email, String phone, String password) {
        this.userId = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userType = TYPE_CUSTOMER;
        this.createdAt = System.currentTimeMillis();
        this.lastModified = this.createdAt;
    }

    public CustomerUser(String userId, String fullName, String email, String phone, String password, String userType, long createdAt, long lastModified) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userType = (userType != null) ? userType : TYPE_CUSTOMER;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("phone", phone);
        result.put("password", password);
        result.put("userType", userType != null ? userType : TYPE_CUSTOMER);
        result.put("createdAt", createdAt);
        result.put("lastModified", lastModified);
        return result;
    }
}
