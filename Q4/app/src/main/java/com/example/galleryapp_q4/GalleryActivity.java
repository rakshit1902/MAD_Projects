package com.example.galleryapp_q4;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.*;
public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
// Receive folder path from MainActivity
        String folderPath = getIntent().getStringExtra("folderPath");
        if (folderPath == null) {
            Toast.makeText(this, "No folder path received",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<File> imageList = new ArrayList<>();
// Filter only jpg / jpeg / png files
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.contains(".")) {
                    String ext = name.substring(
                            name.lastIndexOf(".") + 1).toLowerCase();
                    if (ext.equals("jpg") || ext.equals("jpeg")
                            || ext.equals("png"))
                        imageList.add(file);
                }
            }
        }
        if (imageList.isEmpty())
            Toast.makeText(this, "No images found in folder",
                    Toast.LENGTH_SHORT).show();
// Display images in a 3-column grid
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new ImageAdapter(this, imageList));
    }
}