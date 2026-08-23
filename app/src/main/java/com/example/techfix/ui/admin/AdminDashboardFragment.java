package com.example.techfix.ui.admin;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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

        // Header Navigation
        if (binding.btnNotifications != null) {
            binding.btnNotifications.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminNotificationReviewsFragment));
        }

        if (binding.btnSettings != null) {
            binding.btnSettings.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminProfileSettingsFragment));
        }

        // Card Shortcuts
        if (binding.cardTotalRepairs != null) binding.cardTotalRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardPendingAppts != null) binding.cardPendingAppts.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardActiveRepairs != null) binding.cardActiveRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.cardCompletedRepairs != null) binding.cardCompletedRepairs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));

        // Quick Operation Shortcuts
        if (binding.btnQuickAppointments != null) binding.btnQuickAppointments.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));
        if (binding.btnQuickTechnicians != null) binding.btnQuickTechnicians.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminTechniciansFragment));
        if (binding.btnQuickBranches != null) binding.btnQuickBranches.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminBranchesFragment));
        if (binding.btnQuickInventory != null) binding.btnQuickInventory.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminSparePartsFragment));
        if (binding.btnViewAllAppts != null) binding.btnViewAllAppts.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.adminAppointmentsFragment));

        // Live Data Observations
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

                renderRecentActivity(appointmentItems);
            }
        });

        viewModel.getSpareParts().observe(getViewLifecycleOwner(), parts -> {
            if (parts != null && binding.lowStockContainer != null) {
                renderLowStockAlerts(parts);
            }
        });
    }

    private void renderRecentActivity(List<AppointmentItem> appointments) {
        if (binding.recentActivityContainer == null) return;
        binding.recentActivityContainer.removeAllViews();

        int limit = Math.min(appointments.size(), 3);
        for (int i = 0; i < limit; i++) {
            AppointmentItem appt = appointments.get(i);

            CardView card = new CardView(requireContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            card.setLayoutParams(cardParams);
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.techfix_white));
            card.setRadius(24f);
            card.setCardElevation(0f);

            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.VERTICAL);
            inner.setPadding(36, 30, 36, 30);

            // Row 1: Customer Name & Status Badge
            LinearLayout row1 = new LinearLayout(requireContext());
            row1.setOrientation(LinearLayout.HORIZONTAL);
            row1.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView nameTv = new TextView(requireContext());
            nameTv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            nameTv.setText(appt.getCustomerName() != null ? appt.getCustomerName() : "Customer");
            nameTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));
            nameTv.setTextSize(16f);
            nameTv.setTypeface(null, Typeface.BOLD);

            TextView statusBadge = new TextView(requireContext());
            statusBadge.setText(appt.getStatus() != null ? appt.getStatus() : "PENDING");
            String statusUpper = appt.getStatus() != null ? appt.getStatus().toUpperCase() : "";
            if (statusUpper.contains("CONFIRMED") || statusUpper.contains("COMPLETED")) {
                statusBadge.setBackgroundResource(R.drawable.bg_pill_green_solid);
            } else if (statusUpper.contains("IN_PROGRESS")) {
                statusBadge.setBackgroundResource(R.drawable.bg_pill_orange_solid);
            } else {
                statusBadge.setBackgroundResource(R.drawable.bg_pill_red_solid);
            }
            statusBadge.setTextColor(Color.WHITE);
            statusBadge.setTextSize(11f);
            statusBadge.setTypeface(null, Typeface.BOLD);
            statusBadge.setPadding(24, 8, 24, 8);

            row1.addView(nameTv);
            row1.addView(statusBadge);
            inner.addView(row1);

            // Row 2: Device Model & Issue Description
            TextView deviceTv = new TextView(requireContext());
            deviceTv.setText("📱 " + (appt.getDeviceModel() != null ? appt.getDeviceModel() : "Device") + " • " + (appt.getProblemDescription() != null ? appt.getProblemDescription() : "Repair"));
            deviceTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_dark_text));
            deviceTv.setTextSize(14f);
            deviceTv.setPadding(0, 10, 0, 0);
            inner.addView(deviceTv);

            // Row 3: Branch & Time
            TextView branchTv = new TextView(requireContext());
            branchTv.setText("📍 " + (appt.getBranchName() != null ? appt.getBranchName() : "Colombo Branch") + "  •  🕒 " + (appt.getPreferredDate() != null ? appt.getPreferredDate() : "Today"));
            branchTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_gray_text));
            branchTv.setTextSize(12f);
            branchTv.setPadding(0, 8, 0, 0);
            inner.addView(branchTv);

            card.addView(inner);

            card.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("appointmentId", appt.getId());
                Navigation.findNavController(v).navigate(R.id.action_adminDashboard_to_adminAppointmentDetail, bundle);
            });

            binding.recentActivityContainer.addView(card);
        }
    }

    private void renderLowStockAlerts(List<SparePartItem> parts) {
        if (binding.lowStockContainer == null) return;
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
                card.setRadius(24f);
                card.setCardElevation(0f);

                LinearLayout inner = new LinearLayout(requireContext());
                inner.setOrientation(LinearLayout.HORIZONTAL);
                inner.setPadding(36, 30, 36, 30);
                inner.setGravity(android.view.Gravity.CENTER_VERTICAL);

                TextView nameTv = new TextView(requireContext());
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                nameTv.setLayoutParams(nameParams);
                nameTv.setText("⚠️ " + part.getName() + "\n" + part.getCompatibleDevice());
                nameTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));
                nameTv.setTextSize(14f);
                nameTv.setTypeface(null, Typeface.BOLD);

                TextView badgeTv = new TextView(requireContext());
                badgeTv.setText(part.getQuantity() + " LEFT");
                badgeTv.setBackgroundResource(R.drawable.bg_pill_red_solid);
                badgeTv.setTextColor(Color.WHITE);
                badgeTv.setTextSize(11f);
                badgeTv.setTypeface(null, Typeface.BOLD);
                badgeTv.setPadding(24, 8, 24, 8);

                inner.addView(nameTv);
                inner.addView(badgeTv);
                card.addView(inner);
                binding.lowStockContainer.addView(card);
            }
        }

        if (lowStockCount == 0) {
            TextView emptyTv = new TextView(requireContext());
            emptyTv.setText("All spare parts are sufficiently stocked.");
            emptyTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_gray_text));
            emptyTv.setTextSize(14f);
            emptyTv.setGravity(android.view.Gravity.CENTER);
            emptyTv.setPadding(0, 16, 0, 16);
            binding.lowStockContainer.addView(emptyTv);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
