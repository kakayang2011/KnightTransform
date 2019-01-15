package com.knight.component.library

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.util.Log
import java.lang.RuntimeException
import java.util.ArrayList
import java.util.HashMap

class ServiceManager {

    companion object {
        @JvmStatic
        val ins: ServiceManager by lazy {
            ServiceManager()
        }

        const val TAG = "ServiceManager"
    }

    val moduleApplications = ArrayList<Application>()
    val serviceImplMap = HashMap<Class<*>, Class<*>>()
    val serviceImplInstanceMap = HashMap<Class<*>, Any>()

    private constructor()

    fun attachBaseContext(context: Context) {
        moduleApplications.forEach {
            val attachBaseContext = ContextWrapper::class.java.getDeclaredMethod("attachBaseContext", Context::class.java)
            attachBaseContext.isAccessible = true
            attachBaseContext.invoke(it, context)
        }
        Log.i(TAG, "attachBaseContext: " + moduleApplications.size)

    }

    fun onCreate() {
        moduleApplications.forEach {
            it.onCreate()
        }
        Log.i(TAG, "onCreate: " + moduleApplications.size)

    }

    fun onConfigurationChanged(configuration: Configuration) {
        moduleApplications.forEach {
            it.onConfigurationChanged(configuration)
        }
    }

    fun onLowMemory() {
        moduleApplications.forEach {
            it.onLowMemory()
        }
    }

    fun onTerminate() {
        moduleApplications.forEach {
            it.onTerminate()
        }
    }

    fun onTrimMemory(level: Int) {
        moduleApplications.forEach {
            it.onTrimMemory(level)
        }
    }

    @Synchronized
    fun <T> getService(classType: Class<T>): T? {
        Log.i(TAG, "getService1: " + serviceImplMap.size)
        val classNewInstance = serviceImplInstanceMap[classType]
        classNewInstance?.let {
            Log.i(TAG, "getService2: $it")
            return it as T
        } ?: serviceImplMap[classType]?.let {
            return (it.newInstance() as T).apply {
                Log.i(TAG, "getService3: $this")
                serviceImplInstanceMap[classType] = this as Any
            }
        } ?: throw RuntimeException("no ${classType.name} type, please checkout your code~~~")
    }
}