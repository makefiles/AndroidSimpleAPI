/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.text.TextUtils;
import android.os.Bundle;
import android.app.PendingIntent;
import com.android.internal.annotations.GuardedBy;

/**
 * TODO: Enter the descriptions
 * @since 1.0
 */
public class Sender {

    private Context mContext = null;
    private BroadcastReceiver mCallbackReceiver = null;
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private Intent mResult = null;
    private class ReceiveWrapper extends BroadcastReceiver {
        private BroadcastReceiver mTargetReceiver = null;
        private ReceiveWrapper(BroadcastReceiver receiver) { mTargetReceiver = receiver; }
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized(mLock) {
                context.unregisterReceiver(this);
                if (mTargetReceiver != null) {
                    mTargetReceiver.onReceive(context, intent);
                } else {
                    mResult = intent;
                }
                mLock.notifyAll();
            }
        }
    }

    /** TODO: Enter the descriptions */
    public Sender(Context context) {
        mContext = context;
    }

    /** TODO: Enter the descriptions */
    public void setReceiver(BroadcastReceiver receiver) {
        mCallbackReceiver = receiver;
    }

    /** TODO: Enter the descriptions */
    public boolean sendBroadcast(String key, Bundle param) {
        try {
            if (!TextUtils.isEmpty(key)) {
                final String action = mContext.getClass().getName();
                mContext.registerReceiver(new ReceiveWrapper(mCallbackReceiver), new IntentFilter(action));
                PendingIntent pending = PendingIntent.getBroadcast(mContext, action.hashCode(), new Intent(action), PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);
                Intent intent = new Intent(key);
                intent.putExtras(param);
                intent.putExtra(key, pending.getIntentSender());
                mResult = null;
                mContext.sendBroadcast(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** TODO: Enter the descriptions */
    public Intent getResult() {
        return getResult(0);
    }

    /** TODO: Enter the descriptions */
    public Intent getResult(long timeout) {
        synchronized(mLock) {
            while (mResult == null) {
                try {
                    mLock.wait(timeout);
                } catch (Exception e) {}
            }
            return mResult;
        }
    }
}
