package com.example.techfix.ui.common;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            binding.tvDeviceAndService.setText(item.getDeviceName() + " • " + item.getServiceRequested());
            binding.tvCustomerAndCode.setText(item.getCustomerName() + " • #" + item.getRepairCode());

            String status = item.getStatus();
            binding.tvStatusBadge.setText(status);
            binding.tvStatusBadge.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getStatusBgColor(status)));
            binding.tvStatusBadge.setTextColor(FormatUtils.getStatusTextColor(status));

            String est = item.getEstimatedDurationHours() > 0 ? (item.getEstimatedDurationHours() + " hrs") : "1.5 hrs";
            binding.tvDropOffAndTime.setText("Drop-off " + item.getAppointmentTime() + " • Est. " + est);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRepairClick(item);
                }
            });
        }
    }
}
