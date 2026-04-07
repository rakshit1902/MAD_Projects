package com.example.currencyapp_q1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity{
    EditText enter_amount;
    TextView result, rate_info;
    Spinner spinner_from, spinner_to;
    CardView btn_convert, btn_swap;
    Switch switchDarkMode;
    String[] currencies = {"USD", "EUR", "INR", "JPY", "GBP", "AUD"};
    double[] ratesFromINR = {0.0120, 0.0103, 1.0, 1.7958, 0.0095, 0.0163, 0.0183};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter_amount = findViewById(R.id.enter_amount);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        result = findViewById(R.id.result);
        rate_info = findViewById(R.id.rate_info);
        btn_convert = findViewById(R.id.btn_convert);
        btn_swap = findViewById(R.id.btn_swap);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        switchDarkMode.setChecked(isDark);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(0);
        spinner_to.setSelection(1);
        enter_amount.setText("1000.00");

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("dark_mode", isChecked);
                editor.apply();
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        btn_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int from_pos = spinner_from.getSelectedItemPosition();
                int to_pos = spinner_to.getSelectedItemPosition();
                spinner_from.setSelection(to_pos);
                spinner_to.setSelection(from_pos);
            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = enter_amount.getText().toString().trim();
                if (amountStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                int from_index = spinner_from.getSelectedItemPosition();
                int to_index = spinner_to.getSelectedItemPosition();

                double inr_amount = amount / rates_from_INR[from_index];
                double result = inr_amount * rates_from_INR[to_index];

                String result2 = String.format("%s%,.2f", result);
                result.set_text(result2);

                double singleUnitRate = ratesFromINR[to_index] / ratesFromINR[from_index];
                double reverseUnitRate = 1 / singleUnitRate;

                String base_curr = currencies[from_index];
                String target_curr = currencies[to_index];
            }
        });
    }
}