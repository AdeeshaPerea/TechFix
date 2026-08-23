package com.example.techfix.models;

import java.util.HashMap;
import java.util.Map;

public class Payment {

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PAID = "Paid";
    public static final String STATUS_FAILED = "Failed";

    public static final String METHOD_CASH = "Cash";
    public static final String METHOD_CARD = "Card";
    public static final String METHOD_ONLINE = "Online";

    private String paymentId;
    private String appointmentId;
    private double amount;
    private String paymentStatus;
    private String paymentMethod;
    private long paymentDate;
    private String transactionReference;
    private long lastModified;

    public Payment() {
        // Default constructor
    }

    public Payment(String paymentId, String appointmentId, double amount, String paymentStatus, String paymentMethod, long paymentDate, String transactionReference, long lastModified) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.transactionReference = transactionReference;
        this.lastModified = lastModified > 0 ? lastModified : System.currentTimeMillis();
    }

    public Payment(String paymentId, String appointmentId, double amount, String paymentStatus, String paymentMethod, long paymentDate, String transactionReference) {
        this(paymentId, appointmentId, amount, paymentStatus, paymentMethod, paymentDate, transactionReference, System.currentTimeMillis());
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("paymentId", paymentId);
        map.put("appointmentId", appointmentId);
        map.put("amount", amount);
        map.put("paymentStatus", paymentStatus);
        map.put("paymentMethod", paymentMethod);
        map.put("paymentDate", paymentDate);
        map.put("transactionReference", transactionReference);
        map.put("lastModified", lastModified);
        return map;
    }
}
