package com.example.app2;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends ScreenRotation implements StepCounter.StepListener, ConfirmationDialog.ConfirmationDialogListener {

    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView caloriesTextView;
    private TextView timerTextView;
    private ProgressBar progressBar;
    private TextView stepCountTargetTextView;
    private Spinner modeSpinner;
    private Spinner weightSpinner;
    private Button resetButton;
    private Button startPauseButton;

    private StepCounter stepCounter;
    private Timer timer;
    private stepDataStorage stepDataStorage;

    private int stepCountTarget = 8000;
    private String selectedMode = "Chůze";
    private int weight = 70; // Default weight

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components and listeners
        initializeUIComponents();
        initializeSensorComponents();
        setupListeners();
    }

    private void initializeUIComponents() {
        // Initialize text views
        stepCountTextView = findViewById(R.id.stepCountTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        timerTextView = findViewById(R.id.timerTextView);
        stepCountTargetTextView = findViewById(R.id.KrokovyCilTextView);

        // Initialize progress bar
        progressBar = findViewById(R.id.progressBar);

        // Initialize spinners
        modeSpinner = findViewById(R.id.modeSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);

        // Initialize buttons
        resetButton = findViewById(R.id.resetButton);
        startPauseButton = findViewById(R.id.startPauseButton);

        // Set up progress bar
        setupProgressBar();
    }

    private void setupProgressBar() {
        progressBar.setMax(stepCountTarget);
        progressBar.setProgress(0);
    }

    private void initializeSensorComponents() {
        // Get the sensor manager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Initialize the step data storage
        stepDataStorage = new stepDataStorage(this);

        // Initialize the step counter with the sensor manager and data storage
        stepCounter = new StepCounter(this, sensorManager, stepDataStorage);

        // Initialize the timer
        timer = new Timer(timerTextView, startPauseButton);
    }

    private void setupListeners() {
        setupModeSpinner();
        setupWeightSpinner();
        setupResetButton();
        setupStartPauseButton();
    }

    private void setupModeSpinner() {
        // Set up mode spinner adapter
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        // Set up mode spinner listener
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update the selected mode
                selectedMode = parentView.getItemAtPosition(position).toString();

                // Calculate the calories based on the selected mode and weight
                stepCounter.calculateCalories(selectedMode, weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Default mode if nothing is selected
                selectedMode = "Chůze";
            }
        });
    }

    private void setupWeightSpinner() {
        // Set up weight spinner adapter
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_array, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        // Set up weight spinner listener
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected weight as a string
                String selectedWeight = parentView.getItemAtPosition(position).toString();

                // Parse the selected weight to an integer
                weight = parseWeight(selectedWeight);

                // Calculate the calories based on the selected mode and weight
                stepCounter.calculateCalories(selectedMode, weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Default weight if nothing is selected
                weight = 70;
            }
        });
    }

    private int parseWeight(String weightString) {
        // Parse weight string to integer
        try {
            return Integer.parseInt(weightString.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            Log.e("MainActivity", "Chyba při parsování váhy", e);
            return 70; // Default value
        }
    }

    private void setupResetButton() {
        // Set up reset button listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog before resetting steps
                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
            }
        });
    }

    private void setupStartPauseButton() {
        // Set up start/pause button listener
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the timer between start and pause
                timer.toggleTimer();
            }
        });
    }

    @Override
    public void onConfirm() {
        // Handle confirmation dialog positive button click
        resetStepCount();
    }

    @Override
    public void onCancel() {
        // Handle confirmation dialog negative button click
        Toast.makeText(this, "Reset canceled", Toast.LENGTH_SHORT).show();
    }

    private void resetStepCount() {
        // Reset step count and related UI components
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
        // Update UI with new step count, distance, and calories
        stepCountTextView.setText("Počet kroků: " + stepCount);

        // Calculate progress percentage and update progress bar
        int progress = calculateProgress(stepCount);
        progressBar.setProgress(progress);

        // Update distance and calories text views
        updateDistanceTextView(distanceInKm);
        updateCaloriesTextView(totalCaloriesBurned);
    }

    private int calculateProgress(int stepCount) {
        // Calculate the progress percentage
        return (int) ((double) stepCount / stepCountTarget * progressBar.getMax());
    }

    private void updateDistanceTextView(double distanceInKm) {
        // Update the distance text view
        distanceTextView.setText(String.format(Locale.getDefault(), "Vzdálenost v km: %.2f", distanceInKm));
    }

    private void updateCaloriesTextView(double totalCaloriesBurned) {
        // Update the calories text view
        caloriesTextView.setText(String.format(Locale.getDefault(), "Kalorie: %.2f", totalCaloriesBurned));
    }

    @Override
    public void onSensorUnavailable() {
        // Show a message when the step sensor is unavailable
        Toast.makeText(this, "Senzor pro snímání kroků není k dispozici.", Toast.LENGTH_LONG).show();
    }
}
