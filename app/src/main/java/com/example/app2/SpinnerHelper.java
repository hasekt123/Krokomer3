package com.example.app2;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerHelper {

    public static void loadSpinnerValues(Spinner modeSpinner, Spinner weightSpinner, StepDataStorage stepDataStorage) {
        setSpinnerSelection(modeSpinner, stepDataStorage.loadSelectedMode());
        setSpinnerSelection(weightSpinner, stepDataStorage.loadSelectedWeight() + " kg");
    }
// S touto metodou mi pomohlo Chat GPT
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
