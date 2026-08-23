package com.example.techfix.ui.common;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            binding.tvCustomerNameAndCode.setText(item.getCustomerName() + " • #" + item.getAppointmentCode());
            binding.tvDeviceAndService.setText(item.getDeviceModel() + " • " + item.getServiceRequested());
            binding.tvBranchAndTime.setText(item.getBranchName() + " • " + item.getPreferredDate() + " (" + item.getPreferredTime() + ")");


            String status = item.getStatus();
            binding.tvAppointmentStatus.setText(status);
            binding.tvAppointmentStatus.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getStatusBgColor(status)));
            binding.tvAppointmentStatus.setTextColor(FormatUtils.getStatusTextColor(status));

            if ("PENDING".equalsIgnoreCase(status)) {
                binding.btnAccept.setVisibility(View.VISIBLE);
                binding.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnReject.setVisibility(View.GONE);
            }

            binding.btnAccept.setOnClickListener(v -> {
                if (listener != null) listener.onAcceptClick(item);
            });

            binding.btnReject.setOnClickListener(v -> {
                if (listener != null) listener.onRejectClick(item);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAppointmentClick(item);
            });
        }
    }
}
