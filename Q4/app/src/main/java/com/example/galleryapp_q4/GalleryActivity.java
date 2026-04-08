package com.example.galleryapp_q4;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp_q4.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        // Get folder path from MainActivity
        String folderPath = getIntent().getStringExtra("folderPath");
        if (folderPath == null) {
            Toast.makeText(this, "No folder path received",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        File folder = new File(folderPath);
// Get all image files from folder
        File[] files = folder.listFiles();
        List<File> imageList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
// Check file has an extension before calling lastIndexOf
                if (name.contains(".")) {
                    String extension = name.substring(
                            name.lastIndexOf(".") + 1).toLowerCase();
                    if (extension.equals("jpg") || extension.equals("jpeg")
                            || extension.equals("png")) {
                        imageList.add(file);
                    }
                }
            }
        }
        if (imageList.isEmpty()) {
            Toast.makeText(this, "No images found in folder",
                    Toast.LENGTH_SHORT).show();
        }
// Setup RecyclerView with grid of 3 columns
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new com.example.galleryapp_q4.ImageAdapter(this, imageList));
    }
}
