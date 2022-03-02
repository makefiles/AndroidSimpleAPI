/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.service;

import static com.amolla.sdk.Tube.STR_EXE_ACTION_PREFIX;
import static com.amolla.sdk.Tube.STR_SET_ACTION_PREFIX;
import static com.amolla.sdk.Tube.STR_GET_ACTION_PREFIX;
import com.amolla.sdk.ITube;
import com.amolla.sdk.To;

import com.android.internal.annotations.GuardedBy;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;

public class DynamicReceiver extends BroadcastReceiver {
    public static final String TAG = DynamicReceiver.class.getSimpleName();
    public static final boolean DEBUG = true;

    public static <T extends Enum<T>> IntentFilter getFilter(Class<?>... enumerations) {
        IntentFilter filter = new IntentFilter();
        for (Class<?> cls : enumerations) {
            addAction(filter, (Class<T>) cls);
        }
        return filter;
    }

    public static <T extends Enum<T>> boolean addAction(IntentFilter filter, Class<T> enumeration) {
        if (filter == null || enumeration == null) {
            return false;
        }
        for (T enumValue : enumeration.getEnumConstants()) {
            if (DEBUG) { Log.d(TAG, "Add filter : " + enumValue.name()); };
            filter.addAction(STR_EXE_ACTION_PREFIX + enumValue.name());
            filter.addAction(STR_SET_ACTION_PREFIX + enumValue.name());
            filter.addAction(STR_GET_ACTION_PREFIX + enumValue.name());
        }
        return true;
    }

    private final ITube.Stub mStub;
    public DynamicReceiver(ITube.Stub stub) {
        mStub = stub;
    }

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        try {
            final String action = intent.getAction();
            if (DEBUG) { Log.d(TAG, action + " intent received"); }
            if (action.indexOf(STR_EXE_ACTION_PREFIX) == 0) {
                String key = action.replaceFirst(STR_EXE_ACTION_PREFIX, "");
                Bundle param = intent.getExtras();
                intent.replaceExtras((Bundle) null);
                if (intent.hasExtra(key)) {
                    Intent result = new Intent();
                    result.putExtra(To.R0, action);
                    result.putExtra(To.R1, mStub.doAction(key, param));
                    IntentSender sender = (IntentSender) intent.getParcelableExtra(key);
                    try { sender.sendIntent(context, 0, result, null, null);
                    } catch (IntentSender.SendIntentException ignored) {}
                } else {
                    mStub.doAction(key, param);
                }
            } else if (action.indexOf(STR_SET_ACTION_PREFIX) == 0) {
                String key = action.replaceFirst(STR_SET_ACTION_PREFIX, "");
                Bundle param = intent.getExtras();
                intent.replaceExtras((Bundle) null);
                if (intent.hasExtra(key)) {
                    Intent result = new Intent();
                    result.putExtra(To.R0, action);
                    result.putExtra(To.R1, mStub.setValue(key, param));
                    IntentSender sender = (IntentSender) intent.getParcelableExtra(key);
                    try { sender.sendIntent(context, 0, result, null, null);
                    } catch (IntentSender.SendIntentException ignored) {}
                } else {
                    mStub.setValue(key, param);
                }
            } else if (action.indexOf(STR_GET_ACTION_PREFIX) == 0) {
                String key = action.replaceFirst(STR_GET_ACTION_PREFIX, "");
                Bundle param = intent.getExtras();
                intent.replaceExtras((Bundle) null);
                if (intent.hasExtra(key)) {
                    Intent result = new Intent();
                    result.putExtra(To.R0, action);
                    result.putExtra(To.R1, mStub.getValue(key, param));
                    IntentSender sender = (IntentSender) intent.getParcelableExtra(key);
                    try { sender.sendIntent(context, 0, result, null, null);
                    } catch (IntentSender.SendIntentException ignored) {}
                } else {
                    Log.e(TAG, action + " intent received but no place to send");
                }
            }
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
        }
    }
}
