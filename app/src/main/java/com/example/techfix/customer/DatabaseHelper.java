package com.example.techfix.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.techfix.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 8;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_BRANCHES = "branches";
    public static final String TABLE_DEVICE_CATEGORIES = "device_categories";
    public static final String TABLE_REPAIR_SERVICES = "repair_services";
    public static final String TABLE_TECHNICIANS = "technicians";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_DEVICE_IMAGES = "device_images";
    public static final String TABLE_SPARE_PARTS = "spare_parts";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_REPAIR_PARTS_USED = "repair_parts_used";

    // Common Column
    public static final String COLUMN_LAST_MODIFIED = "last_modified";

    // Users Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_USER_TYPE = "user_type";

    // Branches Columns
    public static final String COLUMN_BRANCH_ID = "branch_id";
    public static final String COLUMN_BRANCH_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_OPENING_HOURS = "opening_hours";

    // Device Categories Columns
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    // Repair Services Columns
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_ESTIMATED_PRICE = "estimated_price";

    // Technicians Columns
    public static final String COLUMN_TECHNICIAN_ID = "technician_id";
    public static final String COLUMN_TECH_NAME = "name";
    public static final String COLUMN_SPECIALIZATION = "specialization";
    public static final String COLUMN_AVAILABILITY_STATUS = "availability_status";

    // Appointments Columns
    public static final String COLUMN_APPOINTMENT_ID = "appointment_id";
    public static final String COLUMN_DEVICE_TYPE = "device_type";
    public static final String COLUMN_DEVICE_BRAND = "device_brand";
    public static final String COLUMN_DEVICE_MODEL = "device_model";
    public static final String COLUMN_PROBLEM_DESC = "problem_description";
    public static final String COLUMN_PREFERRED_DATE = "preferred_date";
    public static final String COLUMN_PREFERRED_TIME = "preferred_time";
    public static final String COLUMN_STATUS = "status";

    // Device Images Columns
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_IMAGE_TYPE = "image_type";
    public static final String COLUMN_UPLOADED_AT = "uploaded_at";

    // Spare Parts Columns
    public static final String COLUMN_PART_ID = "part_id";
    public static final String COLUMN_PART_NAME = "part_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_LOW_STOCK_THRESHOLD = "low_stock_threshold";

    // Payments Columns
    public static final String COLUMN_PAYMENT_ID = "payment_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_PAYMENT_STATUS = "payment_status";
    public static final String COLUMN_PAYMENT_METHOD = "payment_method";
    public static final String COLUMN_PAYMENT_DATE = "payment_date";
    public static final String COLUMN_TRANSACTION_REF = "transaction_reference";

    // Repair Parts Used Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUANTITY_USED = "quantity_used";

    // Create Table SQL Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY, "
            + COLUMN_FULL_NAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_PHONE + " TEXT NOT NULL, "
            + COLUMN_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_USER_TYPE + " TEXT NOT NULL DEFAULT 'customer', "
            + COLUMN_CREATED_AT + " INTEGER, "
            + COLUMN_LAST_MODIFIED + " INTEGER"
            + ");";

    private static final String CREATE_TABLE_BRANCHES = "CREATE TABLE " + TABLE_BRANCHES + " ("
            + COLUMN_BRANCH_ID + " TEXT PRIMARY KEY, "
            + COLUMN_BRANCH_NAME + " TEXT NOT NULL, "
            + COLUMN_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_LATITUDE + " REAL NOT NULL, "
            + COLUMN_LONGITUDE + " REAL NOT NULL, "
            + COLUMN_CONTACT_NUMBER + " TEXT, "
            + COLUMN_OPENING_HOURS + " TEXT, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_DEVICE_CATEGORIES = "CREATE TABLE " + TABLE_DEVICE_CATEGORIES + " ("
            + COLUMN_CATEGORY_ID + " TEXT PRIMARY KEY, "
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_REPAIR_SERVICES = "CREATE TABLE " + TABLE_REPAIR_SERVICES + " ("
            + COLUMN_SERVICE_ID + " TEXT PRIMARY KEY, "
            + COLUMN_CATEGORY_ID + " TEXT NOT NULL, "
            + COLUMN_SERVICE_NAME + " TEXT NOT NULL, "
            + COLUMN_ESTIMATED_PRICE + " REAL NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_TECHNICIANS = "CREATE TABLE " + TABLE_TECHNICIANS + " ("
            + COLUMN_TECHNICIAN_ID + " TEXT PRIMARY KEY, "
            + COLUMN_TECH_NAME + " TEXT NOT NULL, "
            + COLUMN_SPECIALIZATION + " TEXT, "
            + COLUMN_AVAILABILITY_STATUS + " TEXT NOT NULL, "
            + COLUMN_BRANCH_ID + " TEXT NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS + " ("
            + COLUMN_APPOINTMENT_ID + " TEXT PRIMARY KEY, "
            + COLUMN_USER_ID + " TEXT NOT NULL, "
            + COLUMN_BRANCH_ID + " TEXT NOT NULL, "
            + COLUMN_SERVICE_ID + " TEXT NOT NULL, "
            + COLUMN_TECHNICIAN_ID + " TEXT, "
            + COLUMN_DEVICE_TYPE + " TEXT NOT NULL DEFAULT 'Mobile Phone', "
            + COLUMN_DEVICE_BRAND + " TEXT NOT NULL, "
            + COLUMN_DEVICE_MODEL + " TEXT NOT NULL, "
            + COLUMN_PROBLEM_DESC + " TEXT, "
            + COLUMN_PREFERRED_DATE + " TEXT NOT NULL, "
            + COLUMN_PREFERRED_TIME + " TEXT NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL, "
            + COLUMN_CREATED_AT + " INTEGER NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_DEVICE_IMAGES = "CREATE TABLE " + TABLE_DEVICE_IMAGES + " ("
            + COLUMN_IMAGE_ID + " TEXT PRIMARY KEY, "
            + COLUMN_APPOINTMENT_ID + " TEXT NOT NULL, "
            + COLUMN_IMAGE_PATH + " TEXT NOT NULL, "
            + COLUMN_IMAGE_TYPE + " TEXT NOT NULL, "
            + COLUMN_UPLOADED_AT + " INTEGER NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_SPARE_PARTS = "CREATE TABLE " + TABLE_SPARE_PARTS + " ("
            + COLUMN_PART_ID + " TEXT PRIMARY KEY, "
            + COLUMN_BRANCH_ID + " TEXT NOT NULL, "
            + COLUMN_PART_NAME + " TEXT NOT NULL, "
            + COLUMN_CATEGORY_ID + " TEXT NOT NULL DEFAULT 'General', "
            + COLUMN_QUANTITY + " INTEGER NOT NULL, "
            + COLUMN_PRICE + " REAL NOT NULL, "
            + COLUMN_LOW_STOCK_THRESHOLD + " INTEGER NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + TABLE_PAYMENTS + " ("
            + COLUMN_PAYMENT_ID + " TEXT PRIMARY KEY, "
            + COLUMN_APPOINTMENT_ID + " TEXT NOT NULL, "
            + COLUMN_AMOUNT + " REAL NOT NULL, "
            + COLUMN_PAYMENT_STATUS + " TEXT NOT NULL, "
            + COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, "
            + COLUMN_PAYMENT_DATE + " INTEGER NOT NULL, "
            + COLUMN_TRANSACTION_REF + " TEXT, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_TABLE_REPAIR_PARTS_USED = "CREATE TABLE " + TABLE_REPAIR_PARTS_USED + " ("
            + COLUMN_ID + " TEXT PRIMARY KEY, "
            + COLUMN_APPOINTMENT_ID + " TEXT NOT NULL, "
            + COLUMN_PART_ID + " TEXT NOT NULL, "
            + COLUMN_QUANTITY_USED + " INTEGER NOT NULL, "
            + COLUMN_LAST_MODIFIED + " INTEGER NOT NULL"
            + ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BRANCHES);
        db.execSQL(CREATE_TABLE_DEVICE_CATEGORIES);
        db.execSQL(CREATE_TABLE_REPAIR_SERVICES);
        db.execSQL(CREATE_TABLE_TECHNICIANS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_DEVICE_IMAGES);
        db.execSQL(CREATE_TABLE_SPARE_PARTS);
        db.execSQL(CREATE_TABLE_PAYMENTS);
        db.execSQL(CREATE_TABLE_REPAIR_PARTS_USED);

        seedDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPAIR_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNICIANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPARE_PARTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPAIR_PARTS_USED);
        onCreate(db);
    }

    private void seedDefaultData(SQLiteDatabase db) {
        long now = System.currentTimeMillis();

        // 1. Branches
        insertBranchRaw(db, new Branch("colombo_01", "Colombo 01 Branch", "Galle Road, Colombo 01", 6.9061, 79.8517, "+94112345678", "Mon-Sat 8:30 AM - 6:30 PM", now));
        insertBranchRaw(db, new Branch("galle_02", "Galle 02 Branch", "Main Street, Galle Fort", 6.0329, 80.2168, "+94912234567", "Mon-Sat 9:00 AM - 5:30 PM", now));

        // 2. Device Categories
        insertCategoryRaw(db, new DeviceCategory("cat_mobile", "Mobile Phone", now));
        insertCategoryRaw(db, new DeviceCategory("cat_laptop", "Laptop", now));
        insertCategoryRaw(db, new DeviceCategory("cat_computer", "Computer", now));

        // 3. Repair Services
        insertServiceRaw(db, new RepairService("svc_screen_mobile", "cat_mobile", "Screen Replacement", 7000.0, now));
        insertServiceRaw(db, new RepairService("svc_battery_mobile", "cat_mobile", "Battery Replacement", 3500.0, now));
        insertServiceRaw(db, new RepairService("svc_battery_laptop", "cat_laptop", "Battery Service", 8500.0, now));
        insertServiceRaw(db, new RepairService("svc_keyboard_laptop", "cat_laptop", "Keyboard Replacement", 6500.0, now));
        insertServiceRaw(db, new RepairService("svc_ssd_laptop", "cat_laptop", "SSD / Hard Drive Upgrade", 9500.0, now));
        insertServiceRaw(db, new RepairService("svc_ssd_pc", "cat_computer", "SSD / Hard Drive Upgrade", 9500.0, now));
        insertServiceRaw(db, new RepairService("svc_motherboard_pc", "cat_computer", "Motherboard Diagnostics", 12000.0, now));

        // 4. Technicians
        insertTechnicianRaw(db, new Technician("tech_01", "Kamal Perera", "Mobile Hardware Specialist", "Available", "colombo_01", now));
        insertTechnicianRaw(db, new Technician("tech_02", "Nimal Fernando", "Laptop & Display Specialist", "Available", "galle_02", now));

        // 5. Spare Parts
        insertPartRaw(db, new SparePart("part_sc_a54", "colombo_01", "Samsung A54 OLED Screen", "Screen / Display", 15, 7000.0, 5, now));
        insertPartRaw(db, new SparePart("part_bat_dell", "colombo_01", "Dell Inspiron Battery 54Wh", "Battery", 8, 8500.0, 3, now));
        insertPartRaw(db, new SparePart("part_ssd_nvme", "colombo_01", "Kingston 512GB NVMe SSD", "SSD / Hard Drive", 2, 9500.0, 4, now));
        insertPartRaw(db, new SparePart("part_kb_macbook", "galle_02", "MacBook Pro A2338 Keyboard", "Keyboard", 12, 11500.0, 3, now));
        insertPartRaw(db, new SparePart("part_mb_pc", "galle_02", "ASUS B550 Motherboard", "Motherboard", 1, 32000.0, 2, now));
    }

    private void insertBranchRaw(SQLiteDatabase db, Branch b) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_BRANCH_ID, b.getBranchId());
        v.put(COLUMN_BRANCH_NAME, b.getName());
        v.put(COLUMN_ADDRESS, b.getAddress());
        v.put(COLUMN_LATITUDE, b.getLatitude());
        v.put(COLUMN_LONGITUDE, b.getLongitude());
        v.put(COLUMN_CONTACT_NUMBER, b.getContactNumber());
        v.put(COLUMN_OPENING_HOURS, b.getOpeningHours());
        v.put(COLUMN_LAST_MODIFIED, b.getLastModified());
        db.insertWithOnConflict(TABLE_BRANCHES, null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertCategoryRaw(SQLiteDatabase db, DeviceCategory c) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_CATEGORY_ID, c.getCategoryId());
        v.put(COLUMN_CATEGORY_NAME, c.getCategoryName());
        v.put(COLUMN_LAST_MODIFIED, c.getLastModified());
        db.insertWithOnConflict(TABLE_DEVICE_CATEGORIES, null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertServiceRaw(SQLiteDatabase db, RepairService s) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_SERVICE_ID, s.getServiceId());
        v.put(COLUMN_CATEGORY_ID, s.getCategoryId());
        v.put(COLUMN_SERVICE_NAME, s.getServiceName());
        v.put(COLUMN_ESTIMATED_PRICE, s.getEstimatedPrice());
        v.put(COLUMN_LAST_MODIFIED, s.getLastModified());
        db.insertWithOnConflict(TABLE_REPAIR_SERVICES, null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertTechnicianRaw(SQLiteDatabase db, Technician t) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TECHNICIAN_ID, t.getTechnicianId());
        v.put(COLUMN_TECH_NAME, t.getName());
        v.put(COLUMN_SPECIALIZATION, t.getSpecialization());
        v.put(COLUMN_AVAILABILITY_STATUS, t.getAvailabilityStatus());
        v.put(COLUMN_BRANCH_ID, t.getBranchId());
        v.put(COLUMN_LAST_MODIFIED, t.getLastModified());
        db.insertWithOnConflict(TABLE_TECHNICIANS, null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertPartRaw(SQLiteDatabase db, SparePart p) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_PART_ID, p.getPartId());
        v.put(COLUMN_BRANCH_ID, p.getBranchId());
        v.put(COLUMN_PART_NAME, p.getPartName());
        v.put(COLUMN_CATEGORY_ID, p.getCategory() != null ? p.getCategory() : "General");
        v.put(COLUMN_QUANTITY, p.getQuantity());
        v.put(COLUMN_PRICE, p.getPrice());
        v.put(COLUMN_LOW_STOCK_THRESHOLD, p.getLowStockThreshold());
        v.put(COLUMN_LAST_MODIFIED, p.getLastModified());
        db.insertWithOnConflict(TABLE_SPARE_PARTS, null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // ==================== USERS ====================
    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_FULL_NAME, user.getFullName().trim());
        values.put(COLUMN_EMAIL, user.getEmail().trim().toLowerCase());
        values.put(COLUMN_PHONE, user.getPhone().trim());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_TYPE, user.getUserType() != null ? user.getUserType() : User.TYPE_CUSTOMER);
        values.put(COLUMN_CREATED_AT, user.getCreatedAt());
        values.put(COLUMN_LAST_MODIFIED, user.getLastModified());

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean upsertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_FULL_NAME, user.getFullName() != null ? user.getFullName().trim() : "");
        values.put(COLUMN_EMAIL, user.getEmail().trim().toLowerCase());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone().trim() : "");
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(COLUMN_PASSWORD, user.getPassword());
        }
        values.put(COLUMN_USER_TYPE, user.getUserType() != null ? user.getUserType() : User.TYPE_CUSTOMER);
        values.put(COLUMN_CREATED_AT, user.getCreatedAt());
        values.put(COLUMN_LAST_MODIFIED, user.getLastModified());

        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{user.getEmail().trim().toLowerCase()});
        if (rowsUpdated == 0) {
            if (!values.containsKey(COLUMN_PASSWORD) || values.getAsString(COLUMN_PASSWORD) == null) {
                values.put(COLUMN_PASSWORD, "");
            }
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        }
        return true;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                int userTypeIndex = cursor.getColumnIndex(COLUMN_USER_TYPE);
                int createdIndex = cursor.getColumnIndex(COLUMN_CREATED_AT);
                int modifiedIndex = cursor.getColumnIndex(COLUMN_LAST_MODIFIED);

                User user = new User(
                        userIdIndex != -1 ? cursor.getString(userIdIndex) : "",
                        nameIndex != -1 ? cursor.getString(nameIndex) : "",
                        emailIndex != -1 ? cursor.getString(emailIndex) : "",
                        phoneIndex != -1 ? cursor.getString(phoneIndex) : "",
                        passwordIndex != -1 ? cursor.getString(passwordIndex) : "",
                        userTypeIndex != -1 ? cursor.getString(userTypeIndex) : User.TYPE_CUSTOMER,
                        createdIndex != -1 ? cursor.getLong(createdIndex) : 0,
                        modifiedIndex != -1 ? cursor.getLong(modifiedIndex) : 0
                );
                users.add(user);
            }
            cursor.close();
        }
        return users;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email.trim().toLowerCase()});
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email.trim().toLowerCase(), password});
        boolean isValid = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return isValid;
    }

    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email.trim().toLowerCase()});
        String name = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
                if (nameIndex != -1) name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return name;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email.trim().toLowerCase()});
        User user = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                int userTypeIndex = cursor.getColumnIndex(COLUMN_USER_TYPE);
                int createdIndex = cursor.getColumnIndex(COLUMN_CREATED_AT);
                int modifiedIndex = cursor.getColumnIndex(COLUMN_LAST_MODIFIED);

                user = new User(
                        userIdIndex != -1 ? cursor.getString(userIdIndex) : "",
                        nameIndex != -1 ? cursor.getString(nameIndex) : "",
                        emailIndex != -1 ? cursor.getString(emailIndex) : "",
                        phoneIndex != -1 ? cursor.getString(phoneIndex) : "",
                        passwordIndex != -1 ? cursor.getString(passwordIndex) : "",
                        userTypeIndex != -1 ? cursor.getString(userTypeIndex) : User.TYPE_CUSTOMER,
                        createdIndex != -1 ? cursor.getLong(createdIndex) : 0,
                        modifiedIndex != -1 ? cursor.getLong(modifiedIndex) : 0
                );
            }
            cursor.close();
        }
        return user;
    }

    // ==================== BRANCHES ====================
    public boolean insertBranch(Branch branch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_BRANCH_ID, branch.getBranchId());
        v.put(COLUMN_BRANCH_NAME, branch.getName());
        v.put(COLUMN_ADDRESS, branch.getAddress());
        v.put(COLUMN_LATITUDE, branch.getLatitude());
        v.put(COLUMN_LONGITUDE, branch.getLongitude());
        v.put(COLUMN_CONTACT_NUMBER, branch.getContactNumber());
        v.put(COLUMN_OPENING_HOURS, branch.getOpeningHours());
        v.put(COLUMN_LAST_MODIFIED, branch.getLastModified());

        long res = db.insertWithOnConflict(TABLE_BRANCHES, null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return res != -1;
    }

    public List<Branch> getAllBranches() {
        List<Branch> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BRANCHES, null);
        if (c != null) {
            while (c.moveToNext()) {
                list.add(new Branch(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BRANCH_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BRANCH_NAME)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_OPENING_HOURS)),
                        c.getLong(c.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
                ));
            }
            c.close();
        }
        return list;
    }

    // ==================== APPOINTMENTS ====================
    public boolean insertAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_APPOINTMENT_ID, appointment.getAppointmentId());
        v.put(COLUMN_USER_ID, appointment.getUserId());
        v.put(COLUMN_BRANCH_ID, appointment.getBranchId());
        v.put(COLUMN_SERVICE_ID, appointment.getServiceId());
        v.put(COLUMN_TECHNICIAN_ID, appointment.getTechnicianId());
        v.put(COLUMN_DEVICE_TYPE, appointment.getDeviceType() != null ? appointment.getDeviceType() : "Mobile Phone");
        v.put(COLUMN_DEVICE_BRAND, appointment.getDeviceBrand());
        v.put(COLUMN_DEVICE_MODEL, appointment.getDeviceModel());
        v.put(COLUMN_PROBLEM_DESC, appointment.getProblemDescription());
        v.put(COLUMN_PREFERRED_DATE, appointment.getPreferredDate());
        v.put(COLUMN_PREFERRED_TIME, appointment.getPreferredTime());
        v.put(COLUMN_STATUS, appointment.getStatus());
        v.put(COLUMN_CREATED_AT, appointment.getCreatedAt());
        v.put(COLUMN_LAST_MODIFIED, appointment.getLastModified());

        long res = db.insertWithOnConflict(TABLE_APPOINTMENTS, null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return res != -1;
    }

    public String getNextAppointmentId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_APPOINTMENT_ID + " FROM " + TABLE_APPOINTMENTS, null);
        int maxId = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String idStr = cursor.getString(0);
                if (idStr != null) {
                    String digitsOnly = idStr.replaceAll("\\D+", "");
                    if (!digitsOnly.isEmpty()) {
                        try {
                            int num = Integer.parseInt(digitsOnly);
                            if (num > maxId) {
                                maxId = num;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            cursor.close();
        }
        int nextId = maxId + 1;
        return String.format(Locale.US, "%02d", nextId);
    }

    public List<Appointment> getAppointmentsByUserId(String userId) {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_CREATED_AT + " DESC", new String[]{userId});
        if (c != null) {
            while (c.moveToNext()) {
                list.add(cursorToAppointment(c));
            }
            c.close();
        }
        return list;
    }

    public Appointment getAppointmentById(String appointmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_APPOINTMENT_ID + " = ?", new String[]{appointmentId});
        Appointment appt = null;
        if (c != null) {
            if (c.moveToFirst()) {
                appt = cursorToAppointment(c);
            }
            c.close();
        }
        return appt;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " ORDER BY " + COLUMN_CREATED_AT + " DESC", null);
        if (c != null) {
            while (c.moveToNext()) {
                list.add(cursorToAppointment(c));
            }
            c.close();
        }
        return list;
    }

    public List<Appointment> getAppointmentsByBranch(String branchId) {
        if (branchId == null || branchId.equalsIgnoreCase("ALL")) {
            return getAllAppointments();
        }
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_BRANCH_ID + " = ? ORDER BY " + COLUMN_CREATED_AT + " DESC", new String[]{branchId});
        if (c != null) {
            while (c.moveToNext()) {
                list.add(cursorToAppointment(c));
            }
            c.close();
        }
        return list;
    }

    public List<Technician> getAllTechnicians() {
        List<Technician> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TECHNICIANS, null);
        if (c != null && c.moveToFirst()) {
            do {
                list.add(new Technician(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TECHNICIAN_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TECH_NAME)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_SPECIALIZATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_AVAILABILITY_STATUS)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BRANCH_ID)),
                        c.getLong(c.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
                ));
            } while (c.moveToNext());
            c.close();
        }

        if (list.isEmpty()) {
            list.add(new Technician("tech_01", "Kamal Perera", "Mobile & Screen Specialist", "Available", "colombo_01", System.currentTimeMillis()));
            list.add(new Technician("tech_02", "Nimal Fernando", "Laptop & Hardware Specialist", "Available", "colombo_01", System.currentTimeMillis()));
            list.add(new Technician("tech_03", "Suneth Wickrama", "Desktop & GPU Specialist", "Available", "galle_02", System.currentTimeMillis()));
        }
        return list;
    }

    public boolean updateAppointmentDetails(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_STATUS, appointment.getStatus());
        v.put(COLUMN_TECHNICIAN_ID, appointment.getTechnicianId());
        v.put(COLUMN_BRANCH_ID, appointment.getBranchId());
        v.put(COLUMN_PROBLEM_DESC, appointment.getProblemDescription());
        v.put(COLUMN_LAST_MODIFIED, System.currentTimeMillis());
        int rows = db.update(TABLE_APPOINTMENTS, v, COLUMN_APPOINTMENT_ID + " = ?", new String[]{appointment.getAppointmentId()});
        return rows > 0;
    }

    public boolean updateAppointmentStatus(String appointmentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_STATUS, status);
        v.put(COLUMN_LAST_MODIFIED, System.currentTimeMillis());
        int rows = db.update(TABLE_APPOINTMENTS, v, COLUMN_APPOINTMENT_ID + " = ?", new String[]{appointmentId});
        return rows > 0;
    }

    private Appointment cursorToAppointment(Cursor c) {
        int devTypeIndex = c.getColumnIndex(COLUMN_DEVICE_TYPE);
        String deviceType = (devTypeIndex != -1 && !c.isNull(devTypeIndex)) ? c.getString(devTypeIndex) : "Mobile Phone";

        return new Appointment(
                c.getString(c.getColumnIndexOrThrow(COLUMN_APPOINTMENT_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_USER_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_BRANCH_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_TECHNICIAN_ID)),
                deviceType,
                c.getString(c.getColumnIndexOrThrow(COLUMN_DEVICE_BRAND)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DEVICE_MODEL)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PROBLEM_DESC)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PREFERRED_DATE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PREFERRED_TIME)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_STATUS)),
                c.getLong(c.getColumnIndexOrThrow(COLUMN_CREATED_AT)),
                c.getLong(c.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
        );
    }

    public RepairService getServiceDetails(String categoryName, String issueName) {
        String catName = categoryName != null ? categoryName.trim() : "Mobile Phone";
        String issName = issueName != null ? issueName.trim() : "Screen Replacement";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT s.* FROM " + TABLE_REPAIR_SERVICES + " s " +
                        "JOIN " + TABLE_DEVICE_CATEGORIES + " c ON s." + COLUMN_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID + " " +
                        "WHERE LOWER(c." + COLUMN_CATEGORY_NAME + ") = ? AND LOWER(s." + COLUMN_SERVICE_NAME + ") LIKE ?",
                new String[]{catName.toLowerCase(), "%" + issName.toLowerCase() + "%"}
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                RepairService service = new RepairService(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ESTIMATED_PRICE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
                );
                cursor.close();
                return service;
            }
            cursor.close();
        }

        String categoryId = "cat_mobile";
        if ("Laptop".equalsIgnoreCase(catName)) categoryId = "cat_laptop";
        else if ("Computer".equalsIgnoreCase(catName)) categoryId = "cat_computer";

        String slug = issName.toLowerCase().replaceAll("[^a-z0-9]+", "_");
        if (slug.startsWith("_")) slug = slug.substring(1);
        if (slug.endsWith("_")) slug = slug.substring(0, slug.length() - 1);
        String serviceId = "svc_" + slug;

        double estimatedPrice = 7500.0;
        String lowerIss = issName.toLowerCase();
        if (lowerIss.contains("ssd") || lowerIss.contains("hard drive") || lowerIss.contains("upgrade")) {
            estimatedPrice = 9500.0;
        } else if (lowerIss.contains("motherboard") || lowerIss.contains("power supply")) {
            estimatedPrice = 12000.0;
        } else if (lowerIss.contains("battery")) {
            estimatedPrice = 6500.0;
        } else if (lowerIss.contains("screen") || lowerIss.contains("display")) {
            estimatedPrice = 8500.0;
        }

        return new RepairService(serviceId, categoryId, issName, estimatedPrice, System.currentTimeMillis());
    }

    // ==================== PAYMENTS ====================
    public boolean insertPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_ID, payment.getPaymentId());
        values.put(COLUMN_APPOINTMENT_ID, payment.getAppointmentId());
        values.put(COLUMN_AMOUNT, payment.getAmount());
        values.put(COLUMN_PAYMENT_STATUS, payment.getPaymentStatus());
        values.put(COLUMN_PAYMENT_METHOD, payment.getPaymentMethod());
        values.put(COLUMN_PAYMENT_DATE, payment.getPaymentDate());
        values.put(COLUMN_TRANSACTION_REF, payment.getTransactionReference());
        values.put(COLUMN_LAST_MODIFIED, payment.getLastModified());

        long result = db.insertWithOnConflict(TABLE_PAYMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public Payment getPaymentById(String paymentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENT_ID + " = ?", new String[]{paymentId});
        Payment payment = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                payment = cursorToPayment(cursor);
            }
            cursor.close();
        }
        return payment;
    }

    public Payment getPaymentByAppointmentId(String appointmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_APPOINTMENT_ID + " = ?", new String[]{appointmentId});
        Payment payment = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                payment = cursorToPayment(cursor);
            }
            cursor.close();
        }
        return payment;
    }

    public boolean updatePaymentStatus(String paymentId, String status, String transactionRef) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_STATUS, status);
        if (transactionRef != null) {
            values.put(COLUMN_TRANSACTION_REF, transactionRef);
        }
        values.put(COLUMN_LAST_MODIFIED, System.currentTimeMillis());
        int rows = db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENT_ID + " = ?", new String[]{paymentId});
        return rows > 0;
    }

    private Payment cursorToPayment(Cursor cursor) {
        return new Payment(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPOINTMENT_ID)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_REF)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
        );
    }

    // ==================== DEVICE IMAGES ====================
    public boolean insertDeviceImage(DeviceImage image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_IMAGE_ID, image.getImageId());
        v.put(COLUMN_APPOINTMENT_ID, image.getAppointmentId());
        v.put(COLUMN_IMAGE_PATH, image.getImagePath());
        v.put(COLUMN_IMAGE_TYPE, image.getImageType());
        v.put(COLUMN_UPLOADED_AT, image.getUploadedAt());
        v.put(COLUMN_LAST_MODIFIED, image.getLastModified());

        long res = db.insertWithOnConflict(TABLE_DEVICE_IMAGES, null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return res != -1;
    }

    public List<DeviceImage> getImagesByAppointmentId(String appointmentId) {
        List<DeviceImage> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DEVICE_IMAGES + " WHERE " + COLUMN_APPOINTMENT_ID + " = ?", new String[]{appointmentId});
        if (c != null) {
            while (c.moveToNext()) {
                list.add(new DeviceImage(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_APPOINTMENT_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_TYPE)),
                        c.getLong(c.getColumnIndexOrThrow(COLUMN_UPLOADED_AT)),
                        c.getLong(c.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
                ));
            }
            c.close();
        }
        return list;
    }

    // ==================== SPARE PARTS & REPAIR PARTS USED ====================
    public boolean insertSparePart(SparePart part) {
        return insertOrUpdateSparePart(part);
    }

    public boolean insertOrUpdateSparePart(SparePart part) {
        if (part == null || part.getPartId() == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_PART_ID, part.getPartId());
        v.put(COLUMN_BRANCH_ID, part.getBranchId() != null ? part.getBranchId() : "colombo_01");
        v.put(COLUMN_PART_NAME, part.getPartName() != null ? part.getPartName() : "General Part");
        v.put(COLUMN_CATEGORY_ID, part.getCategory() != null ? part.getCategory() : "General");
        v.put(COLUMN_QUANTITY, part.getQuantity());
        v.put(COLUMN_PRICE, part.getPrice());
        v.put(COLUMN_LOW_STOCK_THRESHOLD, part.getLowStockThreshold());
        v.put(COLUMN_LAST_MODIFIED, System.currentTimeMillis());

        long res = db.insertWithOnConflict(TABLE_SPARE_PARTS, null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return res != -1;
    }

    public List<SparePart> getAllSpareParts() {
        List<SparePart> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SPARE_PARTS + " ORDER BY " + COLUMN_PART_NAME + " ASC", null);
        if (c != null) {
            while (c.moveToNext()) {
                list.add(cursorToSparePart(c));
            }
            c.close();
        }
        return list;
    }

    public List<SparePart> getSparePartsByBranch(String branchId) {
        if (branchId == null || branchId.equalsIgnoreCase("ALL")) {
            return getAllSpareParts();
        }
        List<SparePart> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SPARE_PARTS + " WHERE " + COLUMN_BRANCH_ID + " = ? ORDER BY " + COLUMN_PART_NAME + " ASC", new String[]{branchId});
        if (c != null) {
            while (c.moveToNext()) {
                list.add(cursorToSparePart(c));
            }
            c.close();
        }
        return list;
    }

    public boolean deleteSparePart(String partId) {
        if (partId == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_SPARE_PARTS, COLUMN_PART_ID + " = ?", new String[]{partId});
        return rows > 0;
    }

    public String getNextPartId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PART_ID + " FROM " + TABLE_SPARE_PARTS, null);
        int maxId = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String idStr = cursor.getString(0);
                if (idStr != null) {
                    String digitsOnly = idStr.replaceAll("\\D+", "");
                    if (!digitsOnly.isEmpty()) {
                        try {
                            int num = Integer.parseInt(digitsOnly);
                            if (num > maxId) {
                                maxId = num;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            cursor.close();
        }
        int nextId = maxId + 1;
        return "part_" + String.format(Locale.US, "%03d", nextId);
    }

    private SparePart cursorToSparePart(Cursor c) {
        int catIndex = c.getColumnIndex(COLUMN_CATEGORY_ID);
        String category = (catIndex != -1 && !c.isNull(catIndex)) ? c.getString(catIndex) : "General";

        return new SparePart(
                c.getString(c.getColumnIndexOrThrow(COLUMN_PART_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_BRANCH_ID)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PART_NAME)),
                category,
                c.getInt(c.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_LOW_STOCK_THRESHOLD)),
                c.getLong(c.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED))
        );
    }

    public boolean insertRepairPartUsed(RepairPartUsed partUsed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_ID, partUsed.getId());
        v.put(COLUMN_APPOINTMENT_ID, partUsed.getAppointmentId());
        v.put(COLUMN_PART_ID, partUsed.getPartId());
        v.put(COLUMN_QUANTITY_USED, partUsed.getQuantityUsed());
        v.put(COLUMN_LAST_MODIFIED, partUsed.getLastModified());

        long res = db.insertWithOnConflict(TABLE_REPAIR_PARTS_USED, null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return res != -1;
    }
}
