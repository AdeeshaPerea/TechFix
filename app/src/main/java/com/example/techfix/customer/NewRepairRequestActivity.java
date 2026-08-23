package com.example.techfix.customer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.models.*;
import com.example.techfix.utils.SessionManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewRepairRequestActivity extends AppCompatActivity {

    private ImageButton btnBack, btnNotification;
    private LinearLayout btnDeviceType;
    private LinearLayout btnDeviceBrand;
    private LinearLayout btnRepairService;
    private LinearLayout btnBranch;
    private LinearLayout btnDate;
    private LinearLayout btnAddPhoto;

    private TextView tvSelectedType;
    private TextView tvSelectedBrand;
    private TextView tvSelectedService;
    private TextView tvSelectedBranch;
    private TextView tvSelectedDate;

    private EditText etDeviceModel;
    private EditText etProblemDescription;

    private Button btnTime1, btnTime2, btnTime3;
    private Button btnNext;

    private String selectedDeviceType = "Mobile Phone";
    private String selectedBrand = "Apple";
    private String selectedService = "Screen Replacement";
    private String selectedBranchId = "colombo_01";
    private String selectedDate = "27 Jul 2026";
    private String selectedTimeSlot = "11:00 AM";

    private final String[] deviceTypes = {
            "Mobile Phone",
            "Laptop",
            "Computer"
    };

    private final String[] mobileBrands = {
            "Apple (iPhone)",
            "Samsung",
            "Xiaomi / Redmi / Poco",
            "Google Pixel",
            "OnePlus",
            "Oppo / Vivo",
            "Huawei / Honor",
            "Realme",
            "Nokia",
            "Other / Custom"
    };

    private final String[] laptopBrands = {
            "Apple (MacBook)",
            "Dell",
            "HP",
            "Lenovo",
            "Asus",
            "Acer",
            "MSI",
            "Microsoft Surface",
            "Razer",
            "Samsung",
            "Other / Custom"
    };

    private final String[] computerBrands = {
            "Custom Built / DIY PC",
            "Dell / Alienware",
            "HP / Omen",
            "Lenovo",
            "Asus / ROG",
            "Acer / Predator",
            "Apple (iMac / Mac mini)",
            "MSI",
            "Gigabyte",
            "Other / Custom"
    };

    private final String[] mobileServices = {
            "Screen Replacement",
            "Battery Replacement",
            "Charging Port Repair",
            "Camera Repair",
            "Water Damage Repair",
            "Speaker / Microphone Issue",
            "Software / OS Flash"
    };

    private final String[] laptopServices = {
            "Screen / Display Replacement",
            "Battery Replacement",
            "Keyboard Replacement",
            "Charging Port / DC Jack Repair",
            "Overheating & Fan Service",
            "SSD / Hard Drive Upgrade",
            "Motherboard Repair",
            "RAM Upgrade",
            "Liquid Damage Repair"
    };

    private final String[] computerServices = {
            "Power Supply (PSU) Repair",
            "Motherboard Diagnostics & Repair",
            "Graphics Card / Display Issue",
            "SSD / Hard Drive Upgrade",
            "RAM Upgrade / Replacement",
            "OS / Software & Virus Removal",
            "Cooling System Maintenance",
            "General Hardware Upgrade"
    };

    private final String[] branchNames = {"Colombo 01 Branch", "Galle 02 Branch"};
    private final String[] branchIds = {"colombo_01", "galle_02"};

    private DatabaseHelper dbHelper;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_repair_request);

        dbHelper = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);

        btnDeviceType = findViewById(R.id.btnDeviceType);
        btnDeviceBrand = findViewById(R.id.btnDeviceBrand);
        btnRepairService = findViewById(R.id.btnRepairService);
        btnBranch = findViewById(R.id.btnBranch);
        btnDate = findViewById(R.id.btnDate);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);

        tvSelectedType = findViewById(R.id.tvSelectedType);
        tvSelectedBrand = findViewById(R.id.tvSelectedBrand);
        tvSelectedService = findViewById(R.id.tvSelectedService);
        tvSelectedBranch = findViewById(R.id.tvSelectedBranch);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        etDeviceModel = findViewById(R.id.etDeviceModel);
        etProblemDescription = findViewById(R.id.etProblemDescription);

        btnTime1 = findViewById(R.id.btnTime1);
        btnTime2 = findViewById(R.id.btnTime2);
        btnTime3 = findViewById(R.id.btnTime3);

        btnNext = findViewById(R.id.btnNext);
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());
        }

        if (btnAddPhoto != null) {
            btnAddPhoto.setOnClickListener(v ->
                    Toast.makeText(this, "Photo upload selected", Toast.LENGTH_SHORT).show());
        }

        // Device Type dropdown dialog
        if (btnDeviceType != null) {
            btnDeviceType.setOnClickListener(v -> showDeviceTypeDialog());
        }

        // Brand dropdown dialog
        if (btnDeviceBrand != null) {
            btnDeviceBrand.setOnClickListener(v -> showBrandDialog());
        }

        // Repair Service dropdown dialog
        if (btnRepairService != null) {
            btnRepairService.setOnClickListener(v -> showServiceDialog());
        }

        // Branch dropdown dialog
        if (btnBranch != null) {
            btnBranch.setOnClickListener(v -> showBranchDialog());
        }

        // Time slot selection
        if (btnTime1 != null) {
            btnTime1.setOnClickListener(v -> selectTimeSlot("9:00 AM", btnTime1, btnTime2, btnTime3));
        }
        if (btnTime2 != null) {
            btnTime2.setOnClickListener(v -> selectTimeSlot("11:00 AM", btnTime2, btnTime1, btnTime3));
        }
        if (btnTime3 != null) {
            btnTime3.setOnClickListener(v -> selectTimeSlot("2:00 PM", btnTime3, btnTime1, btnTime2));
        }

        // Date picker
        if (btnDate != null) {
            btnDate.setOnClickListener(v -> showDatePicker());
        }

        // Next button
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> submitRepairRequest());
        }
    }

    private void showDeviceTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Device Type");
        builder.setItems(deviceTypes, (dialog, which) -> {
            selectedDeviceType = deviceTypes[which];
            if (tvSelectedType != null) {
                tvSelectedType.setText(selectedDeviceType);
            }

            // Auto-reset brand options for selected device type
            String[] brands = getBrandsForType(selectedDeviceType);
            if (brands.length > 0) {
                selectedBrand = brands[0];
                if (tvSelectedBrand != null) {
                    tvSelectedBrand.setText(selectedBrand);
                }
            }

            // Auto-reset repair service dropdown options for selected device type
            String[] services = getServicesForType(selectedDeviceType);
            if (services.length > 0) {
                selectedService = services[0];
                if (tvSelectedService != null) {
                    tvSelectedService.setText(selectedService);
                }
            }
        });
        builder.show();
    }

    private void showBrandDialog() {
        String[] brands = getBrandsForType(selectedDeviceType);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Device Brand (" + selectedDeviceType + ")");
        builder.setItems(brands, (dialog, which) -> {
            selectedBrand = brands[which];
            if (tvSelectedBrand != null) {
                tvSelectedBrand.setText(selectedBrand);
            }
        });
        builder.show();
    }

    private void showServiceDialog() {
        String[] services = getServicesForType(selectedDeviceType);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Repair Service (" + selectedDeviceType + ")");
        builder.setItems(services, (dialog, which) -> {
            selectedService = services[which];
            if (tvSelectedService != null) {
                tvSelectedService.setText(selectedService);
            }
        });
        builder.show();
    }

    private String[] getBrandsForType(String type) {
        if ("Laptop".equalsIgnoreCase(type)) {
            return laptopBrands;
        } else if ("Computer".equalsIgnoreCase(type)) {
            return computerBrands;
        } else {
            return mobileBrands;
        }
    }

    private String[] getServicesForType(String type) {
        if ("Laptop".equalsIgnoreCase(type)) {
            return laptopServices;
        } else if ("Computer".equalsIgnoreCase(type)) {
            return computerServices;
        } else {
            return mobileServices;
        }
    }

    private void showBranchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Branch");
        builder.setItems(branchNames, (dialog, which) -> {
            selectedBranchId = branchIds[which];
            if (tvSelectedBranch != null) {
                tvSelectedBranch.setText(branchNames[which]);
            }
        });
        builder.show();
    }

    private void selectTimeSlot(String timeSlot, Button selectedBtn, Button other1, Button other2) {
        selectedTimeSlot = timeSlot;
        selectedBtn.setBackgroundResource(R.drawable.bg_button_navy);
        selectedBtn.setTextColor(getResources().getColor(android.R.color.white));

        other1.setBackgroundResource(R.drawable.bg_button_white_card);
        other1.setTextColor(getResources().getColor(R.color.text_secondary));

        other2.setBackgroundResource(R.drawable.bg_button_white_card);
        other2.setTextColor(getResources().getColor(R.color.text_secondary));
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(selectedYear, selectedMonth, selectedDay);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy", Locale.US);
            selectedDate = sdf.format(selected.getTime());

            if (tvSelectedDate != null) {
                tvSelectedDate.setText(selectedDate);
            }
        }, year, month, day);

        dialog.show();
    }

    private void submitRepairRequest() {
        // 1. appointment_id (PK)
        String appointmentId = dbHelper.getNextAppointmentId();

        // 2. user_id (FK → users) - Auto fetched from logged-in user
        String userId = SessionManager.getUserId(this);
        if (TextUtils.isEmpty(userId)) {
            if (getIntent() != null && getIntent().hasExtra("USER_EMAIL")) {
                String email = getIntent().getStringExtra("USER_EMAIL");
                User u = dbHelper.getUserByEmail(email);
                if (u != null) userId = u.getUserId();
            }
        }
        if (TextUtils.isEmpty(userId)) {
            List<User> users = dbHelper.getAllUsers();
            if (users != null && !users.isEmpty()) {
                userId = users.get(0).getUserId();
            } else {
                userId = "usr_kasun_01";
            }
        }

        // 3. branch_id (FK → branches)
        String branchId = selectedBranchId;

        // 4. device_type, device_brand & device_model
        String deviceType = selectedDeviceType;
        String brand = selectedBrand;
        String model = (etDeviceModel != null && !TextUtils.isEmpty(etDeviceModel.getText()))
                ? etDeviceModel.getText().toString().trim()
                : "Standard Model";

        // 5. service_id (FK → repair_services) & price
        RepairService service = dbHelper.getServiceDetails(deviceType, selectedService);
        String serviceId = (service != null && service.getServiceId() != null) ? service.getServiceId() : "svc_screen_mobile";

        // 6. technician_id (FK → technicians, nullable until admin assigns)
        String technicianId = null;

        // 7. problem_description
        String problemDescription = etProblemDescription != null ? etProblemDescription.getText().toString().trim() : "";
        if (TextUtils.isEmpty(problemDescription)) {
            problemDescription = deviceType + " - " + brand + " " + model + " (" + selectedService + ")";
        }

        // 8. preferred_date & preferred_time
        String prefDate = selectedDate;
        String prefTime = selectedTimeSlot;

        // 9. status (Default: Appointment)
        String status = Appointment.STATUS_APPOINTMENT;

        // 10. created_at & last_modified
        long now = System.currentTimeMillis();

        // Construct Appointment aligned with datatable schema including deviceType
        Appointment appointment = new Appointment(
                appointmentId,
                userId,
                branchId,
                serviceId,
                technicianId,
                deviceType,
                brand,
                model,
                problemDescription,
                prefDate,
                prefTime,
                status,
                now,
                now
        );

        // Save to local SQLite database
        dbHelper.insertAppointment(appointment);

        // Sync to Cloud Firestore database
        firebaseHelper.saveAppointmentToCloud(appointment, null);

        // Show confirmation popup and redirect to Customer Home
        new AlertDialog.Builder(this)
                .setTitle("Appointment Received")
                .setMessage("Appointment received, we will get back to you ASAP")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent homeIntent = new Intent(NewRepairRequestActivity.this, CustomerHomeActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(homeIntent);
                    finish();
                })
                .show();
    }
}
