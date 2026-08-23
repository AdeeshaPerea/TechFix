package com.example.techfix.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class PayHereSandboxHelper {

    public static final String SANDBOX_MERCHANT_ID = "1222222";
    public static final String SANDBOX_MERCHANT_SECRET = "4NXXXXXX198XXXXX0245XXXXX"; // Test Sandbox Secret
    public static final String CURRENCY_LKR = "LKR";

    /**
     * Generate PayHere MD5 Hash:
     * hash = uppercase( md5( merchant_id + order_id + amount_formatted + currency + uppercase(md5(merchant_secret)) ) )
     */
    public static String generatePayHereHash(String orderId, double amount) {
        String formattedAmount = String.format(Locale.US, "%.2f", amount);
        String secretHash = getMd5(SANDBOX_MERCHANT_SECRET).toUpperCase(Locale.US);
        String rawString = SANDBOX_MERCHANT_ID + orderId + formattedAmount + CURRENCY_LKR + secretHash;
        return getMd5(rawString).toUpperCase(Locale.US);
    }

    /**
     * Generate a unique Sandbox Transaction Reference for testing
     */
    public static String generateSandboxTxnRef() {
        long timestamp = System.currentTimeMillis() % 1000000;
        int randomNum = 100 + (int)(Math.random() * 900);
        return "PAYHERE-SB-" + timestamp + randomNum;
    }

    /**
     * Generate Cash Transaction Reference
     */
    public static String generateCashTxnRef() {
        long timestamp = System.currentTimeMillis() % 1000000;
        return "CASH-BRANCH-" + timestamp;
    }

    private static String getMd5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] messageDigest = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                while (hex.length() < 2) {
                    hex = "0" + hex;
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
