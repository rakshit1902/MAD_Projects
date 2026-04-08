package com.example.galleryapp_q4;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.galleryapp_q4.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class ImageDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
// Get image path from GalleryActivity
        String imagePath = getIntent().getStringExtra("imagePath");
        if (imagePath == null) {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        File imageFile = new File(imagePath);

        // Connect views from XML
        ImageView detailImageView = findViewById(R.id.detailImageView);
        TextView tvImageName = findViewById(R.id.tvImageName);
        TextView tvImagePath = findViewById(R.id.tvImagePath);
        TextView tvImageSize = findViewById(R.id.tvImageSize);
        TextView tvImageDate = findViewById(R.id.tvImageDate);
        Button btnDeleteImage = findViewById(R.id.btnDeleteImage);
// Load image using Glide
        Glide.with(this).load(imageFile).centerCrop().into(detailImageView);
// Show image details
        tvImageName.setText(imageFile.getName());
        tvImagePath.setText(imageFile.getAbsolutePath());
        tvImageSize.setText((imageFile.length() / 1024) + " KB");
// Format and display date taken
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM yyyy hh:mm a", Locale.getDefault());
        tvImageDate.setText(dateFormat.format(new Date(imageFile.lastModified())));
// Delete button with confirmation dialog
        btnDeleteImage.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (imageFile.exists()) {
                            imageFile.delete();
                            Toast.makeText(this, "Image deleted",
                                    Toast.LENGTH_SHORT).show();
                            finish(); // Go back to gallery screen
                        } else {
                            Toast.makeText(this, "Image not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        });
    }
}
