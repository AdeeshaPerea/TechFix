package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemServiceCardBinding;
import com.example.techfix.model.RepairServiceItem;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceClick(RepairServiceItem service);
    }

    private List<RepairServiceItem> services = new ArrayList<>();
    private final OnServiceClickListener listener;

    public ServiceAdapter(OnServiceClickListener listener) {
        this.listener = listener;
    }

    public void setServices(List<RepairServiceItem> list) {
        this.services = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServiceCardBinding binding = ItemServiceCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final ItemServiceCardBinding binding;

        public ServiceViewHolder(ItemServiceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RepairServiceItem service) {
            binding.tvServiceTitle.setText(service.getTitle());
            binding.tvCategoryTag.setText(service.getCategory());
            binding.tvServiceDesc.setText(service.getDescription());
            binding.tvEstDuration.setText("Est. Duration: " + service.getEstimatedDuration());
            binding.tvServicePrice.setText(FormatUtils.formatCurrency(service.getPriceLkr()));

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onServiceClick(service);
            });
        }
    }
}
