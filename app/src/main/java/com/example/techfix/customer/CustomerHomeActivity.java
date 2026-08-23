package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

import java.util.Calendar;

public class CustomerHomeActivity extends AppCompatActivity {

    private ImageButton btnBack, btnNotification;
    private TextView tvGreeting, tvUserName;
    private Button btnNewRepairRequest;
    private View cardTrackRepair, cardMyBookings, cardHistory;
    private View cardRepair1, cardRepair2, tvSeeAllRepairs;
    private View navHome, navServices, navBookings, navHistory, navProfile;

    private DatabaseHelper databaseHelper;
    private String userEmail, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_cus);

        databaseHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvUserName = findViewById(R.id.tvUserName);
        btnNewRepairRequest = findViewById(R.id.btnNewRepairRequest);
        cardTrackRepair = findViewById(R.id.cardTrackRepair);
        cardMyBookings = findViewById(R.id.cardMyBookings);
        cardHistory = findViewById(R.id.cardHistory);
        cardRepair1 = findViewById(R.id.cardRepair1);
        cardRepair2 = findViewById(R.id.cardRepair2);
        tvSeeAllRepairs = findViewById(R.id.tvSeeAllRepairs);

        navHome = findViewById(R.id.navHome);
        navServices = findViewById(R.id.navServices);
        navBookings = findViewById(R.id.navBookings);
        navHistory = findViewById(R.id.navHistory);
        navProfile = findViewById(R.id.navProfile);

        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("USER_EMAIL");
            userName = intent.getStringExtra("USER_NAME");
        }

        if (TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail)) {
            userName = databaseHelper.getUserFullName(userEmail);
        }

        if (!TextUtils.isEmpty(userName)) {
            tvUserName.setText(userName.toUpperCase());
        } else {
            tvUserName.setText("KASUN PERERA");
        }

        if (tvGreeting != null) {
            tvGreeting.setText(getTimeOfDayGreeting());
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnNewRepairRequest != null) {
            btnNewRepairRequest.setOnClickListener(v -> {
                Intent reqIntent = new Intent(CustomerHomeActivity.this, NewRepairRequestActivity.class);
                startActivity(reqIntent);
            });
        }

        if (cardTrackRepair != null) {
            cardTrackRepair.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Track Repair: iPhone 13 (#TF-2201)", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardMyBookings != null) {
            cardMyBookings.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Opening My Bookings", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardHistory != null) {
            cardHistory.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Opening Repair History", Toast.LENGTH_SHORT).show()
            );
        }

        if (tvSeeAllRepairs != null) {
            tvSeeAllRepairs.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "All Ongoing Repairs", Toast.LENGTH_SHORT).show()
            );
        }

        setupBottomNav();
    }

    private String getTimeOfDayGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 4 && hour < 12) {
            return "GOOD MORNING";
        } else if (hour >= 12 && hour < 17) {
            return "GOOD AFTERNOON";
        } else {
            return "GOOD EVENING";
        }
    }

    private void setupBottomNav() {
        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Home Tab", Toast.LENGTH_SHORT).show()
            );
        }
        if (navServices != null) {
            navServices.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Services Catalog", Toast.LENGTH_SHORT).show()
            );
        }
        if (navBookings != null) {
            navBookings.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Bookings Overview", Toast.LENGTH_SHORT).show()
            );
        }
        if (navHistory != null) {
            navHistory.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "History Overview", Toast.LENGTH_SHORT).show()
            );
        }
        if (navProfile != null) {
            navProfile.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "User Profile", Toast.LENGTH_SHORT).show()
            );
        }
    }
}
