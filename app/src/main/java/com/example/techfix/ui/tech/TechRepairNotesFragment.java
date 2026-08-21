package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfix.databinding.FragmentTechRepairNotesBinding;
import com.example.techfix.model.RepairItem;
import com.example.techfix.ui.common.NoteAdapter;

public class TechRepairNotesFragment extends Fragment {

    private FragmentTechRepairNotesBinding binding;
    private TechViewModel viewModel;
    private NoteAdapter adapter;
    private String repairId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TechViewModel.class);

        if (getArguments() != null) {
            repairId = getArguments().getString("repairId", "REP_001");
        } else {
            repairId = "REP_001";
        }

        adapter = new NoteAdapter();
        binding.rvNotesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotesList.setAdapter(adapter);

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null && item.getNotes() != null) {
                adapter.setNotes(item.getNotes());
            }
        });

        binding.btnAddNoteConfirm.setOnClickListener(v -> {
            String noteText = binding.etNewNote.getText() != null ? binding.etNewNote.getText().toString().trim() : "";
            if (TextUtils.isEmpty(noteText)) {
                Toast.makeText(requireContext(), "Please enter note text", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addNote(repairId, "Alex Perera", "WORK_PERFORMED", noteText);
            binding.etNewNote.setText("");
            Toast.makeText(requireContext(), "Note added!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
