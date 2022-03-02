/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.service.util;

import com.amolla.sdk.ITube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.service.DynamicReceiver;
import com.amolla.service.DynamicController;
import com.amolla.service.util.FileSystemController;
import com.amolla.service.util.KeyValueController;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;

public class UtilService extends ITube.Stub {

    private static final String TAG = UtilService.class.getSimpleName();
    private static final boolean DEBUG = DynamicController.DEBUG_ALL || false;

    private static enum UTIL_FS {
        UTIL_FS_SYSTEM_SETTINGS,
        UTIL_FS_SYSTEM_PROPERTY,
        UTIL_FS_SYSTEM_BYTES,
        UTIL_FS_SYSTEM_STRING,
        UTIL_FS_EXECUTE
    }
    private static enum UTIL_INI {
        UTIL_INI_FILE_KEY_VALUES,
        UTIL_INI_DB_CLEAR_USER,
        UTIL_INI_DB_SINGLE_USER_DATA,
        UTIL_INI_DB_MULTI_USER_DATA
    }

    private DynamicController mControl;
    public UtilService(Context context) {
        mControl = new DynamicController(context, TAG);
        mControl.setHandler(new Handler(mControl.getLooper()) {
            @Override
            public synchronized void handleMessage(Message msg) {
            }
        });
        mControl.registerReceiver(new DynamicReceiver(this), DynamicReceiver.getFilter(UTIL_FS.class, UTIL_INI.class));
        FileSystemController.init(mControl);
        KeyValueController.init(mControl);
    }

