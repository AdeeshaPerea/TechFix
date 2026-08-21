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

import com.example.techfix.databinding.FragmentTechRepairGalleryBinding;
import com.example.techfix.model.RepairItem;

public class TechRepairGalleryFragment extends Fragment {

    private FragmentTechRepairGalleryBinding binding;
    private TechViewModel viewModel;
    private String repairId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechRepairGalleryBinding.inflate(inflater, container, false);
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

        viewModel.getRepairs().observe(getViewLifecycleOwner(), repairItems -> {
            RepairItem item = viewModel.getRepairById(repairId);
            if (item != null) {
                StringBuilder beforeSb = new StringBuilder();
                for (String img : item.getBeforeImages()) {
                    beforeSb.append("• ").append(img).append("\n");
                }
                if (beforeSb.length() > 0) binding.tvBeforePhotosList.setText(beforeSb.toString().trim());

                StringBuilder afterSb = new StringBuilder();
                for (String img : item.getAfterImages()) {
                    afterSb.append("• ").append(img).append("\n");
                }
                if (afterSb.length() > 0) binding.tvAfterPhotosList.setText(afterSb.toString().trim());
            }
        });

        binding.btnAddBeforePhoto.setOnClickListener(v -> {
            viewModel.addBeforePhoto(repairId, "New Before Photo (Captured at " + System.currentTimeMillis() % 10000 + ")");
            Toast.makeText(requireContext(), "Mock Before Photo Added!", Toast.LENGTH_SHORT).show();
        });

        binding.btnAddAfterPhoto.setOnClickListener(v -> {
            viewModel.addAfterPhoto(repairId, "New After Photo (Captured at " + System.currentTimeMillis() % 10000 + ")");
            Toast.makeText(requireContext(), "Mock After Photo Added!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
