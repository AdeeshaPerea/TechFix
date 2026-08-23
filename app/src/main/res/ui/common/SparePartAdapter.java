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

    public interface OnPartClickListener {
        void onPartClick(SparePartItem part);
    }

    private List<SparePartItem> partList = new ArrayList<>();
    private OnPartClickListener listener;

    public SparePartAdapter() {}

    public SparePartAdapter(OnPartClickListener listener) {
        this.listener = listener;
    }

    public void setParts(List<SparePartItem> parts) {
        this.partList = parts != null ? parts : new ArrayList<>();
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
        holder.bind(partList.get(position));
    }

    @Override
    public int getItemCount() {
        return partList.size();
    }

    class SparePartViewHolder extends RecyclerView.ViewHolder {
        private final ItemSparePartCardBinding binding;

        public SparePartViewHolder(ItemSparePartCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SparePartItem part) {
            binding.tvPartName.setText(part.getName());
            binding.tvPartCategoryAndModel.setText(part.getCategory() + "  •  " + part.getCompatibleDevice());
            binding.tvPartQuantity.setText("Stock: " + part.getQuantity() + " Units (Min: " + part.getMinStockThreshold() + ")");
            binding.tvPartPrice.setText(FormatUtils.formatCurrency(part.getUnitPriceLkr()));

            String status = part.getAvailabilityStatus();
            if (status == null) status = "AVAILABLE";
            binding.tvStockStatusBadge.setText(status.replace("_", " "));
            binding.tvStockStatusBadge.setBackgroundTintList(ColorStateList.valueOf(FormatUtils.getStatusBgColor(status)));
            binding.tvStockStatusBadge.setTextColor(FormatUtils.getStatusTextColor(status));

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onPartClick(part);
            });
        }
    }
}
