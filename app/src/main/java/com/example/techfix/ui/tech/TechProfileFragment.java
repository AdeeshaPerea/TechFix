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
        if (binding.txtTechName != null) binding.txtTechName.setText(tech.getName());
        if (binding.txtTechRole != null) binding.txtTechRole.setText(tech.getSpecialization() + " · " + tech.getBranchName());

        if (binding.menuEditProfile != null) {
            binding.menuEditProfile.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Edit Profile Dialog Opened (Mock)", Toast.LENGTH_SHORT).show();
            });
        }

        binding.btnLogoutTech.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
