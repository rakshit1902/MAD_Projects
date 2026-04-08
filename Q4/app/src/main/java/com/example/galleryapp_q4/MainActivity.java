package com.example.galleryapp_q4;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.galleryapp_q4.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    // Store the path of captured image
    private String currentPhotoPath = "";
    // Camera launcher
    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(this,
                                    "Image saved to: " + currentPhotoPath,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
// Folder picker launcher

    private final ActivityResultLauncher<Intent> folderPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                if (uri != null) {
// Convert URI to file path and open gallery
                                    String path = getPathFromUri(uri);
                                    if (path != null) {
                                        Intent intent = new Intent(this,
                                                com.example.galleryapp_q4.GalleryActivity.class);
                                        intent.putExtra("folderPath", path);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(this,
                                                "Could not read folder",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCaptureImage = findViewById(R.id.btnCaptureImage);
        Button btnChooseFolder = findViewById(R.id.btnChooseFolder);
// Request permissions when app starts
        requestPermissions();
// Capture image button
        btnCaptureImage.setOnClickListener(v -> openCamera());
// Choose folder button
        btnChooseFolder.setOnClickListener(v -> openFolderPicker());
    }
    private void requestPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        List<String> notGranted = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permission);
            }
        }
        if (!notGranted.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, notGranted.toArray(new String[0]), 100);
        }
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();
        Uri photoUri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".provider",
                photoFile
        );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        takePictureLauncher.launch(intent);
    }
    private File createImageFile() {
// Create unique filename using timestamp
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            file = File.createTempFile("IMG_" + timeStamp + "_", ".jpg",
                    storageDir);
            currentPhotoPath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        folderPickerLauncher.launch(intent);
    }
    private String getPathFromUri(Uri uri) {
// For document tree URIs, return the pictures directory path
        File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDir != null) return picturesDir.getAbsolutePath();
        return null;
    }
}
