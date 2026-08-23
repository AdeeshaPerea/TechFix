package com.example.techfix.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFixCustomer.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";

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

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(CustomerUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_FULL_NAME, user.getFullName().trim());
        values.put(COLUMN_EMAIL, user.getEmail().trim().toLowerCase());
        values.put(COLUMN_PHONE, user.getPhone().trim());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_TYPE, user.getUserType() != null ? user.getUserType() : CustomerUser.TYPE_CUSTOMER);
        values.put(COLUMN_CREATED_AT, user.getCreatedAt());
        values.put(COLUMN_LAST_MODIFIED, user.getLastModified());

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean upsertUser(CustomerUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_FULL_NAME, user.getFullName() != null ? user.getFullName().trim() : "");
        values.put(COLUMN_EMAIL, user.getEmail().trim().toLowerCase());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone().trim() : "");
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(COLUMN_PASSWORD, user.getPassword());
        }
        values.put(COLUMN_USER_TYPE, user.getUserType() != null ? user.getUserType() : CustomerUser.TYPE_CUSTOMER);
        values.put(COLUMN_CREATED_AT, user.getCreatedAt());
        values.put(COLUMN_LAST_MODIFIED, user.getLastModified());

        int rowsUpdated = db.update(TABLE_USERS, values,
                COLUMN_EMAIL + " = ?",
                new String[]{user.getEmail().trim().toLowerCase()});

        if (rowsUpdated == 0) {
            if (!values.containsKey(COLUMN_PASSWORD) || values.getAsString(COLUMN_PASSWORD) == null) {
                values.put(COLUMN_PASSWORD, "");
            }
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        }
        return true;
    }

    public List<CustomerUser> getAllUsers() {
        List<CustomerUser> users = new ArrayList<>();
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

                CustomerUser user = new CustomerUser(
                        userIdIndex != -1 ? cursor.getString(userIdIndex) : "",
                        nameIndex != -1 ? cursor.getString(nameIndex) : "",
                        emailIndex != -1 ? cursor.getString(emailIndex) : "",
                        phoneIndex != -1 ? cursor.getString(phoneIndex) : "",
                        passwordIndex != -1 ? cursor.getString(passwordIndex) : "",
                        userTypeIndex != -1 ? cursor.getString(userTypeIndex) : CustomerUser.TYPE_CUSTOMER,
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
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email.trim().toLowerCase()});
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS
                + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email.trim().toLowerCase(), password});
        boolean isValid = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }

    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email.trim().toLowerCase()});
        String name = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex);
                }
            }
            cursor.close();
        }
        return name;
    }
}
