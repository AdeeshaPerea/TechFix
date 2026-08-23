package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemBranchCardBinding;
import com.example.techfix.model.BranchItem;

import java.util.ArrayList;
import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    public interface OnBranchClickListener {
        void onBranchClick(BranchItem branch);
    }

    private List<BranchItem> branchList = new ArrayList<>();
    private OnBranchClickListener listener;

    public BranchAdapter() {}

    public BranchAdapter(OnBranchClickListener listener) {
        this.listener = listener;
    }

    public void setBranches(List<BranchItem> branches) {
        this.branchList = branches != null ? branches : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBranchCardBinding binding = ItemBranchCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BranchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        holder.bind(branchList.get(position));
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    class BranchViewHolder extends RecyclerView.ViewHolder {
        private final ItemBranchCardBinding binding;

        public BranchViewHolder(ItemBranchCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BranchItem branch) {
            binding.tvBranchName.setText(branch.getName());
            binding.tvBranchStatsBadge.setText(branch.getTechnicianCount() + " Techs • " + branch.getActiveRepairsCount() + " Repairs");
            binding.tvBranchAddress.setText("📍 " + branch.getAddress());
            binding.tvBranchContactAndHours.setText("📞 " + branch.getPhone() + "  •  🕒 " + branch.getOpeningHours());

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onBranchClick(branch);
            });
        }
    }
}
