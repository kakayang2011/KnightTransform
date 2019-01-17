package com.knight.component.library;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManagerJava {

    private List<Application> moduleApplications = new ArrayList<>();

    private Map<Class, Class> routersMap = new HashMap<>();

    private Map<Class, Object> routerInstanceMap = new HashMap<>();

    private ServiceManagerJava() {
        moduleApplications.add(new Application());
        routersMap.put(String.class,String.class);
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
    }

    public void onCreate() {
        for (Application app : moduleApplications) {
            app.onCreate();
        }
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

    public synchronized <T> T service(Class<T> routerType) {
        T requiredRouter = null;
        if (!routerInstanceMap.containsKey(routerType)) {
            try {
                requiredRouter = (T) routersMap.get(routerType).newInstance();
                routerInstanceMap.put(routerType, requiredRouter);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            requiredRouter = (T) routerInstanceMap.get(routerType);
        }
        return requiredRouter;
    }

    public List<Application> moduleApplications() {
        return moduleApplications;
    }

    public Map<Class, Class> routersMap() {
        return routersMap;
    }

    public static ServiceManagerJava get() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static ServiceManagerJava INSTANCE = new ServiceManagerJava();
    }
}