package com.example.q3_sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.hardware.SensorEvent;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_acc, tv_light, tv_prox;
    SensorManager sensor_manager;
    Sensor acc_sensor, light_sensor, prox_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_acc = findViewById(R.id.tv_accel);
        tv_light = findViewById(R.id.tv_light);
        tv_prox = findViewById(R.id.tv_prox);

        sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensor_manager != null) {
            acc_sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            light_sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);
            prox_sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (acc_sensor != null) {
            sensor_manager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            tv_acc.setText("Not supported");
        }

        if (light_sensor != null) {
            sensor_manager.registerListener(this, light_sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            tv_light.setText("Not supported");
        }

        if (prox_sensor != null) {
            sensor_manager.registerListener(this, prox_sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            tv_prox.setText("Not supported");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (sensor_manager != null) {
            sensor_manager.unregisterListener(this);
        }
    }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                tv_acc.setText(String.format("X: %.2f\nY: %.2f\nZ: %.2f", event.values[0], event.values[1], event.values[2]));
            }
            else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                tv_light.setText(String.format("%.1f lx", event.values[0]));
            }
            else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                float value = event.values[0];
                tv_prox.setText(String.format("%.1f", value));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

}

