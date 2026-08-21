package com.example.techfix.model;

public class RepairServiceItem {
    private String id;
    private String title;
    private String category; // Mobile, Laptop, Tablet, General
    private double priceLkr;
    private String estimatedDuration;
    private String description;

    public RepairServiceItem() {}

    public RepairServiceItem(String id, String title, String category, double priceLkr, String estimatedDuration, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.priceLkr = priceLkr;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPriceLkr() { return priceLkr; }
    public void setPriceLkr(double priceLkr) { this.priceLkr = priceLkr; }

    public String getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
