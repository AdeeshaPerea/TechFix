package com.example.techfix.model;

public class RepairNoteItem {
    private String id;
    private String repairId;
    private String authorName;
    private String category; // DIAGNOSIS, WORK_PERFORMED, GENERAL
    private String noteText;
    private String timestamp;

    public RepairNoteItem() {}

    public RepairNoteItem(String id, String repairId, String authorName, String category, String noteText, String timestamp) {
        this.id = id;
        this.repairId = repairId;
        this.authorName = authorName;
        this.category = category;
        this.noteText = noteText;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRepairId() { return repairId; }
    public void setRepairId(String repairId) { this.repairId = repairId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNoteText() { return noteText; }
    public void setNoteText(String noteText) { this.noteText = noteText; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
