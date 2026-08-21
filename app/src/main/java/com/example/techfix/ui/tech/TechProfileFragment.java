package com.example.techfix.ui.tech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.data.MockDataGenerator;
import com.example.techfix.databinding.FragmentTechProfileBinding;
import com.example.techfix.model.User;

public class TechProfileFragment extends Fragment {

    private FragmentTechProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User tech = MockDataGenerator.getMockTechnician();
        binding.tvProfileName.setText(tech.getName());
        binding.tvProfileEmail.setText(tech.getEmail());
        binding.tvProfilePhone.setText(tech.getPhone());
        binding.tvProfileSpec.setText(tech.getSpecialization());
        binding.tvProfileBranch.setText(tech.getBranchName());
        binding.tvProfileHours.setText(tech.getWorkingHours());
        binding.tvProfileActiveRepairs.setText(tech.getActiveRepairsCount() + " Active");

        binding.btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Edit Profile Dialog Opened (Mock)", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogoutTech.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.roleSelectFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
