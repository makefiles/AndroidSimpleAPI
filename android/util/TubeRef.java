/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package android.util;

import android.util.Log;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.content.Context;

import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

/** @hide */
public class TubeRef {

    public static final String TAG = TubeRef.class.getSimpleName();

    /** Start server */

    public static boolean startServer(Context context) {
        if (ServiceManager.checkService("STATIC_SERVER") != null) {
            Log.v(TAG, "StaticServer is already started");
            return true;
        }
        try {
            PathClassLoader loader = new PathClassLoader("/system/framework/com.amolla.server.jar", ClassLoader.getSystemClassLoader());
            Class cls = loader.loadClass("com.amolla.server.StaticServer");
            Constructor<Class> ctor = cls.getConstructor(Context.class);
            Object obj = ctor.newInstance(context);
            ServiceManager.addService("STATIC_SERVER", (IBinder) obj);
            Log.v(TAG, "StaticServer started");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Call the SDK 4 */

    private static boolean mLoad = false;
    private static Method DoAction = null;
    private static Method SetValue = null;
    private static Method GetValue = null;

    private static boolean loadMethods() {
        if (mLoad) return mLoad;
        try {
            PathClassLoader loader = new PathClassLoader("/system/framework/com.amolla.server.jar", ClassLoader.getSystemClassLoader());
            Class cls = loader.loadClass("com.amolla.sdk.Tube");
            DoAction = cls.getDeclaredMethod("doAction", new Class[]{String.class, String.class, Bundle.class});
            SetValue = cls.getDeclaredMethod("setValue", new Class[]{String.class, String.class, Bundle.class});
            GetValue = cls.getDeclaredMethod("getValue", new Class[]{String.class, String.class, Bundle.class});
            Log.i(TAG, "Load " + cls.getName() + " completed");
            mLoad = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLoad;
    }

    public static final String P0 = "PARAM0";
    public static final String P1 = "PARAM1";
    public static final String P2 = "PARAM2";
    public static final String P3 = "PARAM3";
    public static final String P4 = "PARAM4";
    public static final String P5 = "PARAM5";
    public static final String P6 = "PARAM6";
    public static final String P7 = "PARAM7";
    public static final String P8 = "PARAM8";
    public static final String P9 = "PARAM9";

    public static final String R0 = "RESULT0";
    public static final String R1 = "RESULT1";
    public static final String R2 = "RESULT2";
    public static final String R3 = "RESULT3";
    public static final String R4 = "RESULT4";
    public static final String R5 = "RESULT5";
    public static final String R6 = "RESULT6";
    public static final String R7 = "RESULT7";
    public static final String R8 = "RESULT8";
    public static final String R9 = "RESULT9";

    public static int doAction(String name, String key, Bundle val) {
        try {
            if (loadMethods()) return (int) DoAction.invoke(null, name, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int doAction(String key, Bundle val) {
        return doAction(null, key, val);
    }

    public static int setValue(String name, String key, Bundle val) {
        try {
            if (loadMethods()) return (int) SetValue.invoke(null, name, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int setValue(String key, Bundle val) {
        return setValue(null, key, val);
    }

    public static Bundle getValue(String name, String key, Bundle val) {
        try {
            if (loadMethods()) return (Bundle) GetValue.invoke(null, name, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bundle getValue(String key, Bundle val) {
        return getValue(null, key, val);
    }

    public static boolean check(String val) {
        Bundle param = new Bundle();
        param.putString(P0, val);
        return getBoolean("STATIC_SUPPORTED", param);
    }

    public static String define(String val) {
        Bundle param = new Bundle();
        param.putString(P0, val);
        return getString("STATIC_DEFINED_CONSTANT_BY_KEY", param);
    }

    public static int identify(String val) {
        Bundle param = new Bundle();
        param.putString(P0, val);
        return getInt("STATIC_RESOURCE_ID", param);
    }

    public static int setBoolean(String key, boolean val) {
        Bundle param = new Bundle();
        param.putBoolean(P0, val);
        return setValue(key, param);
    }

    public static boolean getBoolean(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return false;
        return result.getBoolean(R0);
    }

    public static int setByte(String key, byte val) {
        Bundle param = new Bundle();
        param.putByte(P0, val);
        return setValue(key, param);
    }

    public static byte getByte(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return -1;
        return result.getByte(R0);
    }

    public static byte[] getByteArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getByteArray(R0);
    }

    public static int setInt(String key, int val) {
        Bundle param = new Bundle();
        param.putInt(P0, val);
        return setValue(key, param);
    }

    public static int getInt(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return -1;
        return result.getInt(R0);
    }

    public static int[] getIntArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getIntArray(R0);
    }

    public static int setString(String key, String val) {
        Bundle param = new Bundle();
        param.putString(P0, val);
        return setValue(key, param);
    }

    public static String getString(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getString(R0);
    }

    public static String[] getStringArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getStringArray(R0);
    }
}
