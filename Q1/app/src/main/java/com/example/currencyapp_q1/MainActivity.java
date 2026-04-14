package com.example.currencyapp_q1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    EditText enter_amount;
    TextView result;
    Spinner spinner_from, spinner_to;
    CardView btn_convert, btn_settings;

    String[] currencies = {"USD", "EUR", "INR", "JPY", "GBP", "AUD"};
    double[] rates_from_INR = {0.0106723586, 0.0091449474, 1.0, 1.724137931, 0.007930843, 0.0151653018};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enter_amount = findViewById(R.id.enter_amount);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        result = findViewById(R.id.result);
        btn_convert = findViewById(R.id.btn_convert);
        btn_settings = findViewById(R.id.btn_settings);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, currencies);
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(0);
        spinner_to.setSelection(2);

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performConversion() {
        String amountStr = enter_amount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        int fromIndex = spinner_from.getSelectedItemPosition();
        int toIndex = spinner_to.getSelectedItemPosition();

        // Convert input to INR first, then to target currency [cite: 3467]
        double inrBase = amount / rates_from_INR[fromIndex];
        double converted = inrBase * rates_from_INR[toIndex];

        result.setText(String.format("%.2f", converted));
    }
}