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

        setupNavigation();
    }

    private void setupNavigation() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();

            if (destId == R.id.roleSelectFragment || destId == R.id.techLoginFragment || destId == R.id.adminLoginFragment) {
                binding.bottomNavTech.setVisibility(View.GONE);
                binding.bottomNavAdmin.setVisibility(View.GONE);
            } else if (destId == R.id.techDashboardFragment || destId == R.id.techRepairsFragment || 
                       destId == R.id.techRepairDetailFragment || destId == R.id.techDiagnosisFragment || 
                       destId == R.id.techRepairStatusFragment || destId == R.id.techRepairNotesFragment || 
                       destId == R.id.techSparePartsUsedFragment || destId == R.id.techRepairGalleryFragment || 
                       destId == R.id.techProfileFragment || destId == R.id.techNotificationsFragment) {
                binding.bottomNavTech.setVisibility(View.VISIBLE);
                binding.bottomNavAdmin.setVisibility(View.GONE);
            } else {
                binding.bottomNavTech.setVisibility(View.GONE);
                binding.bottomNavAdmin.setVisibility(View.VISIBLE);
            }
        });

        binding.bottomNavTech.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_tech_repairs) {
                navController.navigate(R.id.techRepairsFragment);
                return true;
            } else if (itemId == R.id.nav_tech_dashboard) {
                navController.navigate(R.id.techDashboardFragment);
                return true;
            } else if (itemId == R.id.nav_tech_notifications) {
                navController.navigate(R.id.techNotificationsFragment);
                return true;
            } else if (itemId == R.id.nav_tech_profile) {
                navController.navigate(R.id.techProfileFragment);
                return true;
            }
            return false;
        });

        binding.bottomNavAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_admin_dashboard) {
                navController.navigate(R.id.adminDashboardFragment);
                return true;
            } else if (itemId == R.id.nav_admin_appointments) {
                navController.navigate(R.id.adminAppointmentsFragment);
                return true;
            } else if (itemId == R.id.nav_admin_techs) {
                navController.navigate(R.id.adminTechniciansFragment);
                return true;
            } else if (itemId == R.id.nav_admin_branches) {
                navController.navigate(R.id.adminBranchesFragment);
                return true;
            } else if (itemId == R.id.nav_admin_inventory) {
                navController.navigate(R.id.adminSparePartsFragment);
                return true;
            } else if (itemId == R.id.nav_admin_profile) {
                navController.navigate(R.id.adminProfileSettingsFragment);
                return true;
            }
            return false;
        });
    }
}