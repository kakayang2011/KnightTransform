package com.example.knight.doublecheck.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class A {
    static void toast(Context context) {
        Log.i("tag","test");
        Toast.makeText(context,"test",Toast.LENGTH_LONG).show();
    }
}
