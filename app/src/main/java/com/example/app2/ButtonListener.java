package com.example.app2;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ButtonListener {

    public static void setupResetButton(final MainActivity activity, Button resetButton) {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.show(activity.getSupportFragmentManager(), "ConfirmationDialog");
            }
        });
    }

    public static void setupStartPauseButton(final Timer timer, Button startPauseButton) {
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.toggleTimer();
            }
        });
    }
}
