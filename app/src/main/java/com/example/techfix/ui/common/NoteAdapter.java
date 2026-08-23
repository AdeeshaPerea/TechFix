package com.example.techfix.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.databinding.ItemRepairNoteBinding;
import com.example.techfix.model.RepairNoteItem;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<RepairNoteItem> notes = new ArrayList<>();

    public void setNotes(List<RepairNoteItem> list) {
        this.notes = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRepairNoteBinding binding = ItemRepairNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemRepairNoteBinding binding;

        public NoteViewHolder(ItemRepairNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RepairNoteItem note) {
            binding.tvAuthorAndCategory.setText(note.getAuthorName() + " • " + note.getCategory());
            binding.tvNoteText.setText(note.getNoteText());
            binding.tvTimestamp.setText(note.getTimestamp());
        }
    }
}
