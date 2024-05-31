package com.example.app2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepCounter implements SensorEventListener {

    private static final double STEP_LENGTH_IN_METERS = 0.75;

    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private int stepCount = 0;
    private int initialStepCount = -1;
    private double totalCaloriesBurned = 0.0;

    private StepListener listener;

    public StepCounter(StepListener listener, SensorManager sensorManager) {
        this.listener = listener;
        this.sensorManager = sensorManager;
        this.stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCountSensor == null) {
            // Senzor není dostupný, informujte uživatele a nepokračujte dále
            listener.onSensorUnavailable();
            return;
        }

        registerSensor();
    }

    public void registerSensor() {
        if (stepCountSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterSensor() {
        if (stepCountSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    public void reset() {
        initialStepCount = -1;
        stepCount = 0;
        totalCaloriesBurned = 0.0;
    }

    public void calculateCalories(String mode, int weight) {
        double metValue;
        switch (mode) {
            case "Běh":
                metValue = 8.0;
                break;
            case "Chůze":
            default:
                metValue = 3.5;
                break;
        }
        double distanceInKm = (stepCount * STEP_LENGTH_IN_METERS) / 1000.0;
        totalCaloriesBurned = metValue * weight * distanceInKm / 1.60934; // Convert km to miles
        listener.onStepCountUpdated(stepCount, distanceInKm, totalCaloriesBurned);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (initialStepCount == -1) {
            initialStepCount = (int) event.values[0];
        }

        stepCount = (int) event.values[0] - initialStepCount;
        calculateCalories("Chůze", 70); // Default calculation with initial mode and weight
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface StepListener {
        void onStepCountUpdated(int stepCount, double distanceInKm, double totalCaloriesBurned);
        void onSensorUnavailable(); // Nová metoda upozornění
    }
}
