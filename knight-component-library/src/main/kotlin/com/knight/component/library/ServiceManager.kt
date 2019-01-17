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
        /**
         * @JvmStatic 是为了在Java可以直接调用改静态方法，不用添加COMPANION
         * 懒加载模式的单例，是不是比Java的单例更加简单
         */
        @JvmStatic
        val ins: ServiceManager by lazy {
            ServiceManager()
        }

        const val TAG = "ServiceManager"
    }
    // 各个module的Application集合，是为了方面管理各个module的生命周期
    private val moduleApplications = ArrayList<Application>()
    // module和moduleService之前的映射关系哈希表
    private val serviceImplMap = HashMap<Class<*>, Class<*>>()
    // module的实例对应哈希表，仅在第一次调用的时候初始化
    private val serviceImplInstanceMap = HashMap<Class<*>, Any>()

    private constructor(){
        moduleApplications.add(Application())
        serviceImplMap.put(String.javaClass,String.javaClass)
    }

    // 该方法会被注入到主app的Application中，按照Application的生命周期进行调用，以下的方法都是一个意思
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

    // 同步调用ModuleService的实例，各个module的实例是在这里被创建的
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
        } ?: Log.e(TAG, "no ${classType.name} type, please checkout your code~~~")

        return null
    }
}