package com.example.app2;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * SpinnerHelper provides helper methods for spinner operations.
 */
public class SpinnerHelper {

    /**
     * Loads the spinner values from storage.
     */
    public static void loadSpinnerValues(Spinner modeSpinner, Spinner weightSpinner, StepDataStorage stepDataStorage) {
        setSpinnerSelection(modeSpinner, stepDataStorage.loadSelectedMode());
        setSpinnerSelection(weightSpinner, stepDataStorage.loadSelectedWeight() + " kg");
    }

    /**
     * Sets the selection of a spinner to a specific value.
     */
    // Zde mi částečně pomohlo AI
    private static void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
