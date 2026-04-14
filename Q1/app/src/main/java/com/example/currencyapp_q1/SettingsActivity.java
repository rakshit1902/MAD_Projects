package com.example.currencyapp_q1;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnLightMode = findViewById(R.id.btn_light_mode);
        Button btnDarkMode = findViewById(R.id.btn_dark_mode);

        btnLightMode.setOnClickListener(v ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO));

        btnDarkMode.setOnClickListener(v ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES));
    }
}