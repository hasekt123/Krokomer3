package com.example.app2;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    private StepDataStorage stepDataStorage;

    @Before
    public void setUp() {
        stepDataStorage = new StepDataStorage(null); // Context je zde nastaven na null pro jednoduchost
    }

    @Test
    public void testSaveAndLoadStepData() {
        int stepCount = 5000;
        double caloriesBurned = 150.5;

        stepDataStorage.saveStepData(stepCount, caloriesBurned);
        assertEquals(stepCount, stepDataStorage.loadStepCount());
        assertEquals(caloriesBurned, stepDataStorage.loadTotalCaloriesBurned(), 0.01);
    }

    @Test
    public void testSaveAndLoadSelectedMode() {
        String mode = "Běh";
        stepDataStorage.saveSelectedMode(mode);
        assertEquals(mode, stepDataStorage.loadSelectedMode());
    }

    @Test
    public void testSaveAndLoadSelectedWeight() {
        int weight = 75;
        stepDataStorage.saveSelectedWeight(weight);
        assertEquals(weight, stepDataStorage.loadSelectedWeight());
    }

    @Test
    public void testSaveAndLoadTimerTime() {
        long time = 123456;
        stepDataStorage.saveTimerTime(time);
        assertEquals(time, stepDataStorage.loadTimerTime());
    }

    @Test
    public void testClearStepData() {
        stepDataStorage.saveStepData(5000, 150.5);
        stepDataStorage.clearStepData();
        assertEquals(0, stepDataStorage.loadStepCount());
        assertEquals(0.0, stepDataStorage.loadTotalCaloriesBurned(), 0.01);
    }
}
