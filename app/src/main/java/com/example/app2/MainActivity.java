package com.example.app2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView caloriesTextView;
    private ProgressBar progressBar;
    private TextView stepCountTargetTextView;
    private Spinner modeSpinner;
    private Spinner weightSpinner;
    private EditText weightEditText;
    private Button resetButton;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private int stepCount = 0;
    private int initialStepCount = -1;
    private double stepLengthInMeters = 0.75;
    private int stepCountTarget = 8000;
    private double totalCaloriesBurned = 0.0;
    private String selectedMode = "Chůze";
    private int weight = 70; // Default weight

    @Override
    protected void onStop() {
        super.onStop();
        if (stepCountSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCountSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCountTextView = findViewById(R.id.stepCountTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        stepCountTargetTextView = findViewById(R.id.KrokovyCilTextView);
        progressBar = findViewById(R.id.progressBar);
        modeSpinner = findViewById(R.id.modeSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);
        resetButton = findViewById(R.id.resetButton);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepCountTextView.setText("Počet kroků: " + stepCount);
        progressBar.setMax(stepCountTarget);
        progressBar.setProgress(stepCount);

        stepCountTargetTextView.setText("Váš cílový počet kroků: " + stepCountTarget);

        if (stepCountSensor == null) {
            stepCountTextView.setText("Senzor pro snímání kroků není k dispozici");
        }

        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode = parentView.getItemAtPosition(position).toString();
                calculateCalories(); // Recalculate calories when mode changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedMode = "Chůze";
                calculateCalories();
            }
        });

        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_array, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedWeight = parentView.getItemAtPosition(position).toString();
                weight = Integer.parseInt(selectedWeight.replaceAll("[^0-9]", ""));
                calculateCalories(); // Recalculate calories when weight changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                weight = 70; // Default weight
                calculateCalories();
            }
        });

        resetButton.setOnClickListener(view -> resetStepCount());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int currentStepCount = (int) sensorEvent.values[0];

            if (initialStepCount == -1) {
                initialStepCount = currentStepCount;
            }

            stepCount = currentStepCount - initialStepCount;

            stepCountTextView.setText("Počet kroků: " + stepCount);
            progressBar.setProgress(stepCount);

            if (stepCount >= stepCountTarget) {
                stepCountTargetTextView.setText("Váš cílový počet kroků byl dosažen");
            }

            double distanceInKm = stepCount * stepLengthInMeters / 1000;
            distanceTextView.setText(String.format(Locale.getDefault(), "Vzdálenost v km: %.2f", distanceInKm));

            calculateCalories();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void resetStepCount() {
        initialStepCount = -1;
        stepCount = 0;
        totalCaloriesBurned = 0.0;

        stepCountTextView.setText("Počet kroků: " + stepCount);
        progressBar.setProgress(stepCount);
        distanceTextView.setText("Vzdálenost v km: 0.00");
        stepCountTargetTextView.setText("Váš cílový počet kroků: " + stepCountTarget);
        caloriesTextView.setText("Kalorie: 0.0");
    }

    private void calculateCalories() {
        double metValue = selectedMode.equals("Chůze") ? 3.5 : 7.0; // MET value for walking and running
        double weightInKg = weight;
        double caloriesPerMinute = (metValue * 3.5 * weightInKg) / 200;
        double minutes = stepCount / (selectedMode.equals("Chůze") ? 100 : 150);
        totalCaloriesBurned += caloriesPerMinute * minutes;

        caloriesTextView.setText(String.format(Locale.getDefault(), "Kalorie: %.2f", totalCaloriesBurned));
    }
}
