package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;
import com.example.techfix.models.*;
import com.example.techfix.utils.*;

import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    public static final String EXTRA_REQUEST_ID = "extra_request_id";
    public static final String EXTRA_AMOUNT = "extra_amount";

    private ImageButton btnBack;
    private LinearLayout btnPayHereCard;
    private LinearLayout btnCashAtBranch;
    private View radioPayHere;
    private View radioCash;
    private Button btnPayWithPayhere;
    private TextView tvCostTotal;

    private String selectedPaymentMethod = Payment.METHOD_CARD;
    private String requestId = "01";
    private double totalAmount = 8500.00;

    private DatabaseHelper dbHelper;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();

        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_REQUEST_ID)) {
                requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
            }
            if (getIntent().hasExtra(EXTRA_AMOUNT)) {
                totalAmount = getIntent().getDoubleExtra(EXTRA_AMOUNT, 8500.00);
            }
        }

        initViews();
        setupPaymentMethodSelection();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPayHereCard = findViewById(R.id.btnPayHereCard);
        btnCashAtBranch = findViewById(R.id.btnCashAtBranch);

        // Child views inside payment cards
        if (btnPayHereCard != null && btnPayHereCard.getChildCount() > 0) {
            radioPayHere = btnPayHereCard.getChildAt(0);
        }
        if (btnCashAtBranch != null && btnCashAtBranch.getChildCount() > 0) {
            radioCash = btnCashAtBranch.getChildAt(0);
        }

        btnPayWithPayhere = findViewById(R.id.btnPayWithPayhere);
        tvCostTotal = findViewById(R.id.tvCostTotal);
        if (tvCostTotal != null) {
            tvCostTotal.setText(String.format(java.util.Locale.US, "Rs. %,.2f", totalAmount));
        }
    }

    private void setupPaymentMethodSelection() {
        // Default: PayHere Card selected
        selectPayHereCard();

        if (btnPayHereCard != null) {
            btnPayHereCard.setOnClickListener(v -> selectPayHereCard());
        }
        if (btnCashAtBranch != null) {
            btnCashAtBranch.setOnClickListener(v -> selectCashAtBranch());
        }
    }

    private void selectPayHereCard() {
        selectedPaymentMethod = Payment.METHOD_CARD;
        if (btnPayHereCard != null) btnPayHereCard.setBackgroundResource(R.drawable.bg_card_selected);
        if (btnCashAtBranch != null) btnCashAtBranch.setBackgroundResource(R.drawable.bg_card_white);

        if (radioPayHere != null) radioPayHere.setBackgroundResource(R.drawable.bg_radio_selected);
        if (radioCash != null) radioCash.setBackgroundResource(R.drawable.bg_radio_unselected);

        if (btnPayWithPayhere != null) btnPayWithPayhere.setText("PAY WITH PAYHERE");
    }

    private void selectCashAtBranch() {
        selectedPaymentMethod = Payment.METHOD_CASH;
        if (btnPayHereCard != null) btnPayHereCard.setBackgroundResource(R.drawable.bg_card_white);
        if (btnCashAtBranch != null) btnCashAtBranch.setBackgroundResource(R.drawable.bg_card_selected);

        if (radioPayHere != null) radioPayHere.setBackgroundResource(R.drawable.bg_radio_unselected);
        if (radioCash != null) radioCash.setBackgroundResource(R.drawable.bg_radio_selected);

        if (btnPayWithPayhere != null) btnPayWithPayhere.setText("CONFIRM CASH BOOKING");
    }

    private void setupClickListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnPayWithPayhere != null) {
            btnPayWithPayhere.setOnClickListener(v -> processPayment());
        }
    }

    private void processPayment() {
        String paymentId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();

        String status;
        String txnRef;

        if (Payment.METHOD_CARD.equals(selectedPaymentMethod)) {
            // Process via PayHere Sandbox Hash calculation
            String hash = PayHereSandboxHelper.generatePayHereHash(requestId, totalAmount);
            txnRef = PayHereSandboxHelper.generateSandboxTxnRef();
            status = Payment.STATUS_PAID;
            Log.d(TAG, "PayHere Sandbox Hash generated: " + hash + " | TxnRef: " + txnRef);
            Toast.makeText(this, "PayHere Sandbox Payment Verified!", Toast.LENGTH_SHORT).show();
        } else {
            txnRef = PayHereSandboxHelper.generateCashTxnRef();
            status = Payment.STATUS_PENDING;
            Toast.makeText(this, "Cash Booking Confirmed!", Toast.LENGTH_SHORT).show();
        }

        Payment payment = new Payment(
                paymentId,
                requestId,
                totalAmount,
                status,
                selectedPaymentMethod,
                now,
                txnRef
        );

        // 1. Save locally in SQLite
        dbHelper.insertPayment(payment);

        // 2. Sync with Firebase Firestore
        firebaseHelper.savePaymentToCloud(payment, null);

        // 3. Launch Receipt screen
        Intent receiptIntent = new Intent(PaymentActivity.this, ReceiptActivity.class);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_PAYMENT_ID, paymentId);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_REQUEST_ID, requestId);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_TXN_REF, txnRef);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_AMOUNT, totalAmount);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_METHOD, selectedPaymentMethod);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_STATUS, status);
        receiptIntent.putExtra(ReceiptActivity.EXTRA_DATE, now);

        startActivity(receiptIntent);
        finish();
    }
}
