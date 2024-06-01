package com.example.app2;

import android.app.Activity;
import android.view.WindowManager;

/**
 * KeepScreenOn provides methods to keep the screen on.
 */
public class KeepScreenOn {

    /**
     * Enables keeping the screen on.
     */
    public static void enable(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Disables keeping the screen on.
     */
    public static void disable(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
