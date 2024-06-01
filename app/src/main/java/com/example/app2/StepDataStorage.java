package com.example.app2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * StepDataStorage handles saving and loading step data and user preferences.
 */
public class StepDataStorage {

    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "StepCountKey";
    private static final String CALORIES_KEY = "CaloriesKey";
    private static final String MODE_KEY = "ModeKey";
    private static final String WEIGHT_KEY = "WeightKey";
    private static final String TIMER_KEY = "TimerKey";

    private SharedPreferences sharedPreferences;

    public StepDataStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves the step count and total calories burned.
     */
    public void saveStepData(int stepCount, double totalCaloriesBurned) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(STEP_COUNT_KEY, stepCount);
        editor.putLong(CALORIES_KEY, Double.doubleToLongBits(totalCaloriesBurned));
        editor.apply();
    }

    /**
     * Loads the step count.
     */
    public int loadStepCount() {
        return sharedPreferences.getInt(STEP_COUNT_KEY, 0);
    }

    /**
     * Loads the total calories burned.
     */
    public double loadTotalCaloriesBurned() {
        return Double.longBitsToDouble(sharedPreferences.getLong(CALORIES_KEY, Double.doubleToLongBits(0.0)));
    }

    /**
     * Saves the selected mode.
     */
    public void saveSelectedMode(String mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MODE_KEY, mode);
        editor.apply();
    }

    /**
     * Loads the selected mode.
     */
    public String loadSelectedMode() {
        return sharedPreferences.getString(MODE_KEY, "Chůze");
    }

    /**
     * Saves the selected weight.
     */
    public void saveSelectedWeight(int weight) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WEIGHT_KEY, weight);
        editor.apply();
    }

    /**
     * Loads the selected weight.
     */
    public int loadSelectedWeight() {
        return sharedPreferences.getInt(WEIGHT_KEY, 70);
    }

    /**
     * Saves the timer time.
     */
    public void saveTimerTime(long time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIMER_KEY, time);
        editor.apply();
    }

    /**
     * Loads the timer time.
     */
    public long loadTimerTime() {
        return sharedPreferences.getLong(TIMER_KEY, 0L);
    }

    /**
     * Clears the step data.
     */
    public void clearStepData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(STEP_COUNT_KEY);
        editor.remove(CALORIES_KEY);
        editor.apply();
    }

    /**
     * Clears the timer time.
     */
    public void clearTimerTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TIMER_KEY);
        editor.apply();
    }
}
