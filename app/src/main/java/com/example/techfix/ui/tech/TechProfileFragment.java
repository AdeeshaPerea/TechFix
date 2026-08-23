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

        try {
            User tech = MockDataGenerator.getMockTechnician();
            if (tech != null) {
                if (binding.txtTechName != null && tech.getName() != null) {
                    binding.txtTechName.setText(tech.getName());
                }
                if (binding.txtTechRole != null) {
                    String spec = tech.getSpecialization() != null ? tech.getSpecialization() : "Technician";
                    String branch = tech.getBranchName() != null ? tech.getBranchName() : "TechFix";
                    binding.txtTechRole.setText(spec + " · " + branch);
                }
            }

            if (binding.menuEditProfile != null) {
                binding.menuEditProfile.setOnClickListener(v -> {
                    Toast.makeText(requireContext(), "Edit Profile Dialog Opened (Mock)", Toast.LENGTH_SHORT).show();
                });
            }

            if (binding.btnLogoutTech != null) {
                binding.btnLogoutTech.setOnClickListener(v -> {
                    try {
                        Navigation.findNavController(v).navigate(R.id.action_global_login);
                    } catch (Exception e) {
                        try {
                            Navigation.findNavController(v).popBackStack(R.id.loginFragment, false);
                        } catch (Exception ignored) {}
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
