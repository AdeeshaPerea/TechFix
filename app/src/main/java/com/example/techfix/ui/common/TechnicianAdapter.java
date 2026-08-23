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

    public interface OnTechClickListener {
        void onTechClick(User user);
    }

    private List<User> techList = new ArrayList<>();
    private OnTechClickListener listener;

    public TechnicianAdapter() {}

    public TechnicianAdapter(OnTechClickListener listener) {
        this.listener = listener;
    }

    public void setTechnicians(List<User> technicians) {
        this.techList = technicians != null ? technicians : new ArrayList<>();
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
        holder.bind(techList.get(position));
    }

    @Override
    public int getItemCount() {
        return techList.size();
    }

    class TechnicianViewHolder extends RecyclerView.ViewHolder {
        private final ItemTechnicianCardBinding binding;

        public TechnicianViewHolder(ItemTechnicianCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.tvTechName.setText(user.getName());
            binding.tvTechSpecialization.setText(user.getSpecialization());
            binding.tvTechBranchAndPhone.setText(user.getBranchName() + "  •  " + user.getPhone());
            binding.tvTechActiveJobsBadge.setText(user.getActiveRepairsCount() + " Active Jobs");

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTechClick(user);
            });
        }
    }
}
