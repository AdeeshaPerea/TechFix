package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.databinding.ItemRepairCardBinding;
import com.example.techfix.model.RepairItem;

import java.util.ArrayList;
import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairViewHolder> {

    public interface OnRepairClickListener {
        void onRepairClick(RepairItem repairItem);
    }

    private List<RepairItem> repairItems = new ArrayList<>();
    private final OnRepairClickListener listener;

    public RepairAdapter(OnRepairClickListener listener) {
        this.listener = listener;
    }

    public void setRepairItems(List<RepairItem> items) {
        this.repairItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRepairCardBinding binding = ItemRepairCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RepairViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairViewHolder holder, int position) {
        holder.bind(repairItems.get(position));
    }

    @Override
    public int getItemCount() {
        return repairItems.size();
    }

    class RepairViewHolder extends RecyclerView.ViewHolder {
        private final ItemRepairCardBinding binding;

        public RepairViewHolder(ItemRepairCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RepairItem item) {
            if (binding.tvRepairCode != null) {
                binding.tvRepairCode.setText("#" + (item.getRepairCode() != null ? item.getRepairCode() : "RF-1024"));
            }

            if (binding.tvDeviceModel != null) {
                binding.tvDeviceModel.setText(item.getDeviceName() != null ? item.getDeviceName() : "Device");
            }

            if (binding.tvDeviceAndService != null) {
                binding.tvDeviceAndService.setText(item.getServiceRequested() != null ? item.getServiceRequested() : "Repair Service");
            }

            if (binding.tvCustomerAndCode != null) {
                binding.tvCustomerAndCode.setText("👤 Customer: " + (item.getCustomerName() != null ? item.getCustomerName() : "Customer"));
            }

            String est = item.getEstimatedDurationHours() > 0 ? (item.getEstimatedDurationHours() + " hrs") : "1.5 hrs";
            if (binding.tvDropOffAndTime != null) {
                binding.tvDropOffAndTime.setText("🕒 Received: " + (item.getAppointmentTime() != null ? item.getAppointmentTime() : "Today • 10:42 AM") + " • Est. " + est);
            }

            String status = item.getStatus() != null ? item.getStatus() : "IN_PROGRESS";
            String statusUpper = status.toUpperCase();

            if (binding.tvStatusBadge != null) {
                binding.tvStatusBadge.setText(status);
                if (statusUpper.contains("COMPLETED") || statusUpper.contains("DONE")) {
                    binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_pill_green_solid);
                } else if (statusUpper.contains("PROGRESS") || statusUpper.contains("ACTIVE") || statusUpper.contains("ASSIGNED")) {
                    binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_pill_blue_solid);
                } else {
                    binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_pill_orange_solid);
                }
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRepairClick(item);
                }
            });
        }
    }
}
