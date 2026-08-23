package com.example.techfix.model;

public class AppointmentItem {
    private String id;
    private String appointmentCode; // e.g. APT-8821
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String deviceModel;
    private String serviceRequested;
    private String preferredDate;
    private String preferredTime;
    private String branchId;
    private String branchName;
    private String assignedTechId;
    private String assignedTechName;
    private String status; // PENDING, CONFIRMED, REJECTED, COMPLETED
    private String problemDescription;

    public AppointmentItem() {}

    public AppointmentItem(String id, String appointmentCode, String customerName, String customerPhone, String customerEmail, String deviceModel, String serviceRequested, String preferredDate, String preferredTime, String branchId, String branchName, String assignedTechId, String assignedTechName, String status, String problemDescription) {
        this.id = id;
        this.appointmentCode = appointmentCode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.deviceModel = deviceModel;
        this.serviceRequested = serviceRequested;
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.branchId = branchId;
        this.branchName = branchName;
        this.assignedTechId = assignedTechId;
        this.assignedTechName = assignedTechName;
        this.status = status;
        this.problemDescription = problemDescription;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAppointmentCode() { return appointmentCode; }
    public void setAppointmentCode(String appointmentCode) { this.appointmentCode = appointmentCode; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getServiceRequested() { return serviceRequested; }
    public void setServiceRequested(String serviceRequested) { this.serviceRequested = serviceRequested; }

    public String getPreferredDate() { return preferredDate; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }

    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getAssignedTechId() { return assignedTechId; }
    public void setAssignedTechId(String assignedTechId) { this.assignedTechId = assignedTechId; }

    public String getAssignedTechName() { return assignedTechName; }
    public void setAssignedTechName(String assignedTechName) { this.assignedTechName = assignedTechName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }
}
