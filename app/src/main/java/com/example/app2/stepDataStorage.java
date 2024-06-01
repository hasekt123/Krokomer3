package com.example.app2;

import android.content.Context;
import android.content.SharedPreferences;

public class stepDataStorage {

    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "StepCountKey";
    private static final String CALORIES_KEY = "CaloriesKey";

    private SharedPreferences sharedPreferences;

    public stepDataStorage(Context context) {
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

    public void clearStepData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(STEP_COUNT_KEY);
        editor.remove(CALORIES_KEY);
        editor.apply();
    }
}
