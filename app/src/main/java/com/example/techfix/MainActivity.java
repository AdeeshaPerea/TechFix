package com.example.techfix;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.techfix.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        binding.topAppToolbar.setNavigationOnClickListener(v -> {
            if (navController != null) {
                navController.navigateUp();
            }
        });

        setupNavigation();
    }

    private void setupNavigation() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();

            if (destId == R.id.loginFragment) {
                binding.topAppToolbar.setVisibility(View.GONE);
                binding.bottomNavTech.setVisibility(View.GONE);
                binding.bottomNavAdmin.setVisibility(View.GONE);
                if (binding.navDivider != null) binding.navDivider.setVisibility(View.GONE);
                return;
            }

            boolean isTechDestination = (destId == R.id.techRepairsFragment || destId == R.id.techDashboardFragment ||
                    destId == R.id.techRepairDetailFragment || destId == R.id.techDiagnosisFragment ||
                    destId == R.id.techRepairStatusFragment || destId == R.id.techRepairNotesFragment ||
                    destId == R.id.techSparePartsUsedFragment || destId == R.id.techRepairGalleryFragment ||
                    destId == R.id.techProfileFragment || destId == R.id.techNotificationsFragment);

            binding.topAppToolbar.setVisibility(View.GONE);

            if (isTechDestination) {
                binding.bottomNavTech.setVisibility(View.VISIBLE);
                binding.bottomNavAdmin.setVisibility(View.GONE);
                if (binding.navDivider != null) binding.navDivider.setVisibility(View.VISIBLE);

                // Update tech active item
                if (destId == R.id.techDashboardFragment) binding.bottomNavTech.getMenu().findItem(R.id.nav_tech_dashboard).setChecked(true);
                else if (destId == R.id.techRepairsFragment) binding.bottomNavTech.getMenu().findItem(R.id.nav_tech_repairs).setChecked(true);
                else if (destId == R.id.techNotificationsFragment) binding.bottomNavTech.getMenu().findItem(R.id.nav_tech_notifications).setChecked(true);
                else if (destId == R.id.techProfileFragment) binding.bottomNavTech.getMenu().findItem(R.id.nav_tech_profile).setChecked(true);
            } else {
                binding.bottomNavTech.setVisibility(View.GONE);
                binding.bottomNavAdmin.setVisibility(View.VISIBLE);
                if (binding.navDivider != null) binding.navDivider.setVisibility(View.VISIBLE);

                // Update admin active item
                if (destId == R.id.adminDashboardFragment) binding.bottomNavAdmin.getMenu().findItem(R.id.nav_admin_dashboard).setChecked(true);
                else if (destId == R.id.adminAppointmentsFragment) binding.bottomNavAdmin.getMenu().findItem(R.id.nav_admin_appointments).setChecked(true);
                else if (destId == R.id.adminTechniciansFragment) binding.bottomNavAdmin.getMenu().findItem(R.id.nav_admin_techs).setChecked(true);
                else if (destId == R.id.adminSparePartsFragment) binding.bottomNavAdmin.getMenu().findItem(R.id.nav_admin_inventory).setChecked(true);
                else if (destId == R.id.adminProfileSettingsFragment) binding.bottomNavAdmin.getMenu().findItem(R.id.nav_admin_profile).setChecked(true);
            }
        });

        binding.bottomNavTech.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (navController != null && navController.getCurrentDestination() != null) {
                int currentId = navController.getCurrentDestination().getId();
                try {
                    if (itemId == R.id.nav_tech_dashboard) {
                        if (currentId != R.id.techDashboardFragment) navController.navigate(R.id.techDashboardFragment);
                        return true;
                    } else if (itemId == R.id.nav_tech_repairs) {
                        if (currentId != R.id.techRepairsFragment) navController.navigate(R.id.techRepairsFragment);
                        return true;
                    } else if (itemId == R.id.nav_tech_notifications) {
                        if (currentId != R.id.techNotificationsFragment) navController.navigate(R.id.techNotificationsFragment);
                        return true;
                    } else if (itemId == R.id.nav_tech_profile) {
                        if (currentId != R.id.techProfileFragment) navController.navigate(R.id.techProfileFragment);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        });

        binding.bottomNavAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (navController != null && navController.getCurrentDestination() != null) {
                int currentId = navController.getCurrentDestination().getId();
                try {
                    if (itemId == R.id.nav_admin_dashboard) {
                        if (currentId != R.id.adminDashboardFragment) navController.navigate(R.id.adminDashboardFragment);
                        return true;
                    } else if (itemId == R.id.nav_admin_appointments) {
                        if (currentId != R.id.adminAppointmentsFragment) navController.navigate(R.id.adminAppointmentsFragment);
                        return true;
                    } else if (itemId == R.id.nav_admin_techs) {
                        if (currentId != R.id.adminTechniciansFragment) navController.navigate(R.id.adminTechniciansFragment);
                        return true;
                    } else if (itemId == R.id.nav_admin_inventory) {
                        if (currentId != R.id.adminSparePartsFragment) navController.navigate(R.id.adminSparePartsFragment);
                        return true;
                    } else if (itemId == R.id.nav_admin_profile) {
                        if (currentId != R.id.adminProfileSettingsFragment) navController.navigate(R.id.adminProfileSettingsFragment);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        });
    }
}