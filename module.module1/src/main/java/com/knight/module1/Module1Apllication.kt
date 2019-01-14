package com.knight.module1

import android.app.Application
import android.content.Context
import android.util.Log
import com.knight.modularization.library.ModuleSpec

@ModuleSpec
class Module1Apllication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.i("lyc","attachBaseContext1");
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("lyc","onCreate1");
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i("lyc","onLowMemory1");

    }
}