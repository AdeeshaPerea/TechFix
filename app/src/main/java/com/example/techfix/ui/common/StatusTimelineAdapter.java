package com.example.techfix.ui.common;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemStatusStepBinding;

import java.util.ArrayList;
import java.util.List;

public class StatusTimelineAdapter extends RecyclerView.Adapter<StatusTimelineAdapter.TimelineViewHolder> {

    public interface OnStatusClickListener {
        void onSetStatus(String status);
    }

    public static class StatusStep {
        public String title;
        public String desc;
        public boolean isCurrent;
        public boolean isCompleted;

        public StatusStep(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }
    }

    private final List<StatusStep> steps = new ArrayList<>();
    private String currentStatus;
    private final OnStatusClickListener listener;

    public StatusTimelineAdapter(String currentStatus, OnStatusClickListener listener) {
        this.currentStatus = currentStatus;
        this.listener = listener;
        initSteps();
    }

    private void initSteps() {
        steps.add(new StatusStep("BOOKED", "Appointment booked online or counter."));
        steps.add(new StatusStep("CONFIRMED", "Service slot confirmed at branch."));
        steps.add(new StatusStep("RECEIVED", "Device physically handed over."));
        steps.add(new StatusStep("DIAGNOSING", "Technician evaluating hardware faults."));
        steps.add(new StatusStep("WAITING FOR PARTS", "Sourcing replacement components."));
        steps.add(new StatusStep("REPAIRING", "Active repair & component assembly."));
        steps.add(new StatusStep("QUALITY CHECK", "Post-repair hardware & touch testing."));
        steps.add(new StatusStep("READY FOR COLLECTION", "Repairs done. Customer notified for pickup."));
        steps.add(new StatusStep("COMPLETED", "Payment completed & device picked up."));
        updateStepStates();
    }

    public void setCurrentStatus(String status) {
        this.currentStatus = status;
        updateStepStates();
        notifyDataSetChanged();
    }

    private void updateStepStates() {
        boolean passedCurrent = false;
        for (StatusStep step : steps) {
            if (step.title.equalsIgnoreCase(currentStatus)) {
                step.isCurrent = true;
                step.isCompleted = false;
                passedCurrent = true;
            } else if (!passedCurrent) {
                step.isCurrent = false;
                step.isCompleted = true;
            } else {
                step.isCurrent = false;
                step.isCompleted = false;
            }
        }
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStatusStepBinding binding = ItemStatusStepBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TimelineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.bind(steps.get(position), position + 1, position == steps.size() - 1);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder {
        private final ItemStatusStepBinding binding;

        public TimelineViewHolder(ItemStatusStepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StatusStep step, int index, boolean isLast) {
            binding.tvStepNumber.setText(String.valueOf(index));
            binding.tvStepTitle.setText(step.title);
            binding.tvStepDesc.setText(step.desc);

            if (isLast) {
                binding.viewLineConnector.setVisibility(View.GONE);
            } else {
                binding.viewLineConnector.setVisibility(View.VISIBLE);
            }

            if (step.isCurrent) {
                binding.tvStepNumber.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2563EB")));
                binding.tvStepNumber.setTextColor(Color.WHITE);
                binding.tvStepBadge.setVisibility(View.VISIBLE);
                binding.tvStepBadge.setText("CURRENT");
                binding.tvStepBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DBEAFE")));
                binding.tvStepBadge.setTextColor(Color.parseColor("#1E40AF"));
                binding.cardStatusStep.setStrokeColor(Color.parseColor("#2563EB"));
                binding.cardStatusStep.setStrokeWidth(3);
                binding.btnSetStatus.setVisibility(View.GONE);
            } else if (step.isCompleted) {
                binding.tvStepNumber.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#059669")));
                binding.tvStepNumber.setText("✓");
                binding.tvStepNumber.setTextColor(Color.WHITE);
                binding.tvStepBadge.setVisibility(View.VISIBLE);
                binding.tvStepBadge.setText("DONE");
                binding.tvStepBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D1FAE5")));
                binding.tvStepBadge.setTextColor(Color.parseColor("#065F46"));
                binding.cardStatusStep.setStrokeColor(Color.parseColor("#E2E8F0"));
                binding.cardStatusStep.setStrokeWidth(1);
                binding.btnSetStatus.setVisibility(View.GONE);
            } else {
                binding.tvStepNumber.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBD5E1")));
                binding.tvStepNumber.setTextColor(Color.parseColor("#475569"));
                binding.tvStepBadge.setVisibility(View.GONE);
                binding.cardStatusStep.setStrokeColor(Color.parseColor("#E2E8F0"));
                binding.cardStatusStep.setStrokeWidth(1);
                binding.btnSetStatus.setVisibility(View.VISIBLE);
                binding.btnSetStatus.setOnClickListener(v -> {
                    if (listener != null) listener.onSetStatus(step.title);
                });
            }
        }
    }
}
