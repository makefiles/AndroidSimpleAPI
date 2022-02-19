package com.amolla.server;

import static com.amolla.sdk.Tube.STR_EXE_ACTION_PREFIX;
import static com.amolla.sdk.Tube.STR_SET_ACTION_PREFIX;
import static com.amolla.sdk.Tube.STR_GET_ACTION_PREFIX;
import com.amolla.sdk.ITube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.server.StaticConst;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.IntentSender;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.provider.Settings;
import android.util.Log;

import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.util.Map;

public class StaticServer extends ITube.Stub {
    private static final String TAG = StaticServer.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static enum STATIC_SERVER {
        STATIC_DEFINED_CONSTANT_MAP,
        STATIC_DEFINED_CONSTANT_BY_KEY,
        STATIC_MODEL_NUMBER,
        STATIC_SUPPORTED,
        STATIC_SERIAL_PORTS,
        STATIC_SAME_AS_CONSOLE
    }

    private boolean mSkipOnce = false;

    private Context mContext;
    private HandlerThread mThread;
    private InfoHandler mHandler;

    private String getPackagePath(String packageName) {
        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (DEBUG) { Log.d(TAG, "Installed package path is " + ai.sourceDir);
            return (ai.sourceDir == null ? "" : ai.sourceDir);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (DEBUG) { Log.d(TAG, "Not installed package found");
        return "";
    }

    private String getInstalledSdkPath() {
        return Settings.Global.getString(mContext.getContentResolver(), "INSTALLED_SDK_PATH");
    }

    private boolean setInstalledSdkPath(String path) {
        Settings.Global.getString(mContext.getContentResolver(), "INSTALLED_SDK_PATH", path);
        return isInstalledSdkPath();
    }

    private boolean isInstalledSdkPath() {
        String path = getInstalledSdkPath();
        return (path != null && !path.isEmpty());
    }

    private void startServices() {
        if (isInstalledSdkPath()) {
            if (!mSkipOnce) {
                Log.i(TAG, "When booting is complete, start services");
                mSkipOnce = true;
                return;
            }
            startServices(getInstalledSdkPath(), null);
        } else {
            if (mSkipOnce) {
                Log.i(TAG, "Services are already started");
                return;
            }
            if (startServices("/system/framework/com.amolla.service.jar", mContext)) {
                mSkipOnce = true;
            }
        }
    }

    private boolean startServices(String path, Context context) {
        try {
            if (DEBUG) { Log.d(TAG, "Load classes with " + path); }
            PathClassLoader loader = new PathClassLoader(path, ClassLoader.getSystemClassLoader());
            Method func = loader.loadClass("com.amolla.service.DynamicService").getDeclaredMethod("startAll", new Class[]{Map.class, Context.class});
            func.invoke(getDefinition(), context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public StaticServer(Context context) {
        mContext = context;
        initHandler();
        initReceiver();
        initSettings();
    }

    private void initHandler() {
        mThread = new HandlerThread(TAG);
        mThread.start();
        mHandler = new Handler(mThread.getLooper()) {
            @Override
            public synchronized void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        startServices();
                        break;
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(0, 30);
    }

    private final class BootCompletedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext.unregisterReceiver(BootCompletedReceiver.this);
            mHandler.sendEmptyMessageDelayed(0, 30);
        }
    }

    private final class PackageEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String packageName = intent.getData().getSchemeSpecificPart();
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_PACKAGE_ADDED) ||
                action.euuals(Intent.ACTION_PACKAGE_REPLACED)) {
                if (DEBUG) { Log.d(TAG, "Package added or replaced for " + packageName); }
                setInstalledSdkPath(getPackagePath(packageName));
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED) {
                if (DEBUG) { Log.d(TAG, "Package removed for " + packageName); }
                setInstalledSdkPath("");
            }
        }
    }

    private void initReceiver() {
        IntentFilter methodFilter = new IntentFilter();
        for (STATIC_SERVER method : STATIC_SERVER.values()) {
            if (DEBUG) { Log.d(TAG, "Add filter : " + method.name()); }
            methodFilter.addAction(STR_EXE_ACTION_PREFIX + method.name());
            methodFilter.addAction(STR_SET_ACTION_PREFIX + method.name());
            methodFilter.addAction(STR_GET_ACTION_PREFIX + method.name());
        }
        BroadcastReceiver methodReceiver = new BroadcastReceiver() {
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
                            result.putInt(To.R0, mStub.doAction(key, param));
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
                            result.putInt(To.R0, mStub.setValue(key, param));
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
                            result.putExtras(To.R0, mStub.getValue(key, param));
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
        };
        mContext.registerReceiver(methodReceiver, methodFilter, null, mHandler);

        mContext.registerReceiver(new BootCompletedReceiver(), new IntentFilter(Intent.ACTION_BOOT_COMPLETED), null, mHandler);

        IntentFilter packageEventFilter = new IntentFilter();
        packageEventFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageEventFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        packageEventFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageEventFilter.addDataScheme("com.amolla.service");
        mContext.registerReceiver(new PackageEventReceiver(), packageEventFilter, null, mHandler);
    }

