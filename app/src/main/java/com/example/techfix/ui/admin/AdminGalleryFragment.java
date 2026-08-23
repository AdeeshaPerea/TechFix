package com.example.techfix.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminGalleryBinding;
import com.example.techfix.model.GalleryItem;
import com.example.techfix.ui.common.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminGalleryFragment extends Fragment {

    private FragmentAdminGalleryBinding binding;
    private AdminViewModel viewModel;
    private GalleryAdapter adapter;

    private List<GalleryItem> allGalleryItems = new ArrayList<>();
    private String selectedCategory = "ALL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        adapter = new GalleryAdapter(this::showManageGalleryDialog);
        binding.rvGallery.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvGallery.setAdapter(adapter);

        viewModel.getGalleryItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                allGalleryItems = items;
                filterGalleryList();
            }
        });

        setupCategoryTabs();

        if (binding.btnAddGalleryItem != null) {
            binding.btnAddGalleryItem.setOnClickListener(v -> showAddGalleryDialog());
        }
    }

    private void setupCategoryTabs() {
        if (binding.tabAll != null) {
            binding.tabAll.setOnClickListener(v -> selectTab("ALL"));
        }
        if (binding.tabMobile != null) {
            binding.tabMobile.setOnClickListener(v -> selectTab("Mobile"));
        }
        if (binding.tabLaptop != null) {
            binding.tabLaptop.setOnClickListener(v -> selectTab("Laptop"));
        }
        if (binding.tabDesktop != null) {
            binding.tabDesktop.setOnClickListener(v -> selectTab("Desktop"));
        }
    }

    private void selectTab(String category) {
        selectedCategory = category;
        resetTabStyles();

        if ("ALL".equalsIgnoreCase(category) && binding.tabAll != null) {
            binding.tabAll.setBackgroundResource(R.drawable.bg_chip_selected_orange);
            binding.tabAll.setTextColor(requireContext().getColor(R.color.techfix_white));
        } else if ("Mobile".equalsIgnoreCase(category) && binding.tabMobile != null) {
            binding.tabMobile.setBackgroundResource(R.drawable.bg_chip_selected_orange);
            binding.tabMobile.setTextColor(requireContext().getColor(R.color.techfix_white));
        } else if ("Laptop".equalsIgnoreCase(category) && binding.tabLaptop != null) {
            binding.tabLaptop.setBackgroundResource(R.drawable.bg_chip_selected_orange);
            binding.tabLaptop.setTextColor(requireContext().getColor(R.color.techfix_white));
        } else if ("Desktop".equalsIgnoreCase(category) && binding.tabDesktop != null) {
            binding.tabDesktop.setBackgroundResource(R.drawable.bg_chip_selected_orange);
            binding.tabDesktop.setTextColor(requireContext().getColor(R.color.techfix_white));
        }

        filterGalleryList();
    }

    private void resetTabStyles() {
        if (binding.tabAll != null) {
            binding.tabAll.setBackground(null);
            binding.tabAll.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
        }
        if (binding.tabMobile != null) {
            binding.tabMobile.setBackground(null);
            binding.tabMobile.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
        }
        if (binding.tabLaptop != null) {
            binding.tabLaptop.setBackground(null);
            binding.tabLaptop.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
        }
        if (binding.tabDesktop != null) {
            binding.tabDesktop.setBackground(null);
            binding.tabDesktop.setTextColor(requireContext().getColor(R.color.techfix_gray_text));
        }
    }

    private void filterGalleryList() {
        if ("ALL".equalsIgnoreCase(selectedCategory)) {
            adapter.setGalleryItems(allGalleryItems);
        } else {
            List<GalleryItem> filtered = new ArrayList<>();
            for (GalleryItem item : allGalleryItems) {
                if (item.getCategory() != null && item.getCategory().equalsIgnoreCase(selectedCategory)) {
                    filtered.add(item);
                }
            }
            adapter.setGalleryItems(filtered);
        }
    }

    private void showAddGalleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Gallery Showcase");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etTitle = new EditText(requireContext());
        etTitle.setHint("Showcase Title (e.g. Shattered Screen Restored)");
        layout.addView(etTitle);

        final EditText etCategory = new EditText(requireContext());
        etCategory.setHint("Category (Mobile, Laptop, Desktop)");
        layout.addView(etCategory);

        final EditText etDesc = new EditText(requireContext());
        etDesc.setHint("Description (e.g. AMOLED display replaced to perfection)");
        layout.addView(etDesc);

        final EditText etBefore = new EditText(requireContext());
        etBefore.setHint("Before Tag (e.g. Shattered Display)");
        layout.addView(etBefore);

        final EditText etAfter = new EditText(requireContext());
        etAfter.setHint("After Tag (e.g. Pristine Screen)");
        layout.addView(etAfter);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String before = etBefore.getText().toString().trim();
            String after = etAfter.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show();
                return;
            }

            GalleryItem item = new GalleryItem(
                    "GAL_" + UUID.randomUUID().toString().substring(0, 5),
                    title,
                    category.isEmpty() ? "Mobile" : category,
                    desc.isEmpty() ? "Hardware restored to factory condition." : desc,
                    before.isEmpty() ? "Damaged" : before,
                    after.isEmpty() ? "Repaired" : after,
                    "2026-08-23"
            );

            viewModel.addGalleryItem(item);
            Toast.makeText(requireContext(), "Added gallery item!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showManageGalleryDialog(GalleryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(item.getTitle());
        builder.setMessage("Category: " + item.getCategory() + "\n" +
                "Description: " + item.getDescription() + "\n" +
                "Before: " + item.getBeforeTag() + " | After: " + item.getAfterTag());

        builder.setNegativeButton("Delete", (dialog, which) -> {
            viewModel.deleteGalleryItem(item.getId());
            Toast.makeText(requireContext(), "Removed gallery item!", Toast.LENGTH_SHORT).show();
        });

        builder.setPositiveButton("Close", null);
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
