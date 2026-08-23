package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class RepairService {

    private String serviceId;
    private String categoryId;
    private String serviceName;
    private double estimatedPrice;
    private long lastModified;

    public RepairService() {
    }

    public RepairService(String serviceId, String categoryId, String serviceName, double estimatedPrice, long lastModified) {
        this.serviceId = serviceId;
        this.categoryId = categoryId;
        this.serviceName = serviceName;
        this.estimatedPrice = estimatedPrice;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceId", serviceId);
        map.put("categoryId", categoryId);
        map.put("serviceName", serviceName);
        map.put("estimatedPrice", estimatedPrice);
        map.put("lastModified", lastModified);
        return map;
    }
}
