package com.example.techfix.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminServicesBinding;
import com.example.techfix.model.ServiceItem;
import com.example.techfix.ui.common.ServiceAdapter;

public class AdminServicesActivity extends AppCompatActivity {

    private FragmentAdminServicesBinding binding;
    private AdminViewModel viewModel;
    private ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAdminServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        adapter = new ServiceAdapter(new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(ServiceItem item) {
                Toast.makeText(AdminServicesActivity.this, "Editing Service: " + item.getServiceName(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvServicesList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvServicesList.setAdapter(adapter);

        viewModel.getServices().observe(this, serviceItems -> {
            if (serviceItems != null) {
                adapter.setServices(serviceItems);
            }
        });

        binding.btnAddService.setOnClickListener(v -> {
            Toast.makeText(this, "Add Service Dialog Opened", Toast.LENGTH_SHORT).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
}
