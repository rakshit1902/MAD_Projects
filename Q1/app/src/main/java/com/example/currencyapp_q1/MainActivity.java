package com.example.currencyapp_q1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    EditText enter_amount;
    TextView result;
    Spinner spinner_from, spinner_to;
    CardView btn_convert, btn_swap;
    Switch switchDarkMode;

    String[] currencies = {"USD", "EUR", "INR", "JPY", "GBP", "AUD"};
    double[] rates_from_INR = {0.0120, 0.0103, 1.0, 1.7958, 0.0095, 0.0163};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enter_amount = findViewById(R.id.enter_amount);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        result = findViewById(R.id.result);
        btn_convert = findViewById(R.id.btn_convert);
        btn_swap = findViewById(R.id.btn_swap);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if (view != null) {
                    ((TextView) view).setTextColor(getColor(R.color.text_main));
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {}
        });

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if (view != null) {
                    ((TextView) view).setTextColor(getColor(R.color.text_main));
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {}
        });

        spinner_from.post(new Runnable() {
            @Override
            public void run() {
                View view = spinner_from.getSelectedView();
                if (view != null) {
                    ((TextView) view).setTextColor(getColor(R.color.text_main));
                }
            }
        });

        spinner_to.post(new Runnable() {
            @Override
            public void run() {
                View view = spinner_to.getSelectedView();
                if (view != null) {
                    ((TextView) view).setTextColor(getColor(R.color.text_main));
                }
            }
        });

        spinner_from.setSelection(0);
        spinner_to.setSelection(1);

        enter_amount.setText("0.00");

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkMode.setChecked(true);
        } else {
            switchDarkMode.setChecked(false);
        }

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

                performConversion();
            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    private void performConversion() {
        String amountStr = enter_amount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        int from_index = spinner_from.getSelectedItemPosition();
        int to_index = spinner_to.getSelectedItemPosition();

        double inr_amount = amount / rates_from_INR[from_index];
        double calculatedMath = inr_amount * rates_from_INR[to_index];

        String resultString = String.format("%,.2f", calculatedMath);
        result.setText(resultString);
    }
}