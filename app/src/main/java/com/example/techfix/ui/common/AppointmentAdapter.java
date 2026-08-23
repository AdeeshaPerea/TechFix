package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.databinding.ItemAppointmentCardBinding;
import com.example.techfix.model.AppointmentItem;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    public interface OnAppointmentActionListener {
        void onAppointmentClick(AppointmentItem item);
        void onAcceptClick(AppointmentItem item);
        void onRejectClick(AppointmentItem item);
    }

    private List<AppointmentItem> appointmentItems = new ArrayList<>();
    private final OnAppointmentActionListener listener;

    public AppointmentAdapter(OnAppointmentActionListener listener) {
        this.listener = listener;
    }

    public void setAppointmentItems(List<AppointmentItem> items) {
        this.appointmentItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentCardBinding binding = ItemAppointmentCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AppointmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.bind(appointmentItems.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentItems.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentCardBinding binding;

        public AppointmentViewHolder(ItemAppointmentCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppointmentItem item) {
            if (binding.tvAppointmentCode != null) {
                binding.tvAppointmentCode.setText("#" + (item.getAppointmentCode() != null ? item.getAppointmentCode() : "RF-1024"));
            }

            if (binding.tvDeviceModel != null) {
                binding.tvDeviceModel.setText(item.getDeviceModel() != null ? item.getDeviceModel() : "Device");
            }

            if (binding.tvDeviceAndService != null) {
                binding.tvDeviceAndService.setText(item.getServiceRequested() != null ? item.getServiceRequested() : "Repair Service");
            }

            if (binding.tvCustomerNameAndCode != null) {
                binding.tvCustomerNameAndCode.setText("👤 Customer: " + (item.getCustomerName() != null ? item.getCustomerName() : "Customer"));
            }

            if (binding.tvTechnicianName != null) {
                binding.tvTechnicianName.setText("👨‍🔧 Branch: " + (item.getBranchName() != null ? item.getBranchName() : "Colombo Branch"));
            }

            if (binding.tvBranchAndTime != null) {
                binding.tvBranchAndTime.setText(item.getPreferredDate() != null ? item.getPreferredDate() : "Aug 24, 2026");
            }

            String status = item.getStatus() != null ? item.getStatus() : "PENDING";
            String statusUpper = status.toUpperCase();

            if (binding.tvAppointmentStatus != null) {
                binding.tvAppointmentStatus.setText(status);
                if (statusUpper.contains("CONFIRMED") || statusUpper.contains("COMPLETED")) {
                    binding.tvAppointmentStatus.setBackgroundResource(R.drawable.bg_pill_green_solid);
                } else if (statusUpper.contains("PROGRESS") || statusUpper.contains("ASSIGNED") || statusUpper.contains("ACTIVE")) {
                    binding.tvAppointmentStatus.setBackgroundResource(R.drawable.bg_pill_blue_solid);
                } else if (statusUpper.contains("REJECT") || statusUpper.contains("CANCEL")) {
                    binding.tvAppointmentStatus.setBackgroundResource(R.drawable.bg_pill_red_solid);
                } else {
                    binding.tvAppointmentStatus.setBackgroundResource(R.drawable.bg_pill_orange_solid);
                }
            }

            if (binding.layoutActionButtons != null) {
                if ("PENDING".equalsIgnoreCase(status) || statusUpper.contains("NEW")) {
                    binding.layoutActionButtons.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutActionButtons.setVisibility(View.GONE);
                }
            }

            if (binding.btnAccept != null) {
                binding.btnAccept.setOnClickListener(v -> {
                    if (listener != null) listener.onAcceptClick(item);
                });
            }

            if (binding.btnReject != null) {
                binding.btnReject.setOnClickListener(v -> {
                    if (listener != null) listener.onRejectClick(item);
                });
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAppointmentClick(item);
            });
        }
    }
}
