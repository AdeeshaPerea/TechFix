package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemRepairPhotoBinding;
import com.example.techfix.model.RepairPhotoItem;
import com.example.techfix.data.firebase.FirestoreConstants;

import java.util.ArrayList;
import java.util.List;

public class RepairPhotoAdapter extends RecyclerView.Adapter<RepairPhotoAdapter.PhotoViewHolder> {

    public interface OnPhotoActionListener {
        void onPhotoClick(RepairPhotoItem item);
        void onRemoveClick(RepairPhotoItem item);
    }

    private List<RepairPhotoItem> photoList = new ArrayList<>();
    private final OnPhotoActionListener listener;
    private boolean allowRemoval = false;

    public RepairPhotoAdapter(OnPhotoActionListener listener) {
        this.listener = listener;
    }

    public RepairPhotoAdapter(boolean allowRemoval, OnPhotoActionListener listener) {
        this.allowRemoval = allowRemoval;
        this.listener = listener;
    }

    public void setPhotoList(List<RepairPhotoItem> photos) {
        this.photoList = photos != null ? photos : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addDraftPhoto(RepairPhotoItem item) {
        if (item != null) {
            this.photoList.add(item);
            notifyItemInserted(this.photoList.size() - 1);
        }
    }

    public void removePhoto(RepairPhotoItem item) {
        if (item != null) {
            int pos = photoList.indexOf(item);
            if (pos >= 0) {
                photoList.remove(pos);
                notifyItemRemoved(pos);
            }
        }
    }

    public List<RepairPhotoItem> getPhotoList() {
        return photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRepairPhotoBinding binding = ItemRepairPhotoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ItemRepairPhotoBinding binding;

        public PhotoViewHolder(ItemRepairPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RepairPhotoItem item) {
            if (item == null) return;

            if (FirestoreConstants.PHOTO_TYPE_BEFORE.equalsIgnoreCase(item.getPhotoType())) {
                binding.txtBadgeType.setText("BEFORE");
                binding.txtBadgeType.setBackgroundColor(0xFF2563EB); // Blue
            } else {
                binding.txtBadgeType.setText("AFTER");
                binding.txtBadgeType.setBackgroundColor(0xFF16A34A); // Green
            }

            PhotoPreviewDialog.loadImageIntoImageView(
                    itemView.getContext(),
                    item.getPhotoUrl(),
                    binding.imgThumbnail
            );

            if (allowRemoval) {
                binding.btnRemove.setVisibility(View.VISIBLE);
                binding.btnRemove.setOnClickListener(v -> {
                    if (listener != null) listener.onRemoveClick(item);
                });
            } else {
                binding.btnRemove.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onPhotoClick(item);
            });
        }
    }
}
