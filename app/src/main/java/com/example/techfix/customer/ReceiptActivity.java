package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.models.*;
import com.example.techfix.utils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT_ID = "extra_payment_id";
    public static final String EXTRA_REQUEST_ID = "extra_request_id";
    public static final String EXTRA_TXN_REF = "extra_txn_ref";
    public static final String EXTRA_AMOUNT = "extra_amount";
    public static final String EXTRA_METHOD = "extra_method";
    public static final String EXTRA_STATUS = "extra_status";
    public static final String EXTRA_DATE = "extra_date";

    private ImageButton btnBack;
    private TextView tvReceiptStatusTitle;
    private TextView tvReceiptStatusSubtitle;
    private TextView tvReceiptRequestId;
    private TextView tvReceiptTxnRef;
    private TextView tvReceiptMethod;
    private TextView tvReceiptStatusBadge;
    private TextView tvReceiptDate;
    private TextView tvReceiptTotalAmount;
    private Button btnBackToHome;
    private Button btnViewBookings;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        dbHelper = new DatabaseHelper(this);

        initViews();
        loadDataFromIntent();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvReceiptStatusTitle = findViewById(R.id.tvReceiptStatusTitle);
        tvReceiptStatusSubtitle = findViewById(R.id.tvReceiptStatusSubtitle);
        tvReceiptRequestId = findViewById(R.id.tvReceiptRequestId);
        tvReceiptTxnRef = findViewById(R.id.tvReceiptTxnRef);
        tvReceiptMethod = findViewById(R.id.tvReceiptMethod);
        tvReceiptStatusBadge = findViewById(R.id.tvReceiptStatusBadge);
        tvReceiptDate = findViewById(R.id.tvReceiptDate);
        tvReceiptTotalAmount = findViewById(R.id.tvReceiptTotalAmount);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnViewBookings = findViewById(R.id.btnViewBookings);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;

        String paymentId = intent.getStringExtra(EXTRA_PAYMENT_ID);
        String requestId = intent.getStringExtra(EXTRA_REQUEST_ID);
        String txnRef = intent.getStringExtra(EXTRA_TXN_REF);
        double amount = intent.getDoubleExtra(EXTRA_AMOUNT, 8500.0);
        String method = intent.getStringExtra(EXTRA_METHOD);
        String status = intent.getStringExtra(EXTRA_STATUS);
        long timestamp = intent.getLongExtra(EXTRA_DATE, System.currentTimeMillis());

        if (requestId == null || requestId.isEmpty()) {
            requestId = "#01";
        }
        if (method == null || method.isEmpty()) {
            method = Payment.METHOD_CARD;
        }
        if (status == null || status.isEmpty()) {
            status = Payment.STATUS_PAID;
        }
        if (txnRef == null || txnRef.isEmpty()) {
            txnRef = PayHereSandboxHelper.generateSandboxTxnRef();
        }

        // Format Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(timestamp));

        // Format Amount
        String formattedAmount = String.format(Locale.US, "Rs. %,.0f", amount);

        // Update UI Elements
        if (tvReceiptRequestId != null) {
            tvReceiptRequestId.setText(requestId.startsWith("#") ? requestId : "#" + requestId);
        }
        if (tvReceiptTxnRef != null) {
            tvReceiptTxnRef.setText(txnRef);
        }
        if (tvReceiptMethod != null) {
            tvReceiptMethod.setText(method.equals(Payment.METHOD_CARD) ? "PayHere (Card)" : "Cash at Branch");
        }
        if (tvReceiptStatusBadge != null) {
            tvReceiptStatusBadge.setText(status);
        }
        if (tvReceiptDate != null) {
            tvReceiptDate.setText(formattedDate);
        }
        if (tvReceiptTotalAmount != null) {
            tvReceiptTotalAmount.setText(formattedAmount);
        }

        if (status.equalsIgnoreCase(Payment.STATUS_PAID)) {
            if (tvReceiptStatusTitle != null) tvReceiptStatusTitle.setText("Payment Successful!");
            if (tvReceiptStatusSubtitle != null) tvReceiptStatusSubtitle.setText("Your repair request payment has been processed.");
            if (tvReceiptStatusBadge != null) tvReceiptStatusBadge.setText("Paid");
        } else {
            if (tvReceiptStatusTitle != null) tvReceiptStatusTitle.setText("Booking Confirmed!");
            if (tvReceiptStatusSubtitle != null) tvReceiptStatusSubtitle.setText("Payment is pending cash collection at the branch.");
            if (tvReceiptStatusBadge != null) tvReceiptStatusBadge.setText("Pending");
        }
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnBackToHome != null) {
            btnBackToHome.setOnClickListener(v -> {
                Intent intent = new Intent(ReceiptActivity.this, CustomerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (btnViewBookings != null) {
            btnViewBookings.setOnClickListener(v -> {
                Intent intent = new Intent(ReceiptActivity.this, CustomerHomeActivity.class);
                intent.putExtra("target_tab", "bookings");
                startActivity(intent);
                finish();
            });
        }
    }
}
