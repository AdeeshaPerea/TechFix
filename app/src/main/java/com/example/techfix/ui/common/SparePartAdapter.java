package com.example.techfix.ui.common;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemSparePartCardBinding;
import com.example.techfix.model.SparePartItem;

import java.util.ArrayList;
import java.util.List;

public class SparePartAdapter extends RecyclerView.Adapter<SparePartAdapter.SparePartViewHolder> {

    public interface OnSparePartClickListener {
        void onPartClick(SparePartItem part);
    }

    private List<SparePartItem> parts = new ArrayList<>();
    private final OnSparePartClickListener listener;

    public SparePartAdapter(OnSparePartClickListener listener) {
        this.listener = listener;
    }

    public void setParts(List<SparePartItem> list) {
        this.parts = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SparePartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSparePartCardBinding binding = ItemSparePartCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SparePartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SparePartViewHolder holder, int position) {
        holder.bind(parts.get(position));
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class SparePartViewHolder extends RecyclerView.ViewHolder {
        private final ItemSparePartCardBinding binding;

        public SparePartViewHolder(ItemSparePartCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SparePartItem part) {
            binding.tvPartName.setText(part.getName());
            binding.tvCategoryAndDevice.setText("Category: " + part.getCategory() + " • Compatible: " + part.getCompatibleDevice());
            binding.tvStockQuantity.setText("Stock: " + part.getQuantity() + " units (Min: " + part.getMinStockThreshold() + ")");
            binding.tvUnitPrice.setText(FormatUtils.formatCurrency(part.getUnitPriceLkr()));

            String status = part.getAvailabilityStatus();
            binding.tvAvailabilityBadge.setText(status.replace("_", " "));
            binding.tvAvailabilityBadge.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getStatusBgColor(status)));
            binding.tvAvailabilityBadge.setTextColor(FormatUtils.getStatusTextColor(status));

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onPartClick(part);
            });
        }
    }
}
