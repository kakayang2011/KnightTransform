package com.example.knight.doublecheck.app;

import android.util.Log;

public class DoubleCheckTool {

    public static long CLICK_DURING_MILLIS = 300L * 1000000;
    private static final String TAG = DoubleCheckTool.class.getSimpleName();

    private static long lastClickTime = 0;

    public static boolean isClickable() {
        Log.i("liyachao","isClickable");
        long now = System.nanoTime();
        long last = lastClickTime;
        if (last == 0 || (now - last) > CLICK_DURING_MILLIS) {
            Log.i("liyachao","true: "+ ((now - last)));
            lastClickTime = System.nanoTime();

            return true;
        } else {
            Log.i("liyachao","false");
            return false;
        }
    }

}
