package com.example.techfix.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentRoleSelectBinding;

public class RoleSelectFragment extends Fragment {

    private FragmentRoleSelectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoleSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding.cardCustomer != null) {
            binding.cardCustomer.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(requireContext(), com.example.techfix.customer.CustomerHomeActivity.class);
                startActivity(intent);
            });
        }

        binding.cardTechnician.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roleSelect_to_techLogin);
        });

        binding.cardAdmin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_roleSelect_to_adminLogin);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
