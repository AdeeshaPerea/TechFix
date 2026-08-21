package com.example.techfix.model;

public class GalleryItem {
    private String id;
    private String title;
    private String category;
    private String description;
    private String beforeTag;
    private String afterTag;
    private String dateAdded;

    public GalleryItem() {}

    public GalleryItem(String id, String title, String category, String description, String beforeTag, String afterTag, String dateAdded) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.beforeTag = beforeTag;
        this.afterTag = afterTag;
        this.dateAdded = dateAdded;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBeforeTag() { return beforeTag; }
    public void setBeforeTag(String beforeTag) { this.beforeTag = beforeTag; }

    public String getAfterTag() { return afterTag; }
    public void setAfterTag(String afterTag) { this.afterTag = afterTag; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
}
