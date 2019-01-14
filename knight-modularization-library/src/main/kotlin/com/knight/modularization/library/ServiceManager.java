package com.knight.modularization.library;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager1 {
    private ArrayList<Application> moduleApplications = new ArrayList<>();

    private HashMap<Class, Class> serviceImplMap = new HashMap<>();

    private HashMap<Class, Object> serviceImplInstanceMap = new HashMap<>();

    private ServiceManager1() {
        moduleApplications.add(new Application());
        serviceImplMap.put(String.class,String.class);
    }

    public void attachBaseContext(Context context) {
        for (Application app : moduleApplications) {
            try {
                // invoke each application's attachBaseContext
                Method attachBaseContext = ContextWrapper.class.getDeclaredMethod("attachBaseContext", Context.class);
                attachBaseContext.setAccessible(true);
                attachBaseContext.invoke(app, context);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Log.i("lyc", "attachBaseContext: " + moduleApplications.size());

    }

    public void onCreate() {
        for (Application app : moduleApplications) {
            app.onCreate();
        }
        Log.i("lyc", "onCreate: " + moduleApplications.size());

    }

    public void onConfigurationChanged(Configuration configuration) {
        for (Application app : moduleApplications) {
            app.onConfigurationChanged(configuration);
        }
    }

    public void onLowMemory() {
        for (Application app : moduleApplications) {
            app.onLowMemory();
        }
    }

    public void onTerminate() {
        for (Application app : moduleApplications) {
            app.onTerminate();
        }

    }

    public void onTrimMemory(int level) {
        for (Application app : moduleApplications) {
            app.onTrimMemory(level);
        }
    }

    public synchronized <T> T getService(Class<T> routerType) {
        T requiredRouter = null;
        Log.i("lyc", "getService: " + serviceImplMap.size());

        if (!get().serviceImplInstanceMap.containsKey(routerType)) {
            try {
                requiredRouter = (T) serviceImplMap.get(routerType).newInstance();
                serviceImplInstanceMap.put(routerType, requiredRouter);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            requiredRouter = (T) serviceImplInstanceMap.get(routerType);
        }
        return requiredRouter;
    }

    public List<Application> moduleApplications() {
        return moduleApplications;
    }

    public Map<Class, Class> routersMap() {
        return serviceImplMap;
    }

    public static ServiceManager1 get() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static ServiceManager1 INSTANCE = new ServiceManager1();
    }
}
