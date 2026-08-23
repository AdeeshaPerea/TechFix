package com.example.techfix.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.techfix.R;
import com.example.techfix.model.RepairPhotoItem;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoPreviewDialog {

    public static void show(Context context, RepairPhotoItem photoItem) {
        if (context == null || photoItem == null) return;

        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_photo_preview, null);
        dialog.setContentView(view);

        ImageView imgFull = view.findViewById(R.id.imgFullPreview);
        ImageButton btnClose = view.findViewById(R.id.btnClosePreview);
        TextView txtType = view.findViewById(R.id.txtPreviewType);
        TextView txtUploader = view.findViewById(R.id.txtPreviewUploader);
        TextView txtDate = view.findViewById(R.id.txtPreviewDate);

        String typeStr = "BEFORE_REPAIR".equalsIgnoreCase(photoItem.getPhotoType()) ? "BEFORE REPAIR" : "AFTER REPAIR";
        txtType.setText(typeStr);

        String roleStr = photoItem.getUploadedByRole() != null ? photoItem.getUploadedByRole() : "User";
        txtUploader.setText("Uploaded by: " + roleStr);

        if (photoItem.getUploadedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault());
            txtDate.setText(sdf.format(new Date(photoItem.getUploadedAt())));
        } else {
            txtDate.setText("Uploaded recently");
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        loadImageIntoImageView(context, photoItem.getPhotoUrl(), imgFull);

        dialog.show();
    }

    public static void loadImageIntoImageView(Context context, String urlOrUri, ImageView imageView) {
        if (urlOrUri == null || imageView == null) return;

        if (urlOrUri.startsWith("content://") || urlOrUri.startsWith("file://")) {
            try {
                Uri uri = Uri.parse(urlOrUri);
                imageView.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Asynchronously load remote URL
            new Thread(() -> {
                try {
                    InputStream in = new URL(urlOrUri).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
