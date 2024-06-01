package com.example.app2;

import android.app.Activity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class UIInitializer {

    private Activity activity;

    public UIInitializer(Activity activity) {
        this.activity = activity;
    }

    public void initializeUIComponents() {
        TextView stepCountTextView = activity.findViewById(R.id.stepCountTextView);
        TextView distanceTextView = activity.findViewById(R.id.distanceTextView);
        TextView caloriesTextView = activity.findViewById(R.id.caloriesTextView);
        TextView timerTextView = activity.findViewById(R.id.timerTextView);
        TextView stepCountTargetTextView = activity.findViewById(R.id.KrokovyCilTextView);
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        Spinner modeSpinner = activity.findViewById(R.id.modeSpinner);
        Spinner weightSpinner = activity.findViewById(R.id.weightSpinner);
        Button resetButton = activity.findViewById(R.id.resetButton);
        Button startPauseButton = activity.findViewById(R.id.startPauseButton);

        progressBar.setMax(8000); // Set initial max value
        progressBar.setProgress(0);
    }
}
