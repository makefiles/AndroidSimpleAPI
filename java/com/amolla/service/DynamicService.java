/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.service;

import com.amolla.sdk.Tube;
import com.amolla.sdk.To;

import com.amolla.service.util.UtilService;
import com.amolla.service.info.InfoService;
import com.amolla.service.port.UartService;

import android.text.TextUtils;
import android.app.Service;
import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.ServiceManager;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class DynamicService extends Service {
    private static final String TAG = DynamicService.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static IBinder getService(final String name, Context context) {
        if (TextUtils.isEmpty(name) || context == null) return null;
        final String[] token = name.split(Tube.STR_TOKEN);
        switch (Tube.BASIC_SERVICE.valueOf(token[0])) {
            case UTIL:        return new UtilService(context);
            case INFO:        return new InfoService(context);
        }
        switch (Tube.RUNTIME_SERVICE.valueOf(token[0])) {
            case PORT_UART:   return new UartService(context, token[1]);
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getService(intent.getStringExtra(To.P0), this);
    }

    private static boolean bindService(final String name) {
        Intent intent = new Intent();
        intent.setClassName(DynamicService.class.getPackage().getName().trim(), DynamicService.class.getName().trim());
        intent.putExtra(To.P0, name);
        if (AppGlobals.getInitialApplication().bindService(intent,
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        Log.i(TAG, "Connected " + componentName);
                        try {
                            ServiceManager.addService(name, iBinder);
                            Log.d(TAG, "ServiceManager added " + name);
                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        Log.i(TAG, "Discnnected " + componentName);
                    }
                }, Context.BIND_AUTO_CREATE)) {
            return true;
        }
        Log.e(TAG, "Fail to bind " + name);
        return false;
    }

    private static boolean startService(final String name, Context context) {
        if (context == null) return bindService(name);
        try {
            ServiceManager.addService(name, getService(name, context));
            Log.d(TAG, "ServiceManager added " + name);
            return true;
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
        }
        Log.e(TAG, "Fail to start " + name);
        return false;
    }

    public static boolean startService(final String ports, String name, Context context) {
        boolean result = true;
        if (TextUtils.isEmpty(ports)) {
            if (DEBUG) Log.d(TAG, name + " service is skipped because the port is not set");
            return result;
        }
        final boolean hasToken = name.contains(Tube.STR_TOKEN);
        final String[] path = ports.split(",");
        for (int i = 0; i < path.length; i++) {
            File file = new File(path[i]);
            if (file == null || !file.exists()) {
                if (DEBUG) Log.d(TAG, name + " service is skipped because the path is invalid");
                if (hasToken) return result;
                continue;
            }
            if (!startService((hasToken ? name : name + Tube.STR_TOKEN + path[i]), context)) {
                result = false;
            }
            if (hasToken) break;
        }
        return result;
    }

    public static boolean startAll(HashMap<String, String> map, Context context) {
        boolean result = true;
        for (Tube.DEFAULT_SERVICE service : Tube.DEFAULT_SERVICE.values()) {
            if (!startService(service.name() + Tube.STR_SERVICE_SUFFIX, context)) {
                result = false;
            }
        }
        if (map == null) {
            Log.e(TAG, "The runtime services could not be started because the kernel interface map is null");
            return false;
        }
        for (Tube.RUNTIME_SERVICE service : Tube.RUNTIME_SERVICE.values()) {
            switch (service) {
                case PORT_UART:
                    if (!startService(map.get("SERIAL_UART_PORT"), service.name(), context)) {
                        result = false;
                    }
                    break;
                default: break;
            }
        }
        return result;
    }
}
