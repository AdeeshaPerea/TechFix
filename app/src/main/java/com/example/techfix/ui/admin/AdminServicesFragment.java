package com.example.techfix.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentAdminServicesBinding;
import com.example.techfix.model.RepairServiceItem;
import com.example.techfix.ui.common.ServiceAdapter;

import java.util.UUID;

public class AdminServicesFragment extends Fragment {

    private FragmentAdminServicesBinding binding;
    private AdminViewModel viewModel;
    private ServiceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminServicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        adapter = new ServiceAdapter(this::showEditOrDeleteServiceDialog);

        binding.rvServicesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvServicesList.setAdapter(adapter);

        viewModel.getServices().observe(getViewLifecycleOwner(), repairServiceItems -> {
            if (repairServiceItems != null) {
                adapter.setServices(repairServiceItems);
            }
        });

        binding.btnAddService.setOnClickListener(v -> showAddServiceDialog());
    }

    private void showAddServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Repair Service");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etTitle = new EditText(requireContext());
        etTitle.setHint("Service Name (e.g. Ultrasonic De-oxidation)");
        layout.addView(etTitle);

        final EditText etCategory = new EditText(requireContext());
        etCategory.setHint("Category (e.g. Mobile / Laptop)");
        layout.addView(etCategory);

        final EditText etPrice = new EditText(requireContext());
        etPrice.setHint("Price LKR (e.g. 15000)");
        etPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etPrice);

        final EditText etDuration = new EditText(requireContext());
        etDuration.setHint("Estimated Duration (e.g. 2 Hours)");
        layout.addView(etDuration);

        final EditText etDesc = new EditText(requireContext());
        etDesc.setHint("Service Description");
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(requireContext(), "Service name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = priceStr.isEmpty() ? 10000.0 : Double.parseDouble(priceStr);

            RepairServiceItem service = new RepairServiceItem(
                    "SERV_" + UUID.randomUUID().toString().substring(0, 5),
                    title,
                    category.isEmpty() ? "General Repair" : category,
                    price,
                    duration.isEmpty() ? "1 Hour" : duration,
                    desc.isEmpty() ? "Full hardware diagnostic & repair." : desc
            );

            viewModel.addService(service);
            Toast.makeText(requireContext(), "Added service " + title, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditOrDeleteServiceDialog(RepairServiceItem service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Service: " + service.getTitle());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etTitle = new EditText(requireContext());
        etTitle.setText(service.getTitle());
        layout.addView(etTitle);

        final EditText etPrice = new EditText(requireContext());
        etPrice.setText(String.valueOf(service.getPriceLkr()));
        etPrice.setHint("Price (LKR)");
        etPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etPrice);

        final EditText etDuration = new EditText(requireContext());
        etDuration.setText(service.getEstimatedDuration());
        etDuration.setHint("Estimated Duration");
        layout.addView(etDuration);

        final EditText etDesc = new EditText(requireContext());
        etDesc.setText(service.getDescription());
        etDesc.setHint("Description");
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            service.setTitle(etTitle.getText().toString().trim());

            String priceStr = etPrice.getText().toString().trim();
            if (!priceStr.isEmpty()) service.setPriceLkr(Double.parseDouble(priceStr));

            service.setEstimatedDuration(etDuration.getText().toString().trim());
            service.setDescription(etDesc.getText().toString().trim());

            viewModel.updateService(service);
            Toast.makeText(requireContext(), "Updated service details!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Delete", (dialog, which) -> {
            viewModel.deleteService(service.getId());
            Toast.makeText(requireContext(), "Service deleted!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
