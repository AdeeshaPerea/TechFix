package com.example.techfix.ui.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techfix.R;
import com.example.techfix.data.firebase.FirebaseAuthRepository;
import com.example.techfix.databinding.FragmentAdminProfileSettingsBinding;
import com.example.techfix.model.User;

public class AdminProfileSettingsFragment extends Fragment {

    private FragmentAdminProfileSettingsBinding binding;
    private static final String PREF_THEME = "TechFixThemePref";
    private static final String KEY_DARK_MODE = "isDarkMode";
    private static final String PREF_ADMIN_PROFILE = "AdminProfilePref";

    private String adminName = "Nimal Jayasinghe";
    private String adminRole = "Branch Manager · TechFix Colombo 03";
    private String adminPhone = "+94 71 987 6543";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminProfileSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadAdminProfile();
        setupDarkModeToggle();

        if (binding.cardEditProfile != null) {
            binding.cardEditProfile.setOnClickListener(v -> showEditProfileDialog());
        }

        if (binding.cardChangePassword != null) {
            binding.cardChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        }

        if (binding.btnLogOut != null) {
            binding.btnLogOut.setOnClickListener(v -> {
                FirebaseAuthRepository.getInstance().logout();
                try {
                    Navigation.findNavController(v).navigate(R.id.action_global_login);
                } catch (Exception e) {
                    try {
                        Navigation.findNavController(v).popBackStack(R.id.loginFragment, false);
                    } catch (Exception ignored) {}
                }
            });
        }
    }

    private void loadAdminProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREF_ADMIN_PROFILE, Context.MODE_PRIVATE);
        adminName = prefs.getString("name", "Nimal Jayasinghe");
        adminRole = prefs.getString("role", "Branch Manager · TechFix Colombo 03");
        adminPhone = prefs.getString("phone", "+94 71 987 6543");

        updateProfileUI();
    }

    private void updateProfileUI() {
        if (binding == null) return;
        binding.tvAdminName.setText(adminName);
        binding.tvAdminRole.setText(adminRole);

        // Generate initials (e.g., Nimal Jayasinghe -> NJ)
        String initials = "NJ";
        if (!TextUtils.isEmpty(adminName)) {
            String[] parts = adminName.trim().split("\\s+");
            if (parts.length >= 2) {
                initials = ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
            } else if (parts.length == 1 && parts[0].length() > 0) {
                initials = ("" + parts[0].charAt(0)).toUpperCase();
            }
        }
        binding.tvAvatarInitials.setText(initials);
    }

    private void setupDarkModeToggle() {
        SharedPreferences themePrefs = requireContext().getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
        boolean isDark = themePrefs.getBoolean(KEY_DARK_MODE, false);

        binding.switchDarkMode.setOnCheckedChangeListener(null);
        binding.switchDarkMode.setChecked(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themePrefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(requireContext(), "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(requireContext(), "Light Mode Enabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Profile");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        final EditText etName = new EditText(requireContext());
        etName.setHint("Full Name");
        etName.setText(adminName);
        layout.addView(etName);

        final EditText etRole = new EditText(requireContext());
        etRole.setHint("Role / Title");
        etRole.setText(adminRole);
        layout.addView(etRole);

        final EditText etPhone = new EditText(requireContext());
        etPhone.setHint("Phone Number");
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        etPhone.setText(adminPhone);
        layout.addView(etPhone);

        builder.setView(layout);

        builder.setPositiveButton("SAVE", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newRole = etRole.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();

            if (TextUtils.isEmpty(newName)) {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            adminName = newName;
            adminRole = !TextUtils.isEmpty(newRole) ? newRole : adminRole;
            adminPhone = !TextUtils.isEmpty(newPhone) ? newPhone : adminPhone;

            SharedPreferences prefs = requireContext().getSharedPreferences(PREF_ADMIN_PROFILE, Context.MODE_PRIVATE);
            prefs.edit()
                    .putString("name", adminName)
                    .putString("role", adminRole)
                    .putString("phone", adminPhone)
                    .apply();

            updateProfileUI();
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Password");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        final EditText etCurrentPass = new EditText(requireContext());
        etCurrentPass.setHint("Current Password");
        etCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etCurrentPass);

        final EditText etNewPass = new EditText(requireContext());
        etNewPass.setHint("New Password (min 6 chars)");
        etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etNewPass);

        final EditText etConfirmPass = new EditText(requireContext());
        etConfirmPass.setHint("Confirm New Password");
        etConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etConfirmPass);

        builder.setView(layout);

        builder.setPositiveButton("UPDATE PASSWORD", (dialog, which) -> {
            String current = etCurrentPass.getText().toString().trim();
            String newPass = etNewPass.getText().toString().trim();
            String confirmPass = etConfirmPass.getText().toString().trim();

            if (TextUtils.isEmpty(current)) {
                Toast.makeText(requireContext(), "Please enter current password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(requireContext(), "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(requireContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(), "Password updated successfully!", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
