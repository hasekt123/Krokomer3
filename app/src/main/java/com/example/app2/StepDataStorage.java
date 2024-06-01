package com.example.app2;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void saveStepData(int stepCount, double totalCaloriesBurned) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(STEP_COUNT_KEY, stepCount);
        editor.putLong(CALORIES_KEY, Double.doubleToLongBits(totalCaloriesBurned));
        editor.apply();
    }

    public int loadStepCount() {
        return sharedPreferences.getInt(STEP_COUNT_KEY, 0);
    }

    public double loadTotalCaloriesBurned() {
        return Double.longBitsToDouble(sharedPreferences.getLong(CALORIES_KEY, Double.doubleToLongBits(0.0)));
    }

    public void saveSelectedMode(String mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MODE_KEY, mode);
        editor.apply();
    }

    public String loadSelectedMode() {
        return sharedPreferences.getString(MODE_KEY, "Chůze");
    }

    public void saveSelectedWeight(int weight) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WEIGHT_KEY, weight);
        editor.apply();
    }

    public int loadSelectedWeight() {
        return sharedPreferences.getInt(WEIGHT_KEY, 70);
    }

    public void saveTimerTime(long time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIMER_KEY, time);
        editor.apply();
    }

    public long loadTimerTime() {
        return sharedPreferences.getLong(TIMER_KEY, 0L);
    }

    public void clearStepData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(STEP_COUNT_KEY);
        editor.remove(CALORIES_KEY);
        editor.apply();
    }

    public void clearTimerTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TIMER_KEY);
        editor.apply();
    }
}
