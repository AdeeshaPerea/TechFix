package com.example.techfix.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminDashboardBinding;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.model.SparePartItem;

import java.util.List;

public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;
    private AdminViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnSettings != null) {
            binding.btnSettings.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.adminProfileSettingsFragment);
            });
        }

        if (binding.cardTotalRepairs != null) binding.cardTotalRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardPendingAppts != null) binding.cardPendingAppts.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardActiveRepairs != null) binding.cardActiveRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardCompletedRepairs != null) binding.cardCompletedRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));

        viewModel.getAppointments().observe(getViewLifecycleOwner(), appointmentItems -> {
            if (appointmentItems != null) {
                int totalRepairs = appointmentItems.size();
                int pendingAppts = 0;
                int activeRepairs = 0;
                int completedRepairs = 0;

                for (AppointmentItem appt : appointmentItems) {
                    String status = appt.getStatus() != null ? appt.getStatus().toUpperCase() : "";
                    if (status.contains("PENDING")) {
                        pendingAppts++;
                    } else if (status.contains("IN_PROGRESS") || status.contains("ACTIVE") || status.contains("CONFIRMED") || status.contains("ASSIGNED")) {
                        activeRepairs++;
                    } else if (status.contains("COMPLETED") || status.contains("DONE")) {
                        completedRepairs++;
                    } else {
                        activeRepairs++;
                    }
                }

                if (binding.tvTotalRepairs != null) binding.tvTotalRepairs.setText(String.valueOf(totalRepairs));
                if (binding.tvPendingAppointments != null) binding.tvPendingAppointments.setText(String.valueOf(pendingAppts));
                if (binding.tvActiveRepairs != null) binding.tvActiveRepairs.setText(String.valueOf(activeRepairs));
                if (binding.tvCompletedRepairs != null) binding.tvCompletedRepairs.setText(String.valueOf(completedRepairs));
            }
        });

        viewModel.getSpareParts().observe(getViewLifecycleOwner(), parts -> {
            if (parts != null && binding.lowStockContainer != null) {
                renderLowStockAlerts(parts);
            }
        });
    }

    private void renderLowStockAlerts(List<SparePartItem> parts) {
        binding.lowStockContainer.removeAllViews();

        int lowStockCount = 0;
        for (SparePartItem part : parts) {
            if (part.getQuantity() <= part.getMinStockThreshold() || part.getQuantity() <= 5) {
                lowStockCount++;
                CardView card = new CardView(requireContext());
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, 16);
                card.setLayoutParams(cardParams);
                card.setCardBackgroundColor(Color.WHITE);
                card.setRadius(32f);
                card.setCardElevation(0f);

                LinearLayout inner = new LinearLayout(requireContext());
                inner.setOrientation(LinearLayout.HORIZONTAL);
                inner.setPadding(36, 36, 36, 36);
                inner.setGravity(android.view.Gravity.CENTER_VERTICAL);

                TextView nameTv = new TextView(requireContext());
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                nameTv.setLayoutParams(nameParams);
                nameTv.setText(part.getName() + " — " + part.getCompatibleDevice());
                nameTv.setTextColor(Color.parseColor("#192841"));
                nameTv.setTextSize(16f);
                nameTv.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView badgeTv = new TextView(requireContext());
                badgeTv.setText(part.getQuantity() + " LEFT");
                badgeTv.setBackgroundResource(R.drawable.bg_pill_red);
                badgeTv.setTextColor(Color.parseColor("#DC2626"));
                badgeTv.setTextSize(12f);
                badgeTv.setTypeface(null, android.graphics.Typeface.BOLD);
                badgeTv.setPadding(28, 12, 28, 12);

                inner.addView(nameTv);
                inner.addView(badgeTv);
                card.addView(inner);
                binding.lowStockContainer.addView(card);
            }
        }

        if (lowStockCount == 0) {
            TextView emptyTv = new TextView(requireContext());
            emptyTv.setText("All spare parts are sufficiently stocked.");
            emptyTv.setTextColor(Color.parseColor("#64748B"));
            emptyTv.setTextSize(14f);
            emptyTv.setGravity(android.view.Gravity.CENTER);
            emptyTv.setPadding(0, 20, 0, 20);
            binding.lowStockContainer.addView(emptyTv);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
