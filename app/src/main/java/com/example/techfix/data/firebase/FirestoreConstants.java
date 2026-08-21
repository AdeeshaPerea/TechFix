package com.example.techfix.data.firebase;

public class FirestoreConstants {

    // Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CUSTOMERS = "customers";
    public static final String COLLECTION_TECHNICIANS = "technicians";
    public static final String COLLECTION_BRANCHES = "branches";
    public static final String COLLECTION_SERVICES = "services";
    public static final String COLLECTION_APPOINTMENTS = "appointments";
    public static final String COLLECTION_REPAIRS = "repairs";
    public static final String COLLECTION_SPARE_PARTS = "spareParts";
    public static final String COLLECTION_REPAIR_NOTES = "repairNotes";
    public static final String COLLECTION_REPAIR_PARTS = "repairParts";
    public static final String COLLECTION_GALLERY = "gallery";

    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TECHNICIAN = "TECHNICIAN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    // Canonical Repair Statuses
    public static final String STATUS_BOOKED = "BOOKED";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_DIAGNOSING = "DIAGNOSING";
    public static final String STATUS_WAITING_FOR_PARTS = "WAITING_FOR_PARTS";
    public static final String STATUS_REPAIRING = "REPAIRING";
    public static final String STATUS_QUALITY_CHECK = "QUALITY_CHECK";
    public static final String STATUS_READY_FOR_COLLECTION = "READY_FOR_COLLECTION";
    public static final String STATUS_COMPLETED = "COMPLETED";

    // Stock Statuses
    public static final String STOCK_AVAILABLE = "AVAILABLE";
    public static final String STOCK_LOW_STOCK = "LOW_STOCK";
    public static final String STOCK_OUT_OF_STOCK = "OUT_OF_STOCK";
}
