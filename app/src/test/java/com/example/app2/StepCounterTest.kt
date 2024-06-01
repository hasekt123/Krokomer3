package com.example.app2

import android.hardware.SensorManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class StepCounterTest {

    @Mock
    private lateinit var mockSensorManager: SensorManager

    @Mock
    private lateinit var mockListener: StepCounter.StepListener

    @Mock
    private lateinit var mockDataStorage: StepDataStorage

    private lateinit var stepCounter: StepCounter

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        stepCounter = StepCounter(mockListener, mockSensorManager, mockDataStorage)
    }

    @Test
    fun testCalculateCaloriesWalking() {
        whenever(mockDataStorage.loadSelectedMode()).thenReturn("Chůze")
        whenever(mockDataStorage.loadSelectedWeight()).thenReturn(70)

        stepCounter.calculateCalories("Chůze", 70)
        assertEquals(0.0, stepCounter.totalCaloriesBurned, 0.1)
    }

    @Test
    fun testCalculateCaloriesRunning() {
        whenever(mockDataStorage.loadSelectedMode()).thenReturn("Běh")
        whenever(mockDataStorage.loadSelectedWeight()).thenReturn(70)

        stepCounter.calculateCalories("Běh", 70)
        assertEquals(0.0, stepCounter.totalCaloriesBurned, 0.1)
    }

    @Test
    fun testReset() {
        stepCounter.reset()
        assertEquals(0, stepCounter.stepCount)
        assertEquals(0.0, stepCounter.totalCaloriesBurned, 0.1)
    }
}
