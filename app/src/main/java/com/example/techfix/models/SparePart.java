package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class SparePart {

    private String partId;
    private String branchId;
    private String partName;
    private String category;
    private int quantity;
    private double price;
    private int lowStockThreshold;
    private long lastModified;

    public SparePart() {
    }

    public SparePart(String partId, String branchId, String partName, String category, int quantity, double price, int lowStockThreshold, long lastModified) {
        this.partId = partId;
        this.branchId = branchId;
        this.partName = partName;
        this.category = category != null ? category : "General";
        this.quantity = quantity;
        this.price = price;
        this.lowStockThreshold = lowStockThreshold;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public SparePart(String partId, String branchId, String partName, int quantity, double price, int lowStockThreshold, long lastModified) {
        this(partId, branchId, partName, "General", quantity, price, lowStockThreshold, lastModified);
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("partId", partId);
        map.put("branchId", branchId);
        map.put("partName", partName);
        map.put("category", category != null ? category : "General");
        map.put("quantity", quantity);
        map.put("price", price);
        map.put("lowStockThreshold", lowStockThreshold);
        map.put("lastModified", lastModified);
        return map;
    }
}
