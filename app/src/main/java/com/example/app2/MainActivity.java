package com.example.app2;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private Button resetButton;
    private Button startPauseButton;

    private StepCounter stepCounter;
    private Timer timer;

    private int stepCountTarget = 8000;
    private String selectedMode = "Chůze";
    private int weight = 70; // Default weight

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components and listeners
        initializeUI();
        setupListeners();
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
        resetButton = findViewById(R.id.resetButton);
        startPauseButton = findViewById(R.id.startPauseButton);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounter = new StepCounter(this, sensorManager);
        timer = new Timer(timerTextView, startPauseButton);
    }

    private void setupListeners() {
        setupSpinners();
        setupButtons();
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
                stepCounter.calculateCalories(selectedMode, weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedMode = "Chůze";
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
                stepCounter.calculateCalories(selectedMode, weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                weight = 70;
            }
        });
    }

    private void setupButtons() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
            }
        });
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.toggleTimer();
            }
        });
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
        progressBar.setProgress(stepCount);
        distanceTextView.setText(String.format(Locale.getDefault(), "Vzdálenost v km: %.2f", distanceInKm));
        caloriesTextView.setText(String.format(Locale.getDefault(), "Kalorie: %.2f", totalCaloriesBurned));
    }

    @Override
    public void onSensorUnavailable() {
        Toast.makeText(this, "Senzor pro snímání kroků není k dispozici.", Toast.LENGTH_LONG).show();
    }
}
