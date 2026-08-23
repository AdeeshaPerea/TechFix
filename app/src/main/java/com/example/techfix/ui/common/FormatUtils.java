package com.example.techfix.ui.common;

import android.graphics.Color;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
        String formatted = formatter.format(amount);
        // Clean up format to match standard Sri Lankan display: Rs. 25,000
        return "Rs. " + String.format(Locale.US, "%,.0f", amount);
    }

    public static int getPriorityColor(String priority) {
        if (priority == null) return Color.parseColor("#64748B");
        switch (priority.toUpperCase()) {
            case "URGENT":
                return Color.parseColor("#DC2626"); // Red
            case "HIGH":
                return Color.parseColor("#EF4444"); // Crimson
            case "MEDIUM":
                return Color.parseColor("#F59E0B"); // Amber
            case "LOW":
                return Color.parseColor("#3B82F6"); // Blue
            default:
                return Color.parseColor("#64748B");
        }
    }

    public static int getStatusBgColor(String status) {
        if (status == null) return Color.parseColor("#F1F5F9");
        switch (status.toUpperCase()) {
            case "COMPLETED":
                return Color.parseColor("#D1FAE5"); // Light Mint Green
            case "REPAIRING":
            case "QUALITY CHECK":
                return Color.parseColor("#DBEAFE"); // Soft Blue
            case "DIAGNOSING":
                return Color.parseColor("#FEF3C7"); // Soft Amber
            case "WAITING FOR PARTS":
                return Color.parseColor("#FFEDD5"); // Soft Orange
            case "REJECTED":
                return Color.parseColor("#FEE2E2"); // Soft Red
            default:
                return Color.parseColor("#F1F5F9"); // Soft Slate
        }
    }

    public static int getStatusTextColor(String status) {
        if (status == null) return Color.parseColor("#475569");
        switch (status.toUpperCase()) {
            case "COMPLETED":
                return Color.parseColor("#065F46"); // Dark Emerald Green
            case "REPAIRING":
            case "QUALITY CHECK":
                return Color.parseColor("#1E40AF"); // Dark Blue
            case "DIAGNOSING":
                return Color.parseColor("#92400E"); // Dark Amber
            case "WAITING FOR PARTS":
                return Color.parseColor("#9A3412"); // Dark Orange
            case "REJECTED":
                return Color.parseColor("#991B1B"); // Dark Red
            default:
                return Color.parseColor("#475569");
        }
    }
}
