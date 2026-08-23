package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.techfix.databinding.FragmentTechDiagnosisBinding;
import com.example.techfix.model.RepairItem;

public class TechDiagnosisFragment extends Fragment {

    private FragmentTechDiagnosisBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechDiagnosisBinding.inflate(inflater, container, false);
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

        RepairItem item = viewModel.getRepairById(repairId);
        if (item != null) {
            if (item.getDiagnosisSummary() != null) binding.etDiagnosisSummary.setText(item.getDiagnosisSummary());
            if (item.getProblemFound() != null) binding.etProblemFound.setText(item.getProblemFound());
            if (item.getRecommendedRepair() != null) binding.etRecommendedRepair.setText(item.getRecommendedRepair());
            if (item.getEstimatedDurationHours() > 0) binding.etEstHours.setText(String.valueOf(item.getEstimatedDurationHours()));
            if (item.getRequiredPartsNotes() != null) binding.etPartsNotes.setText(item.getRequiredPartsNotes());
        }

        binding.btnSaveDiagnosis.setOnClickListener(v -> {
            String summary = binding.etDiagnosisSummary.getText() != null ? binding.etDiagnosisSummary.getText().toString() : "";
            String problem = binding.etProblemFound.getText() != null ? binding.etProblemFound.getText().toString() : "";
            String recommended = binding.etRecommendedRepair.getText() != null ? binding.etRecommendedRepair.getText().toString() : "";
            String hoursStr = binding.etEstHours.getText() != null ? binding.etEstHours.getText().toString() : "2";
            String partsNote = binding.etPartsNotes.getText() != null ? binding.etPartsNotes.getText().toString() : "";

            int hours = 2;
            try {
                hours = Integer.parseInt(hoursStr);
            } catch (Exception ignored) {}

            viewModel.saveDiagnosis(repairId, summary, problem, recommended, hours, partsNote);
            Toast.makeText(requireContext(), "Diagnosis Saved Successfully!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });

        binding.btnCancelDiagnosis.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
