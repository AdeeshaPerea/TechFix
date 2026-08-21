package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemTechnicianCardBinding;
import com.example.techfix.model.User;

import java.util.ArrayList;
import java.util.List;

public class TechnicianAdapter extends RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder> {

    public interface OnTechnicianActionListener {
        void onEditTech(User user);
    }

    private List<User> technicians = new ArrayList<>();
    private final OnTechnicianActionListener listener;

    public TechnicianAdapter(OnTechnicianActionListener listener) {
        this.listener = listener;
    }

    public void setTechnicians(List<User> list) {
        this.technicians = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTechnicianCardBinding binding = ItemTechnicianCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TechnicianViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianViewHolder holder, int position) {
        holder.bind(technicians.get(position));
    }

    @Override
    public int getItemCount() {
        return technicians.size();
    }

    class TechnicianViewHolder extends RecyclerView.ViewHolder {
        private final ItemTechnicianCardBinding binding;

        public TechnicianViewHolder(ItemTechnicianCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.tvTechName.setText(user.getName());
            binding.tvSpecialization.setText(user.getSpecialization());
            binding.tvBranchAndHours.setText(user.getBranchName() + " • " + user.getWorkingHours());
            binding.tvActiveRepairsBadge.setText(user.getActiveRepairsCount() + " Active");

            binding.btnEditTech.setOnClickListener(v -> {
                if (listener != null) listener.onEditTech(user);
            });
        }
    }
}
