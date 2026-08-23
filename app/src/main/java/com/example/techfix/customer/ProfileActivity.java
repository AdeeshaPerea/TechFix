package com.example.techfix.customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.MainActivity;
import com.example.techfix.models.*;
import com.example.techfix.utils.*;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnNotification;

    private TextView tvProfileName;
    private TextView tvProfileSubtitle;
    private TextView tvStatRepairs;
    private TextView tvStatActive;
    private TextView tvStatRating;

    private LinearLayout itemEditProfile;
    private LinearLayout itemSavedAddresses;
    private LinearLayout itemNotifications;
    private LinearLayout itemPrivacy;
    private LinearLayout btnLogOut;

    private LinearLayout navHome;
    private LinearLayout navServices;
    private LinearLayout navBookings;
    private LinearLayout navHistory;
    private LinearLayout navProfile;

    private DatabaseHelper dbHelper;
    private FirebaseHelper firebaseHelper;

    private User currentUser;
    private String userEmail = "kasun.perera@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();

        if (getIntent() != null && getIntent().hasExtra("USER_EMAIL")) {
            String emailExtra = getIntent().getStringExtra("USER_EMAIL");
            if (!TextUtils.isEmpty(emailExtra)) {
                userEmail = emailExtra;
            }
        }
        if (TextUtils.isEmpty(userEmail) || "kasun.perera@gmail.com".equals(userEmail)) {
            String sessionEmail = SessionManager.getUserEmail(this);
            if (!TextUtils.isEmpty(sessionEmail)) {
                userEmail = sessionEmail;
            }
        }

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileSubtitle = findViewById(R.id.tvProfileSubtitle);
        tvStatRepairs = findViewById(R.id.tvStatRepairs);
        tvStatActive = findViewById(R.id.tvStatActive);
        tvStatRating = findViewById(R.id.tvStatRating);

        itemEditProfile = findViewById(R.id.itemEditProfile);
        itemSavedAddresses = findViewById(R.id.itemSavedAddresses);
        itemNotifications = findViewById(R.id.itemNotifications);
        itemPrivacy = findViewById(R.id.itemPrivacy);
        btnLogOut = findViewById(R.id.btnLogOut);

        navHome = findViewById(R.id.navHome);
        navServices = findViewById(R.id.navServices);
        navBookings = findViewById(R.id.navBookings);
        navHistory = findViewById(R.id.navHistory);
        navProfile = findViewById(R.id.navProfile);
    }

    private void loadUserData() {
        // Query user from SQLite database
        currentUser = dbHelper.getUserByEmail(userEmail);

        if (currentUser == null) {
            currentUser = SessionManager.getCurrentUser(this);
        }

        if (currentUser == null) {
            // Fallback default user if null
            currentUser = new User(
                    "usr_kasun_01",
                    "Kasun Perera",
                    userEmail,
                    "+94 77 987 6543",
                    "password123",
                    User.TYPE_CUSTOMER,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );
        }

        updateUI();
    }

    private void updateUI() {
        if (currentUser == null) return;

        // Name
        if (tvProfileName != null) {
            tvProfileName.setText(currentUser.getFullName().toUpperCase(Locale.US));
        }

        // Subtitle: Phone · Email
        String phoneStr = TextUtils.isEmpty(currentUser.getPhone()) ? "+94 77 987 6543" : currentUser.getPhone();
        String emailStr = currentUser.getEmail();
        if (tvProfileSubtitle != null) {
            tvProfileSubtitle.setText(String.format("%s  ·  %s", phoneStr, emailStr));
        }

        // Stats
        if (tvStatRepairs != null) tvStatRepairs.setText("12");
        if (tvStatActive != null) tvStatActive.setText("2");
        if (tvStatRating != null) tvStatRating.setText("4.9");
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        // Edit Profile Menu Item
        if (itemEditProfile != null) {
            itemEditProfile.setOnClickListener(v -> showEditProfileDialog());
        }

        if (itemSavedAddresses != null) {
            itemSavedAddresses.setOnClickListener(v ->
                    Toast.makeText(this, "Saved Addresses coming soon", Toast.LENGTH_SHORT).show()
            );
        }

        if (itemNotifications != null) {
            itemNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "Notification settings coming soon", Toast.LENGTH_SHORT).show()
            );
        }

        if (itemPrivacy != null) {
            itemPrivacy.setOnClickListener(v ->
                    Toast.makeText(this, "Privacy & Security coming soon", Toast.LENGTH_SHORT).show()
            );
        }

        // Log Out Button
        if (btnLogOut != null) {
            btnLogOut.setOnClickListener(v -> handleLogOut());
        }

        // Bottom Nav Listeners
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent homeIntent = new Intent(ProfileActivity.this, CustomerHomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                finish();
            });
        }
    }

    /**
     * Edit Profile Dialog: updates Full Name & Phone in SQLite & Firestore
     * Auto-updates last_modified timestamp on every change!
     */
    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 20);

        final EditText etName = new EditText(this);
        etName.setHint("Full Name");
        etName.setText(currentUser.getFullName());
        layout.addView(etName);

        final EditText etPhone = new EditText(this);
        etPhone.setHint("Phone Number");
        etPhone.setText(currentUser.getPhone());
        layout.addView(etPhone);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();

            if (TextUtils.isEmpty(newName)) {
                Toast.makeText(this, "Full name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // AUTO-UPDATE last_modified timestamp
            long now = System.currentTimeMillis();
            currentUser.setFullName(newName);
            currentUser.setPhone(newPhone);
            currentUser.setLastModified(now);

            // 1. Save to local SQLite
            dbHelper.upsertUser(currentUser);

            // 2. Sync to Cloud Firestore
            firebaseHelper.saveUserToCloud(currentUser, null);

            // 3. Save to SessionManager
            SessionManager.saveUserSession(this, currentUser);

            // 4. Update UI
            updateUI();

            Toast.makeText(this, "Profile updated & synced!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void handleLogOut() {
        SessionManager.clearSession(this);
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