    @Override
    public int doAction(String key, Bundle val) {
        int result = ErroNo.FAILURE.code();

        if (key == null || key.isEmpty()) {
            result = ErroNo.ILLEGAL_ARGUMENT.code();
            if (DEBUG) Log.e(TAG, ErroNo.toString(result));
            return result;
        }

        try {
            if (key.indexOf(UTIL_FS.class.getSimpleName()) == 0) {

                switch (UTIL_FS.valueOf(key)) {
                    case UTIL_FS_SYSTEM_SETTINGS:
                        break;
                    case UTIL_FS_SYSTEM_PROPERTY:
                        break;
                    case UTIL_FS_SYSTEM_BYTES:
                        break;
                    case UTIL_FS_SYSTEM_STRING:
                        break;
                    case UTIL_FS_EXECUTE:
                        if (val == null || val.isEmpty()) {
                            result = ErroNo.ILLEGAL_ARGUMENT.code();
                            break;
                        }
                        if (FileSystemController.get().execute(val.getStringArray(To.P0))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    default: throw new IllegalArgumentException();
                }

            } else if (key.indexOf(UTIL_INI.class.getSimpleName()) == 0) {

                switch(UTIL_INI.valueOf(key)) {
                    case UTIL_INI_FILE_KEY_VALUES:
                        break;
                    case UTIL_INI_DB_CLEAR_USER:
                        if (val == null || val.isEmpty()) {
                            result = ErroNo.ILLEGAL_ARGUMENT.code();
                            break;
                        }
                        if (KeyValueController.get().clearUserData(val.getInt(To.P0))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_INI_DB_SINGLE_USER_DATA:
                        break;
                    case UTIL_INI_DB_MULTI_USER_DATA:
                        break;
                    default: throw new IllegalArgumentException();
                }

            }
        } catch (IllegalArgumentException e) {
            result = ErroNo.UNSUPPORTED.code();
            Log.e(TAG, "Not implemented doAction( " + key + " )");
        }

        if (DEBUG) Log.d(TAG, key + " " + ErroNo.toString(result));
        return result;
    }

    @Override
    public int setValue(String key, Bundle val) {
        int result = ErroNo.FAILURE.code();

        if (key == null || key.isEmpty() ||
            val == null || val.isEmpty()) {
            result = ErroNo.ILLEGAL_ARGUMENT.code();
            if (DEBUG) Log.e(TAG, ErroNo.toString(result));
            return result;
        }

        try {
            if (key.indexOf(UTIL_FS.class.getSimpleName()) == 0) {

                switch (UTIL_FS.valueOf(key)) {
                    case UTIL_FS_SYSTEM_SETTINGS:
                        if (FileSystemController.get().setSetting(val.getInt(To.P0), val.getString(To.P1), val.getString(To.P2))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_FS_SYSTEM_PROPERTY:
                        if (FileSystemController.get().setProperty(val.getString(To.P0), val.getString(To.P1))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_FS_SYSTEM_BYTES:
                        if (FileSystemController.get().writeBytes(val.getString(To.P0), val.getByteArray(To.P1))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_FS_SYSTEM_STRING:
                        if (FileSystemController.get().writeString(val.getString(To.P0), val.getString(To.P1))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_FS_EXECUTE:
                        break;
                    default: throw new IllegalArgumentException();
                }

            } else if (key.indexOf(UTIL_INI.class.getSimpleName()) == 0) {

                switch(UTIL_INI.valueOf(key)) {
                    case UTIL_INI_FILE_KEY_VALUES:
                        if (KeyValueController.get().setKeyValues(val.getString(To.P0), (HashMap<String, String>) val.getSerializable(To.P1))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_INI_DB_CLEAR_USER:
                        break;
                    case UTIL_INI_DB_SINGLE_USER_DATA:
                        if (KeyValueController.get().setUserData(val.getInt(To.P0), val.getString(To.P1), val.getString(To.P2))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    case UTIL_INI_DB_MULTI_USER_DATA:
                        if (KeyValueController.get().setUserData(val.getInt(To.P0), (HashMap<String, String>) val.getSerializable(To.P1))) {
                            result = ErroNo.SUCCESS.code();
                        }
                        break;
                    default: throw new IllegalArgumentException();
                }

            }
        } catch (IllegalArgumentException e) {
            result = ErroNo.UNSUPPORTED.code();
            Log.e(TAG, "Not implemented setValue( " + key + " )");
        }

        if (DEBUG) Log.d(TAG, key + " " + ErroNo.toString(result));
        return result;
    }

    @Override
    public Bundle getValue(String key, Bundle val) {
        Bundle result = new Bundle();

        if (key == null || key.isEmpty()) {
            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
            if (DEBUG) Log.e(TAG, ErroNo.toString(result.getInt(To.R0)));
            return result;
        }

        try {
            if (key.indexOf(UTIL_FS.class.getSimpleName()) == 0) {

                switch (UTIL_FS.valueOf(key)) {
                    case UTIL_FS_SYSTEM_SETTINGS:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putString(To.R0, FileSystemController.get().getSetting(val.getInt(To.P0), val.getString(To.P1)));
                        break;
                    case UTIL_FS_SYSTEM_PROPERTY:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putString(To.R0, FileSystemController.get().getProperty(val.getString(To.P0), val.getString(To.P1)));
                        break;
                    case UTIL_FS_SYSTEM_BYTES:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putByteArray(To.R0, FileSystemController.get().readBytes(val.getString(To.P0)));
                        break;
                    case UTIL_FS_SYSTEM_STRING:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putString(To.R0, FileSystemController.get().readString(val.getString(To.P0)));
                        break;
                    case UTIL_FS_EXECUTE:
                        break;
                    default: throw new IllegalArgumentException();
                }

            } else if (key.indexOf(UTIL_INI.class.getSimpleName()) == 0) {

                switch(UTIL_INI.valueOf(key)) {
                    case UTIL_INI_FILE_KEY_VALUES:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putSerializable(To.R0, KeyValueController.get().getKeyValues(val.getString(To.P0)));
                        break;
                    case UTIL_INI_DB_CLEAR_USER:
                        break;
                    case UTIL_INI_DB_SINGLE_USER_DATA:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putString(To.R0, KeyValueController.get().getUserData(val.getInt(To.P0), val.getString(To.P1)));
                        break;
                    case UTIL_INI_DB_MULTI_USER_DATA:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putSerializable(To.R0, KeyValueController.get().getUserData(val.getInt(To.P0)));
                        break;
                    default: throw new IllegalArgumentException();
                }

            }
        } catch (IllegalArgumentException e) {
            result.putInt(To.R0, ErroNo.UNSUPPORTED.code());
            Log.e(TAG, "Not implemented getValue( " + key + " )");
        }

        if (DEBUG) Log.d(TAG, key + " " + result);
        return result;
    }
}
