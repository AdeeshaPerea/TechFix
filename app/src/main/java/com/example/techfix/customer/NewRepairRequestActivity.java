package com.example.techfix.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfix.R;

public class NewRepairRequestActivity extends AppCompatActivity {

    private ImageButton btnBack, btnNotification;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_repair_request);

        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);
        btnNext = findViewById(R.id.btnNext);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                Toast.makeText(this, "Repair request submitted successfully!", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}
