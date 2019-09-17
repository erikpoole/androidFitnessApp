package com.example.project;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class TabletUtil {

    /**
     * Checks if the device is a tablet (7" or greater).
     * current = (Activity) this.mContext)
     */
    public static boolean checkIsTablet(Activity current) {
        Display display = current.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        return diagonalInches >= 7.0;
    }
}
