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

    private List<BranchItem> branches = new ArrayList<>();

    public void setBranches(List<BranchItem> list) {
        this.branches = list != null ? list : new ArrayList<>();
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
        holder.bind(branches.get(position));
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {
        private final ItemBranchCardBinding binding;

        public BranchViewHolder(ItemBranchCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BranchItem branch) {
            binding.tvBranchName.setText(branch.getName());
            binding.tvTechCount.setText(branch.getTechnicianCount() + " Technicians");
            binding.tvBranchAddress.setText(branch.getAddress());
            binding.tvBranchPhoneAndHours.setText("Ph: " + branch.getPhone() + " • Hours: " + branch.getOpeningHours());
            binding.tvActiveRepairs.setText("Active Repairs: " + branch.getActiveRepairsCount());
            binding.tvCoordinates.setText("GPS: " + branch.getLatitude() + "° N, " + branch.getLongitude() + "° E");
        }
    }
}
