package com.example.techfix.model;

public class SparePartItem {
    private String id;
    private String name;
    private String category;
    private String compatibleDevice;
    private int quantity;
    private double unitPriceLkr;
    private int minStockThreshold;
    private String availabilityStatus; // AVAILABLE, LOW_STOCK, OUT_OF_STOCK

    public SparePartItem() {}

    public SparePartItem(String id, String name, String category, String compatibleDevice, int quantity, double unitPriceLkr, int minStockThreshold) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.compatibleDevice = compatibleDevice;
        this.quantity = quantity;
        this.unitPriceLkr = unitPriceLkr;
        this.minStockThreshold = minStockThreshold;
        updateAvailabilityStatus();
    }

    public void updateAvailabilityStatus() {
        if (quantity <= 0) {
            availabilityStatus = "OUT_OF_STOCK";
        } else if (quantity <= minStockThreshold) {
            availabilityStatus = "LOW_STOCK";
        } else {
            availabilityStatus = "AVAILABLE";
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCompatibleDevice() { return compatibleDevice; }
    public void setCompatibleDevice(String compatibleDevice) { this.compatibleDevice = compatibleDevice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateAvailabilityStatus();
    }

    public double getUnitPriceLkr() { return unitPriceLkr; }
    public void setUnitPriceLkr(double unitPriceLkr) { this.unitPriceLkr = unitPriceLkr; }

    public int getMinStockThreshold() { return minStockThreshold; }
    public void setMinStockThreshold(int minStockThreshold) {
        this.minStockThreshold = minStockThreshold;
        updateAvailabilityStatus();
    }

    public String getAvailabilityStatus() {
        updateAvailabilityStatus();
        return availabilityStatus;
    }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
}
