package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class DeviceCategory {

    private String categoryId;
    private String categoryName;
    private long lastModified;

    public DeviceCategory() {
    }

    public DeviceCategory(String categoryId, String categoryName, long lastModified) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("categoryName", categoryName);
        map.put("lastModified", lastModified);
        return map;
    }
}
