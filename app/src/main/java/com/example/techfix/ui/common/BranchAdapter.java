package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.data.firebase.FirebaseBranchRepository;
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
            if (branch == null) return;

            boolean isOpen = branch.isActive();
            String nameText = branch.getName() + (isOpen ? "" : " (CLOSED)");
            binding.tvBranchName.setText(nameText);

            if (!isOpen) {
                binding.tvBranchName.setTextColor(0xFFEF4444); // Red for Closed
            } else {
                binding.tvBranchName.setTextColor(itemView.getContext().getColor(R.color.navy_header));
            }

            binding.tvBranchStatsBadge.setText(branch.getTechnicianCount() + " Techs • " + branch.getActiveRepairsCount() + " Repairs");
            binding.tvBranchAddress.setText("📍 " + branch.getAddress());
            binding.tvBranchContactAndHours.setText("📞 " + branch.getPhone() + "  •  🕒 " + (isOpen ? branch.getOpeningHours() : "CLOSED"));

            // Clear listener before setChecked to prevent recursive invocation
            binding.switchBranch.setOnCheckedChangeListener(null);
            binding.switchBranch.setChecked(isOpen);

            // Handle Admin switch toggle
            binding.switchBranch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                branch.setActive(isChecked);
                binding.tvBranchName.setText(branch.getName() + (isChecked ? "" : " (CLOSED)"));
                binding.tvBranchName.setTextColor(isChecked ?
                        itemView.getContext().getColor(R.color.navy_header) :
                        0xFFEF4444);
                binding.tvBranchContactAndHours.setText("📞 " + branch.getPhone() + "  •  🕒 " + (isChecked ? branch.getOpeningHours() : "CLOSED"));

                FirebaseBranchRepository.getInstance().updateBranch(branch, null);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onBranchClick(branch);
            });
        }
    }
}
