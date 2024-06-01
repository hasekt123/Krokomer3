package com.example.app2;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Timer handles the step counting timer functionality.
 */
public class Timer {

    private TextView timerTextView;
    private Button startPauseButton;
    private StepDataStorage stepDataStorage;

    private boolean isTimerRunning = false;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private Handler handler = new Handler(Looper.getMainLooper());

    public Timer(TextView timerTextView, Button startPauseButton, StepDataStorage stepDataStorage) {
        this.timerTextView = timerTextView;
        this.startPauseButton = startPauseButton;
        this.stepDataStorage = stepDataStorage;

        this.timeSwapBuff = stepDataStorage.loadTimerTime();
    }

    /**
     * Toggles the timer between start and pause.
     */
    public void toggleTimer() {
        if (isTimerRunning) {
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimerThread);
            startPauseButton.setText("Start");
            stepDataStorage.saveTimerTime(timeSwapBuff);
        } else {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            startPauseButton.setText("Pauza");
        }
        isTimerRunning = !isTimerRunning;
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        isTimerRunning = false;
        startTime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
        timerTextView.setText("Čas: 00:00:00");
        startPauseButton.setText("Start");
        handler.removeCallbacks(updateTimerThread);
        stepDataStorage.clearTimerTime();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs %= 60;
            mins %= 60;

            timerTextView.setText(String.format(Locale.getDefault(), "Čas: %02d:%02d:%02d", hrs, mins, secs));
            handler.postDelayed(this, 0);
        }
    };
}
