package com.example.app2;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager; // Import for keeping the screen on
import android.content.pm.ActivityInfo; // Import for screen orientation

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements StepCounter.StepListener, ConfirmationDialog.ConfirmationDialogListener {

    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView caloriesTextView;
    private TextView timerTextView;
    private ProgressBar progressBar;
    private TextView stepCountTargetTextView;
    private Spinner modeSpinner;
    private Spinner weightSpinner;

    private StepCounter stepCounter;
    private Timer timer;
    private StepDataStorage stepDataStorage;

    private int stepCountTarget = 8000;
    private String selectedMode;
    private int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Set screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        stepDataStorage = new StepDataStorage(this);
        initializeUI();
        setupSpinners();
        setupButtons();
        loadSpinnerValues();
    }

    private void initializeUI() {
        stepCountTextView = findViewById(R.id.stepCountTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        timerTextView = findViewById(R.id.timerTextView);
        stepCountTargetTextView = findViewById(R.id.KrokovyCilTextView);
        progressBar = findViewById(R.id.progressBar);
        modeSpinner = findViewById(R.id.modeSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounter = new StepCounter(this, sensorManager, stepDataStorage);
        timer = new Timer(timerTextView, findViewById(R.id.startPauseButton), stepDataStorage);

        progressBar.setMax(stepCountTarget);
        progressBar.setProgress(0);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode = parentView.getItemAtPosition(position).toString();
                stepDataStorage.saveSelectedMode(selectedMode);
                stepCounter.calculateCalories(selectedMode, stepDataStorage.loadSelectedWeight());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
                stepDataStorage.saveSelectedWeight(weight);
                stepCounter.calculateCalories(stepDataStorage.loadSelectedMode(), weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setupButtons() {
        findViewById(R.id.resetButton).setOnClickListener(v -> {
            ConfirmationDialog dialog = new ConfirmationDialog();
            dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
        });

        findViewById(R.id.startPauseButton).setOnClickListener(v -> timer.toggleTimer());
    }

    private void loadSpinnerValues() {
        setSpinnerSelection(modeSpinner, stepDataStorage.loadSelectedMode());
        setSpinnerSelection(weightSpinner, stepDataStorage.loadSelectedWeight() + " kg");
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onConfirm() {
        resetStepCount();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Reset canceled", Toast.LENGTH_SHORT).show();
    }

    private void resetStepCount() {
        stepCounter.reset();
        timer.reset();
        stepCountTextView.setText("Počet kroků: 0");
        progressBar.setProgress(0);
        distanceTextView.setText("Vzdálenost v km: 0.00");
        caloriesTextView.setText("Kalorie: 0.0");
        stepCountTargetTextView.setText("Váš cílový počet kroků: " + stepCountTarget);
        Toast.makeText(this, "Kroky resetovány", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepCountUpdated(int stepCount, double distanceInKm, double totalCaloriesBurned) {
        stepCountTextView.setText("Počet kroků: " + stepCount);
        int progress = calculateProgress(stepCount);
        progressBar.setProgress(progress);
        updateDistanceTextView(distanceInKm);
        updateCaloriesTextView(totalCaloriesBurned);
    }

    private int calculateProgress(int stepCount) {
        return (int) ((double) stepCount / stepCountTarget * progressBar.getMax());
    }

    private void updateDistanceTextView(double distanceInKm) {
        distanceTextView.setText(String.format(Locale.getDefault(), "Vzdálenost v km: %.2f", distanceInKm));
    }

    private void updateCaloriesTextView(double totalCaloriesBurned) {
        caloriesTextView.setText(String.format(Locale.getDefault(), "Kalorie: %.2f", totalCaloriesBurned));
    }

    @Override
    public void onSensorUnavailable() {
        Toast.makeText(this, "Senzor pro snímání kroků není k dispozici.", Toast.LENGTH_LONG).show();
    }
}
