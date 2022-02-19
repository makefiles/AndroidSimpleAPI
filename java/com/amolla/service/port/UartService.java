package com.amolla.service.port;

import com.amolla.sdk.ITube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.sdk.set.OtherDevice;
import com.amolla.jni.PortUartNative;
import com.amolla.service.DynamicReceiver;
import com.amolla.service.DynamicController;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UartService extends ITube.Stub {
    private static final String TAG = UartService.class.getSimpleName();
    private static final boolean DEBUG = DynamicController.DEBUG_ALL || false;

    private static final int MSG_SCREEN_CHANGED = 1000;
    private static DynamicController mCtrl;
    private static enum PORT_UART {
        PORT_UART_OPEN,
        PORT_UART_CLOSE,
        PORT_UART_SEND_BREAK,
        PORT_UART_READ,
        PORT_UART_WRITE
    }

    public UartService(Context context, String path) {
        mCtrl = new DynamicController(context, TAG);
        mCtrl.setHandler(new Handler(mCtrl.getLooper()) {
            @Override
            public synchronized void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SCREEN_CHANGED:
                        final String action = (String) msg.obj;
                        PortUartNative.sleep(action.equals(Intent.ACTION_SCREEN_OFF));
                        break;
                    default:break;
                }
            }
        });
        mCtrl.registerReceiver(new DynamicReceiver(this), DynamicReceiver.getFilter(PORT_UART.class));
        PortUartNative.setPortPath(path);
        setRequests();
    }

    private void setRequests() {
        long reqUartClockEnable = -1;
        long reqUartClockDisable = -1;
        try { reqUartClockEnable = Long.parseLong(mCtrl.getDefinition("REQ_UART_CLOCK_ENABLE")); } catch (Exception e) {};
        try { reqUartClockDisable = Long.parseLong(mCtrl.getDefinition("REQ_UART_CLOCK_DISABLE")); } catch (Exception e) {};
        if (PortUartNative.setRequests(reqUartClockEnable, reqUartClockDisable)) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            mCtrl.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Message msg = mCtrl.getHandler().obtainMessage();
                    msg.what = MSG_SCREEN_CHANGED;
                    msg.obj = intent.getAction();
                    mCtrl.getHandler().sendMessage(msg);
                }
            }, filter);
        }
    }

    private void setConsoleAsSerial(boolean enable) {
        if (Tube.check("STATIC_SAME_AS_CONSOLE", PortUartNative.getPortPath()) {
            OtherDevice.get().setUsingConsoleAsSerial(enable);
        }
    }

    @Override
    public synchronized int doAction(String key, Bundle val) {
        int result = ErroNo.FAILURE.code();

        if (key == null || key.isEmpty()) {
            result = ErroNo.ILLEGAL_ARGUMENT.code();
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
                        if (PortUartNative.isAlreadyOpen()) {
                            result = ErroNo.TOO_BUSY.code();
                            break;
                        }
                        setConsoleAsSerial(true);
                        result = PortUartNative.open(val.getInt(To.P0), val.getInt(To.P1), val.getBoolean(To.P2));
                        break;
                    case PORT_UART_CLOSE:
                        result = PortUartNative.close(val.getInt(To.P0));
                        setConsoleAsSerial(false);
                        break;
                    case PORT_UART_SEND_BREAK:
                        result = PortUartNative.sendBreak(val.getInt(To.P0));
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

        return result;
    }

    @Override
    public synchronized int setValue(String key, Bundle val) {
        int result = ErroNo.FAILURE.code();

        if (key == null || key.isEmpty() ||
            val == null || val.isEmpty()) {
            result = ErroNo.ILLEGAL_ARGUMENT.code();
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
                        result = PortUartNative.write(val.getInt(To.P0), val.getByteArray(To.P1), val.getInt(To.P2), val.getInt(To.P3));
                        break;
                    default: throw new IllegalArgumentException();
                }

            }
        } catch (IllegalArgumentException e) {
            result = ErroNo.UNSUPPORTED.code();
            Log.e(TAG, "Not implemented setValue( " + key + " )");
        }

        return result;
    }

    @Override
    public synchronized Bundle getValue(String key, Bundle val) {
        Bundle result = new Bundle();

        if (key == null || key.isEmpty()) {
            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
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
                        result.putInt(To.R0, PortUartNative.read(val.getInt(To.P0), val.getByteArray(To.P1), val.getInt(To.P2)));
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

        return result;
    }
}
