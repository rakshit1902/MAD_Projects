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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
public class MainActivity extends AppCompatActivity {
    private String currentPhotoPath = "";
    // Launches camera and saves to FileProvider URI
    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK)
                            Toast.makeText(this,
                                    "Image saved to: " + currentPhotoPath,
                                    Toast.LENGTH_LONG).show();
                    });
    // Launches folder picker and opens GalleryActivity
    private final ActivityResultLauncher<Intent> folderPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                String path = getPathFromUri(uri);
                                if (path != null) {
                                    Intent intent = new Intent(this,
                                            GalleryActivity.class);
                                    intent.putExtra("folderPath", path);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Could not read folder",
                                            Toast.LENGTH_SHORT).show();
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
        requestPermissions();
        btnCaptureImage.setOnClickListener(v -> openCamera());
        btnChooseFolder.setOnClickListener(v -> openFolderPicker());
    }
    private void requestPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
        else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        List<String> notGranted = new ArrayList<>();
        for (String p : permissions)
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED)
                notGranted.add(p);
        if (!notGranted.isEmpty())
            ActivityCompat.requestPermissions(
                    this, notGranted.toArray(new String[0]), 100);
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();
        Uri photoUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider",
                photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        takePictureLauncher.launch(intent);
    }
    private File createImageFile() {
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            file = File.createTempFile("IMG_" + ts + "_", ".jpg", storageDir);
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

    // For simplicity, always points to the app's Pictures directory
    private String getPathFromUri(Uri uri) {
        File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDir != null) return picturesDir.getAbsolutePath();
        return null;
    }
}