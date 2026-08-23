package com.example.techfix.ui.admin;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.techfix.R;
import com.example.techfix.databinding.FragmentAdminNotificatonReviewsBinding;

public class AdminNotificationReviewsFragment extends Fragment {

    private FragmentAdminNotificatonReviewsBinding binding;
    private boolean isReviewsTabSelected = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminNotificatonReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (binding.btnTabNotifications != null) {
            binding.btnTabNotifications.setOnClickListener(v -> selectTab(false));
        }

        if (binding.btnTabReviews != null) {
            binding.btnTabReviews.setOnClickListener(v -> selectTab(true));
        }

        selectTab(true);
    }

    private void selectTab(boolean isReviews) {
        isReviewsTabSelected = isReviews;

        if (isReviews) {
            binding.btnTabReviews.setBackgroundResource(R.drawable.bg_button_orange);
            binding.btnTabReviews.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_white));

            binding.btnTabNotifications.setBackgroundResource(R.drawable.bg_button_white_card);
            binding.btnTabNotifications.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));

            renderReviewsList();
        } else {
            binding.btnTabNotifications.setBackgroundResource(R.drawable.bg_button_orange);
            binding.btnTabNotifications.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_white));

            binding.btnTabReviews.setBackgroundResource(R.drawable.bg_button_white_card);
            binding.btnTabReviews.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));

            renderNotificationsList();
        }
    }

    private void renderReviewsList() {
        if (binding.reviewListContainer == null) return;
        binding.reviewListContainer.removeAllViews();

        addReviewCard("KP", "Kasun Perera", "Published", "★★★★★", "Technician: Nuwan Silva", "Excellent and fast screen replacement service! Returned my laptop in 1 day.", R.drawable.bg_pill_green_solid);
        addReviewCard("AF", "Amali Fernando", "Flagged", "★★☆☆☆", "Technician: Chamara Perera", "Repair took longer than promised at Galle branch.", R.drawable.bg_pill_red_solid);
        addReviewCard("DF", "Dilan Fernando", "Published", "★★★★☆", "Technician: Nuwan Silva", "Good work, friendly staff at Colombo branch.", R.drawable.bg_pill_green_solid);
        addReviewCard("IG", "Ishara Gunasekara", "Pending Review", "★☆☆☆☆", "Technician: Ravindu Jayasuriya", "Battery still drains fast after repair. Needs re-inspection.", R.drawable.bg_pill_orange_solid);
    }

    private void addReviewCard(String initials, String customerName, String status, String rating, String techName, String comment, int statusBgDrawable) {
        CardView card = new CardView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.techfix_white));
        card.setRadius(24f);
        card.setCardElevation(0f);

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // Header Row: Avatar + Name + Status
        LinearLayout headerRow = new LinearLayout(requireContext());
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView avatarTv = new TextView(requireContext());
        avatarTv.setText(initials);
        avatarTv.setTextColor(Color.WHITE);
        avatarTv.setTextSize(14f);
        avatarTv.setTypeface(null, Typeface.BOLD);
        avatarTv.setGravity(android.view.Gravity.CENTER);
        avatarTv.setBackgroundResource(R.drawable.bg_avatar_navy);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(96, 96);
        avatarParams.setMargins(0, 0, 24, 0);
        avatarTv.setLayoutParams(avatarParams);

        LinearLayout nameCol = new LinearLayout(requireContext());
        nameCol.setOrientation(LinearLayout.VERTICAL);
        nameCol.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        TextView nameTv = new TextView(requireContext());
        nameTv.setText(customerName);
        nameTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));
        nameTv.setTextSize(16f);
        nameTv.setTypeface(null, Typeface.BOLD);

        TextView ratingTv = new TextView(requireContext());
        ratingTv.setText(rating);
        ratingTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_orange));
        ratingTv.setTextSize(14f);
        ratingTv.setPadding(0, 4, 0, 0);

        nameCol.addView(nameTv);
        nameCol.addView(ratingTv);

        TextView statusTv = new TextView(requireContext());
        statusTv.setText(status);
        statusTv.setBackgroundResource(statusBgDrawable);
        statusTv.setTextColor(Color.WHITE);
        statusTv.setTextSize(11f);
        statusTv.setTypeface(null, Typeface.BOLD);
        statusTv.setPadding(24, 8, 24, 8);

        headerRow.addView(avatarTv);
        headerRow.addView(nameCol);
        headerRow.addView(statusTv);
        layout.addView(headerRow);

        // Technician details
        TextView techTv = new TextView(requireContext());
        techTv.setText("🔧 " + techName);
        techTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_gray_text));
        techTv.setTextSize(13f);
        techTv.setPadding(0, 16, 0, 0);
        layout.addView(techTv);

        // Comment
        TextView commentTv = new TextView(requireContext());
        commentTv.setText("\"" + comment + "\"");
        commentTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_dark_text));
        commentTv.setTextSize(14f);
        commentTv.setTypeface(null, Typeface.ITALIC);
        commentTv.setPadding(0, 8, 0, 0);
        layout.addView(commentTv);

        // Moderation Action Buttons (Approve / Flag)
        LinearLayout actionRow = new LinearLayout(requireContext());
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        actionRow.setPadding(0, 20, 0, 0);

        Button btnApprove = new Button(requireContext());
        LinearLayout.LayoutParams btnParams1 = new LinearLayout.LayoutParams(0, 84, 1.0f);
        btnParams1.setMargins(0, 0, 12, 0);
        btnApprove.setLayoutParams(btnParams1);
        btnApprove.setBackgroundResource(R.drawable.bg_button_navy);
        btnApprove.setText("✓ Approve");
        btnApprove.setTextColor(Color.WHITE);
        btnApprove.setTextSize(12f);
        btnApprove.setTypeface(null, Typeface.BOLD);
        btnApprove.setOnClickListener(v -> Toast.makeText(requireContext(), "Review Approved & Published for " + customerName, Toast.LENGTH_SHORT).show());

        Button btnFlag = new Button(requireContext());
        LinearLayout.LayoutParams btnParams2 = new LinearLayout.LayoutParams(0, 84, 1.0f);
        btnFlag.setLayoutParams(btnParams2);
        btnFlag.setBackgroundResource(R.drawable.bg_button_outline);
        btnFlag.setText("🚩 Flag Review");
        btnFlag.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));
        btnFlag.setTextSize(12f);
        btnFlag.setTypeface(null, Typeface.BOLD);
        btnFlag.setOnClickListener(v -> Toast.makeText(requireContext(), "Review Flagged for " + customerName, Toast.LENGTH_SHORT).show());

        actionRow.addView(btnApprove);
        actionRow.addView(btnFlag);
        layout.addView(actionRow);

        card.addView(layout);
        binding.reviewListContainer.addView(card);
    }

    private void renderNotificationsList() {
        if (binding.reviewListContainer == null) return;
        binding.reviewListContainer.removeAllViews();

        addNotificationCard("New Appointment Request", "Anura Kumara requested Screen Replacement for MacBook Air M2.", "10 mins ago", R.drawable.bg_pill_blue);
        addNotificationCard("Low Stock Alert", "Dell XPS 15 Battery Pack (PART_006) stock dropped below threshold (3 LEFT).", "45 mins ago", R.drawable.bg_pill_red_solid);
        addNotificationCard("New Review Moderation", "Ishara Gunasekara submitted a 1-star review requiring admin review.", "2 hours ago", R.drawable.bg_pill_orange_solid);
        addNotificationCard("Repair Job Completed", "Nuwan Silva completed Repair #REP_001 (iPhone 13 Screen Replacement).", "3 hours ago", R.drawable.bg_pill_green_solid);
    }

    private void addNotificationCard(String title, String message, String time, int badgeDrawable) {
        CardView card = new CardView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.techfix_white));
        card.setRadius(24f);
        card.setCardElevation(0f);

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        LinearLayout headerRow = new LinearLayout(requireContext());
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView titleTv = new TextView(requireContext());
        titleTv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        titleTv.setText("🔔 " + title);
        titleTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_navy));
        titleTv.setTextSize(16f);
        titleTv.setTypeface(null, Typeface.BOLD);

        TextView timeTv = new TextView(requireContext());
        timeTv.setText(time);
        timeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_gray_text));
        timeTv.setTextSize(12f);

        headerRow.addView(titleTv);
        headerRow.addView(timeTv);
        layout.addView(headerRow);

        TextView msgTv = new TextView(requireContext());
        msgTv.setText(message);
        msgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.techfix_dark_text));
        msgTv.setTextSize(14f);
        msgTv.setPadding(0, 12, 0, 0);
        layout.addView(msgTv);

        card.addView(layout);
        binding.reviewListContainer.addView(card);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
