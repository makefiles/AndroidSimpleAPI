/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.service.port;

import com.amolla.sdk.ITube;
import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.sdk.set.OtherDevice;
import com.amolla.jni.PortUartNative;
import com.amolla.service.DynamicReceiver;
import com.amolla.service.DynamicController;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UartService extends ITube.Stub {
    private static final String TAG = UartService.class.getSimpleName();
    private static final boolean DEBUG = DynamicController.DEBUG_ALL || false;

    private static final int MSG_SCREEN_CHANGED = 1000;
    private static enum PORT_UART {
        PORT_UART_OPEN,
        PORT_UART_CLOSE,
        PORT_UART_SEND_BREAK,
        PORT_UART_READ,
        PORT_UART_WRITE
    }

    private PortUartNative mUartNative;

    private DynamicController mControl;
    public UartService(Context context, String path) {
        mUartNative = new PortUartNative(path);
        mControl = new DynamicController(context, TAG);
        mControl.setHandler(new Handler(mControl.getLooper()) {
            @Override
            public synchronized void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SCREEN_CHANGED:
                        final String action = (String) msg.obj;
                        if (mUartNative != null) mUartNative.sleep(action.equals(Intent.ACTION_SCREEN_OFF));
                        break;
                    default:break;
                }
            }
        });
        mControl.registerReceiver(new DynamicReceiver(this), DynamicReceiver.getFilter(PORT_UART.class));
        setRequests();
    }

    private void setRequests() {
        long reqUartClockEnable = -1;
        long reqUartClockDisable = -1;
        try { reqUartClockEnable = Long.parseLong(mControl.getDefinition("REQ_UART_CLOCK_ENABLED")); } catch (Exception e) {};
        try { reqUartClockDisable = Long.parseLong(mControl.getDefinition("REQ_UART_CLOCK_DISABLED")); } catch (Exception e) {};
        if (mUartNative != null && mUartNative.setRequests(reqUartClockEnable, reqUartClockDisable)) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mControl.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Message msg = mControl.getHandler().obtainMessage();
                    msg.what = MSG_SCREEN_CHANGED;
                    msg.obj = intent.getAction();
                    mControl.getHandler().sendMessage(msg);
                }
            }, filter);
        }
    }

    private void setConsoleAsSerial(boolean enable) {
        if (mUartNative != null && Tube.check("STATIC_SAME_AS_CONSOLE", mUartNative.getPortPath())) {
            OtherDevice.get().setUsingConsoleAsSerial(enable);
        }
    }

    @Override
    public int doAction(String key, Bundle val) {
        int result = ErroNo.FAILURE.code();

        if (key == null || key.isEmpty()) {
            result = ErroNo.ILLEGAL_ARGUMENT.code();
            if (DEBUG) Log.e(TAG, ErroNo.toString(result));
            return result;
        }

        if (mUartNative == null) {
            result = ErroNo.ILLEGAL_STATE.code();
            if (DEBUG) Log.e(TAG, key + " " + ErroNo.toString(result));
            return result;
        }

        try {
            if (key.indexOf(PORT_UART.class.getSimpleName()) == 0) {

                switch (PORT_UART.valueOf(key)) {
                    case PORT_UART_OPEN:
                        if (val == null || val.isEmpty()) {
                            result = ErroNo.ILLEGAL_ARGUMENT.code();
                            break;
                        }
                        if (mUartNative.isAlreadyOpen()) {
                            result = ErroNo.TOO_BUSY.code();
                            break;
                        }
                        setConsoleAsSerial(true);
                        result = mUartNative.open(val.getInt(To.P0), val.getInt(To.P1), val.getBoolean(To.P2));
                        break;
                    case PORT_UART_CLOSE:
                        result = mUartNative.close(val.getInt(To.P0));
                        setConsoleAsSerial(false);
                        break;
                    case PORT_UART_SEND_BREAK:
                        result = mUartNative.sendBreak(val.getInt(To.P0));
                        break;
                    case PORT_UART_READ:
                        break;
                    case PORT_UART_WRITE:
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

        if (mUartNative == null) {
            result = ErroNo.ILLEGAL_STATE.code();
            if (DEBUG) Log.e(TAG, key + " " + ErroNo.toString(result));
            return result;
        }

        try {
            if (key.indexOf(PORT_UART.class.getSimpleName()) == 0) {

                switch (PORT_UART.valueOf(key)) {
                    case PORT_UART_OPEN:
                        break;
                    case PORT_UART_CLOSE:
                        break;
                    case PORT_UART_SEND_BREAK:
                        break;
                    case PORT_UART_READ:
                        break;
                    case PORT_UART_WRITE:
                        result = mUartNative.write(val.getInt(To.P0), val.getByteArray(To.P1), val.getInt(To.P2), val.getInt(To.P3));
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

        if (mUartNative == null) {
            result.putInt(To.R0, ErroNo.ILLEGAL_STATE.code());
            if (DEBUG) Log.e(TAG, key + " " + ErroNo.toString(result.getInt(To.R0)));
            return result;
        }

        try {
            if (key.indexOf(PORT_UART.class.getSimpleName()) == 0) {

                switch (PORT_UART.valueOf(key)) {
                    case PORT_UART_OPEN:
                        break;
                    case PORT_UART_CLOSE:
                        break;
                    case PORT_UART_SEND_BREAK:
                        break;
                    case PORT_UART_READ:
                        if (val == null || val.isEmpty()) {
                            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                            break;
                        }
                        result.putInt(To.R0, mUartNative.read(val.getInt(To.P0), val.getByteArray(To.P1), val.getInt(To.P2)));
                        break;
                    case PORT_UART_WRITE:
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
