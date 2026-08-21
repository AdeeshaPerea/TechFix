package com.example.techfix.model;

import java.util.ArrayList;
import java.util.List;

public class RepairItem {
    private String id;
    private String repairCode; // e.g. TF-2026-00125
    private String customerName;
    private String customerPhone;
    private String deviceName; // e.g. Samsung Galaxy A54
    private String deviceModel;
    private String problemDescription;
    private String serviceRequested; // e.g. Screen Replacement
    private String priority; // High, Medium, Low, Urgent
    private String status; // BOOKED, CONFIRMED, RECEIVED, DIAGNOSING, WAITING FOR PARTS, REPAIRING, QUALITY CHECK, READY FOR COLLECTION, COMPLETED
    private String appointmentTime;
    private String appointmentDate;
    private String branchName;
    private String branchId;
    private String assignedTechId;
    private String assignedTechName;
    private double estimatedCost;
    private String diagnosisSummary;
    private String problemFound;
    private String recommendedRepair;
    private int estimatedDurationHours;
    private String requiredPartsNotes;
    private String createdDate;
    private List<SparePartUsed> sparePartsUsed;
    private List<RepairNoteItem> notes;
    private List<String> beforeImages;
    private List<String> afterImages;

    public static class SparePartUsed {
        private String partId;
        private String partName;
        private int quantity;
        private double unitPrice;

        public SparePartUsed() {}

        public SparePartUsed(String partId, String partName, int quantity, double unitPrice) {
            this.partId = partId;
            this.partName = partName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getPartId() { return partId; }
        public void setPartId(String partId) { this.partId = partId; }

        public String getPartName() { return partName; }
        public void setPartName(String partName) { this.partName = partName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

        public double getTotalPrice() { return quantity * unitPrice; }
    }

    public RepairItem() {
        sparePartsUsed = new ArrayList<>();
        notes = new ArrayList<>();
        beforeImages = new ArrayList<>();
        afterImages = new ArrayList<>();
    }

    public RepairItem(String id, String repairCode, String customerName, String customerPhone, String deviceName, String deviceModel, String problemDescription, String serviceRequested, String priority, String status, String appointmentDate, String appointmentTime, String branchName, String branchId, String assignedTechId, String assignedTechName, double estimatedCost) {
        this.id = id;
        this.repairCode = repairCode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.problemDescription = problemDescription;
        this.serviceRequested = serviceRequested;
        this.priority = priority;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.branchName = branchName;
        this.branchId = branchId;
        this.assignedTechId = assignedTechId;
        this.assignedTechName = assignedTechName;
        this.estimatedCost = estimatedCost;
        this.sparePartsUsed = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.beforeImages = new ArrayList<>();
        this.afterImages = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRepairCode() { return repairCode; }
    public void setRepairCode(String repairCode) { this.repairCode = repairCode; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }

    public String getServiceRequested() { return serviceRequested; }
    public void setServiceRequested(String serviceRequested) { this.serviceRequested = serviceRequested; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getAssignedTechId() { return assignedTechId; }
    public void setAssignedTechId(String assignedTechId) { this.assignedTechId = assignedTechId; }

    public String getAssignedTechName() { return assignedTechName; }
    public void setAssignedTechName(String assignedTechName) { this.assignedTechName = assignedTechName; }

    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }

    public String getDiagnosisSummary() { return diagnosisSummary; }
    public void setDiagnosisSummary(String diagnosisSummary) { this.diagnosisSummary = diagnosisSummary; }

    public String getProblemFound() { return problemFound; }
    public void setProblemFound(String problemFound) { this.problemFound = problemFound; }

    public String getRecommendedRepair() { return recommendedRepair; }
    public void setRecommendedRepair(String recommendedRepair) { this.recommendedRepair = recommendedRepair; }

    public int getEstimatedDurationHours() { return estimatedDurationHours; }
    public void setEstimatedDurationHours(int estimatedDurationHours) { this.estimatedDurationHours = estimatedDurationHours; }

    public String getRequiredPartsNotes() { return requiredPartsNotes; }
    public void setRequiredPartsNotes(String requiredPartsNotes) { this.requiredPartsNotes = requiredPartsNotes; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public List<SparePartUsed> getSparePartsUsed() { return sparePartsUsed; }
    public void setSparePartsUsed(List<SparePartUsed> sparePartsUsed) { this.sparePartsUsed = sparePartsUsed; }

    public List<RepairNoteItem> getNotes() { return notes; }
    public void setNotes(List<RepairNoteItem> notes) { this.notes = notes; }

    public List<String> getBeforeImages() { return beforeImages; }
    public void setBeforeImages(List<String> beforeImages) { this.beforeImages = beforeImages; }

    public List<String> getAfterImages() { return afterImages; }
    public void setAfterImages(List<String> afterImages) { this.afterImages = afterImages; }
}
