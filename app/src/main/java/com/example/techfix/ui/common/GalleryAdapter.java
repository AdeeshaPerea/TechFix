package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemGalleryCardBinding;
import com.example.techfix.model.GalleryItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<GalleryItem> galleryItems = new ArrayList<>();

    public void setGalleryItems(List<GalleryItem> list) {
        this.galleryItems = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGalleryCardBinding binding = ItemGalleryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new GalleryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.bind(galleryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private final ItemGalleryCardBinding binding;

        public GalleryViewHolder(ItemGalleryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GalleryItem item) {
            binding.tvGalleryTitle.setText(item.getTitle());
            binding.tvGalleryDesc.setText(item.getDescription());
            binding.tvBeforeTag.setText(item.getBeforeTag());
            binding.tvAfterTag.setText(item.getAfterTag());
        }
    }
}
