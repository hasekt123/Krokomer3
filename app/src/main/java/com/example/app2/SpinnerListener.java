package com.example.app2;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * SpinnerListener sets up the mode and weight spinners.
 */
public class SpinnerListener {

    /**
     * Sets up the mode spinner.
     */
    public static void setupModeSpinner(final Activity activity, final Spinner modeSpinner, final StepCounter stepCounter, final StepDataStorage stepDataStorage) {
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(activity,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMode = parentView.getItemAtPosition(position).toString();
                stepDataStorage.saveSelectedMode(selectedMode);
                stepCounter.calculateCalories(selectedMode, stepDataStorage.loadSelectedWeight());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    /**
     * Sets up the weight spinner.
     */
    public static void setupWeightSpinner(final Activity activity, final Spinner weightSpinner, final StepCounter stepCounter, final StepDataStorage stepDataStorage) {
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(activity,
                R.array.weight_array, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedWeight = parentView.getItemAtPosition(position).toString();
                int weight = Integer.parseInt(selectedWeight.replaceAll("[^0-9]", ""));
                stepDataStorage.saveSelectedWeight(weight);
                stepCounter.calculateCalories(stepDataStorage.loadSelectedMode(), weight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
