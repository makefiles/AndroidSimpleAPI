/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.sdk;

import com.amolla.sdk.ITube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;

import android.text.TextUtils;
import android.os.Bundle;
import android.os.ServiceManager;
import android.util.Log;
import android.content.Context;

import java.util.HashMap;

/**
 * As an integrated API that is exposed to the outside, sub-SDKs use this API.
 * @since 1.0
 */
public class Tube {

    public static final String SDK_VERSION      = "1.0.0";

    private static final String TAG = Tube.class.getSimpleName();
    private static final boolean DEBUG = true;

    public static final String STR_EXE_ACTION_PREFIX = "EXE.ACTION.INTENT.";
    public static final String STR_SET_ACTION_PREFIX = "SET.ACTION.INTENT.";
    public static final String STR_GET_ACTION_PREFIX = "GET.ACTION.INTENT.";

    public static final String STR_TOKEN = "::";
    public static final String STR_SERVICE_SUFFIX = STR_TOKEN + DEFAULT_SERVICE.class.getSimpleName();
    public static enum DEFAULT_SERVICE {
        UTIL,
        INFO,
        SET
    }
    public static enum RUNTIME_SERVICE {
        PORT_UART
    }

    private static HashMap<String, ITube> mServices = new HashMap<String, ITube>();
    public static ITube getService(String name) {
        if (!name.isEmpty() && !mServices.containsKey(name)) {
            ITube service = ITube.Stub.asInterface(ServiceManager.getService(name));
            if (service != null) {
                if (DEBUG) { Log.d(TAG, "The service to import is " + name); }
                mServices.put(name, service);
            }
        }
        return mServices.get(name);
    }

    private static String findName(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        for (DEFAULT_SERVICE service : DEFAULT_SERVICE.values()) {
            if (key.indexOf(service.name()) == 0) return service.name() + STR_SERVICE_SUFFIX;
        }
        for (RUNTIME_SERVICE service : RUNTIME_SERVICE.values()) {
            if (key.indexOf(service.name()) == 0) return service.name();
        }
        return "STATIC_SERVER";
    }

    public static int doAction(String name, String key, Bundle val) {
        if (DEBUG) { Log.d(TAG, "Do action : " + key + ", " + val); }
        if (!TextUtils.isEmpty(key) && !key.equals("N/A")) {
            try {
                if (!TextUtils.isEmpty(name)) {
                    return getService(name).doAction(key, val);
                }
                return getService(findName(key)).doAction(key, val);
            } catch (Exception e) {
                if (DEBUG) { e.printStackTrace(); }
            }
        }
        return ErroNo.UNKNOWN.code();
    }

    public static int doAction(String key, Bundle val) {
        return doAction(null, key, val);
    }

    public static int setValue(String name, String key, Bundle val) {
        if (DEBUG) { Log.d(TAG, "Set value : " + key + ", " + val); }
        if (!TextUtils.isEmpty(key) && !key.equals("N/A")) {
            try {
                if (!TextUtils.isEmpty(name)) {
                    return getService(name).setValue(key, val);
                }
                return getService(findName(key)).setValue(key, val);
            } catch (Exception e) {
                if (DEBUG) { e.printStackTrace(); }
            }
        }
        return ErroNo.UNKNOWN.code();
    }

    public static int setValue(String key, Bundle val) {
        return setValue(null, key, val);
    }

    public static Bundle getValue(String name, String key, Bundle val) {
        if (DEBUG) { Log.d(TAG, "Get value : " + key + ", " + val); }
        if (!TextUtils.isEmpty(key) && !key.equals("N/A")) {
            try {
                if (!TextUtils.isEmpty(name)) {
                    return getService(name).getValue(key, val);
                }
                return getService(findName(key)).getValue(key, val);
            } catch (Exception e) {
                if (DEBUG) { e.printStackTrace(); }
            }
        }
        return null;
    }

    public static Bundle getValue(String key, Bundle val) {
        return getValue(null, key, val);
    }

    public static boolean check(String key, String val) {
        Bundle param = new Bundle();
        param.putString(To.P0, val);
        return getBoolean(key, param);
    }

    public static String define(String key, String val) {
        Bundle param = new Bundle();
        param.putString(To.P0, val);
        return getString(key, param);
    }

    public static int setBoolean(String key, boolean val) {
        Bundle param = new Bundle();
        param.putBoolean(To.P0, val);
        return setValue(key, param);
    }

    public static boolean getBoolean(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return false;
        return result.getBoolean(To.R0);
    }

    public static int setByte(String key, byte val) {
        Bundle param = new Bundle();
        param.putByte(To.P0, val);
        return setValue(key, param);
    }

    public static byte getByte(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return -1;
        return result.getByte(To.R0);
    }

    public static byte[] getByteArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getByteArray(To.R0);
    }

    public static int setInt(String key, int val) {
        Bundle param = new Bundle();
        param.putInt(To.P0, val);
        return setValue(key, param);
    }

    public static int getInt(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return -1;
        return result.getInt(To.R0);
    }

    public static int[] getIntArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getIntArray(To.R0);
    }

    public static int setString(String key, String val) {
        Bundle param = new Bundle();
        param.putString(To.P0, val);
        return setValue(key, param);
    }

    public static String getString(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getString(To.R0);
    }

    public static String[] getStringArray(String key, Bundle val) {
        Bundle result = getValue(key, val);
        if (result == null) return null;
        return result.getStringArray(To.R0);
    }
}
