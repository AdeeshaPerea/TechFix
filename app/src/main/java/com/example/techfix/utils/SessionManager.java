package com.example.techfix.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.techfix.models.User;

public class SessionManager {

    private static final String PREF_NAME = "TechFixUserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_USER_TYPE = "userType";

    public static void saveUserSession(Context context, User user) {
        if (context == null || user == null) return;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user.getUserId() != null ? user.getUserId() : "");
        editor.putString(KEY_EMAIL, user.getEmail() != null ? user.getEmail() : "");
        editor.putString(KEY_FULL_NAME, user.getFullName() != null ? user.getFullName() : "");
        editor.putString(KEY_PHONE, user.getPhone() != null ? user.getPhone() : "");
        editor.putString(KEY_USER_TYPE, user.getUserType() != null ? user.getUserType() : User.TYPE_CUSTOMER);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        if (context == null) return false;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static String getUserId(Context context) {
        if (context == null) return "";
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_ID, "");
    }

    public static String getUserEmail(Context context) {
        if (context == null) return "";
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_EMAIL, "");
    }

    public static String getUserName(Context context) {
        if (context == null) return "";
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_FULL_NAME, "");
    }

    public static String getUserType(Context context) {
        if (context == null) return User.TYPE_CUSTOMER;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_TYPE, User.TYPE_CUSTOMER);
    }

    public static User getCurrentUser(Context context) {
        if (context == null || !isLoggedIn(context)) return null;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        User user = new User();
        user.setUserId(pref.getString(KEY_USER_ID, ""));
        user.setEmail(pref.getString(KEY_EMAIL, ""));
        user.setFullName(pref.getString(KEY_FULL_NAME, ""));
        user.setPhone(pref.getString(KEY_PHONE, ""));
        user.setUserType(pref.getString(KEY_USER_TYPE, User.TYPE_CUSTOMER));
        return user;
    }

    public static void clearSession(Context context) {
        if (context == null) return;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