    private void initSettings() {
        setDefaultSetting(To.Global, "low_battery_warning_level",
                String.valueOf(mContext.getResources().getInteger(com.android.internal.R.integer.config_lowBatteryWarningLevel)));
        setDefaultSetting(To.Global, "critical_battery_warning_level",
                String.valueOf(mContext.getResources().getInteger(com.android.internal.R.integer.config_criticalBatteryWarningLevel)));
    }

    private boolean setDefaultSetting(int what, String key, String def) {
        switch (what) {
            case To.GLOBAL:
                if (Settings.Global.getString(mContext.getContentResolver(), key) == null) {
                    Settings.Global.setString(mContext.getContentResolver(), key, def);
                    return true;
                }
                break;
            case To.SECURE:
                if (Settings.Secure.getString(mContext.getContentResolver(), key) == null) {
                    Settings.Secure.setString(mContext.getContentResolver(), key, def);
                    return true;
                }
                break;
            case To.SYSTEM:
                if (Settings.System.getString(mContext.getContentResolver(), key) == null) {
                    Settings.System.setString(mContext.getContentResolver(), key, def);
                    return true;
                }
                break;
        }
        return true;
    }

    private HashMap<String, String> getDefinition() {
        return StaticConst.KERNEL_INTERFACE_MAP;
    }

    private String getDefinition(String key) {
        if (getDefinition() == null || getDefinition().isEmpty()) {
            return "";
        }
        return getDefinition().get(key);
    }

    @Override
    public synchronized int doAction(String key, Bundle val) throws RemoteException {
        return ErroNo.UNSUPPORTED.code();
    }

    @Override
    public synchronized int setValue(String key, Bundle val) throws RemoteException {
        return ErroNo.UNSUPPORTED.code();
    }

    @Override
    public synchronized Bundle getValue(String key, Bundle val) throws RemoteException {
        Bundle result = new Bundle();
        try {
            switch (STATIC_SERVER.valueOf(key)) {
                case STATIC_DEFINED_CONSTANT_MAP:
                    result.putSerializable(To.R0, getDefinition());
                    break;
                case STATIC_DEFINED_CONSTANT_BY_KEY:
                    if (val == null || val.isEmpty()) {
                        result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                        break;
                    }
                    result.putString(To.R0, getDefinition(val.getString(To.P0)));
                    break;
                case STATIC_MODEL_NUMBER:
                    String key = "DEF_BASE_MODEL_NUM";
                    if (val == null || !val.getBoolean(To.P0)) {
                        key = "DEF_MODEL_NUM";
                    }
                    result.putString(To.R0, getDefinition(key));
                    break;
                case STATIC_SUPPORTED:
                    if (val == null || val.isEmpty()) {
                        result.setBoolean(To.R0, false);
                        break;
                    }
                    String modelNumber = getDefinition("DEF_MODEL_NUM");
                    String supportList = getDefinition(val.getString(To.P0));
                    result.setBoolean(To.R0, supportedList.contains(modelNumber));
                    break;
                case STATIC_SERIAL_PORTS:
                    if (val == null || val.isEmpty()) {
                        result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                        break;
                    }
                    String value = getDefinition(val.getString(To.P0));
                    if (TextUtils.isEmpty(value)) {
                        result.putInt(To.R0, ErroNo.ILLEGAL_STATE.code());
                        break;
                    }
                    result.putStringArray(To.R0, value.split(","));
                    break;
                case STATIC_SAME_AS_CONSOLE:
                    if (val == null || val.isEmpty()) {
                        result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                        break;
                    }
                    String path = val.getString(To.P0);
                    if (TextUtils.isEmpty(path)) {
                        result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
                        break;
                    }
                    String console = SystemProperties.get(getDefinition("PPT_CONSOLE"), getDefinition("DEF_CONSOLE_PORT"));
                    result.putBoolean(To.R0, path.contains(console));
                    break;
                default: throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            result.putInt(To.R0, ErroNo.UNSUPPORTED.code());
            Log.e(TAG, "Not implemented " + key);
        }
        return result;
    }
}
