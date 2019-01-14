package com.knight.modularization.library

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.util.Log
import java.util.ArrayList
import java.util.HashMap

//class ServiceManager1 {
//
//    companion object {
//        @JvmStatic
//        val ins: ServiceManager1 by lazy {
//            ServiceManager1()
//        }
//    }
//
//    val moduleApplications = ArrayList<Application>()
//    val serviceImplMap = HashMap<Class<*>, Class<*>>()
//    val serviceImplInstanceMap = HashMap<Class<*>, Any>()
//
//    private constructor()
//
//    fun attachBaseContext(context: Context) {
//        moduleApplications.forEach {
//            val attachBaseContext = ContextWrapper::class.java.getDeclaredMethod("attachBaseContext", Context::class.java)
//            attachBaseContext.isAccessible = true
//            attachBaseContext.invoke(it, context)
//        }
//        Log.i("lyc", "attachBaseContext: " + moduleApplications.size)
//
//    }
//
//    fun onCreate() {
//        moduleApplications.forEach {
//            it.onCreate()
//        }
//        Log.i("lyc", "onCreate: " + moduleApplications.size)
//
//    }
//
//    fun onConfigurationChanged(configuration: Configuration) {
//        moduleApplications.forEach {
//            it.onConfigurationChanged(configuration)
//        }
//    }
//
//    fun onLowMemory() {
//        moduleApplications.forEach {
//            it.onLowMemory()
//        }
//    }
//
//    fun onTerminate() {
//        moduleApplications.forEach {
//            it.onTerminate()
//        }
//    }
//
//    fun onTrimMemory(level: Int) {
//        moduleApplications.forEach {
//            it.onTrimMemory(level)
//        }
//    }
//
//    @Synchronized
//    fun <T> getService(classType: Class<T>): T? {
//        Log.i("lyc", "getService1: " + serviceImplMap.size)
//
//        val any = (serviceImplInstanceMap[classType]
//                ?: serviceImplMap[classType]?.apply {
//                    newInstance().apply { serviceImplInstanceMap[classType] = this }
//                    Log.i("lyc", "getService2: " + serviceImplInstanceMap.size)
//                })
//
//        Log.i("lyc", "getService3: " + (any as T))
//
//        return any as T
//    }
//}