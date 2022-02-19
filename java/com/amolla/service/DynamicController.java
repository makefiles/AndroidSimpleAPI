package com.amolla.service;

import com.amolla.sdk.To;
import com.amolla.sdk.Tube;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
import android.app.AppOpsManager;
import android.app.StatusBarManager;
import android.media.AudioManager;
import android.content.Context;
import android.content.ContentResolver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.view.IWindowManager;
import android.view.inputmethod.InputMethodManager;
import android.util.Log;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Handler;
import android.os.Vibrator;
import android.os.ServiceManager;
import android.os.IPowerManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.nfc.NfcAdapter;
import android.net.wifi.WifiManager;
import android.net.wifi.IWifiManager;
import android.telephony.TelephonyManager;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbManager;
import android.hardware.input.InputManager;

import java.util.HashMap;
import java.util.Map;

public class DynamicController {
    public static final boolean DEBUG_ALL = true;
    protected static String TAG = DynamicController.class.getSimpleName();
    protected static DynamicController mInstance;
    protected Context mContext;
    protected Handler mHandler;
    protected HandlerThread mThread;
    protected Map<String, String> mKernelInterfaceMap;
    public DynamicController(Context context, String tag) {
        mContext = context;
        getDefinedMap();
        TAG = tag;
        mThread = new HandlerThread(tag);
        mThread.start();
    }
    public DynamicController(DynamicController ctrl, String tag) {
        mContext = ctrl.getContext();
        mKernelInterfaceMap = ctrl.getDefinedMap();
        TAG = tag;
        mThread = new HandlerThread(tag);
        mThread.start();
    }
    public void setHandler(Handler handler) { mHandler = handler; }
    public Handler getHandler() { return mHandler; }
    public Looper getLooper() { return mThread.getLooper(); }
    public Context getContext() { return mContext; }
    public ContentResolver getContentResolver() {
        if (mContext == null) return null;
        return mContext.getContentResolver();
    }
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mContext == null) return null;
        if (mHandler == null) {
            mHandler = new Handler(getLooper());
        }
        return mContext.registerReceiver(receiver, filter, null, mHandler);
    }
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (mContext == null) return null;
        mContext.unregisterReceiver(receiver);
    }
    public Map<String, String> getDefinedMap() {
        if (mKernelInterfaceMap == null || mKernelInterfaceMap.isEmpty()) {
            try {
                Bundle result = Tube.getValue("STATIC_DEFINED_CONSTANT_MAP", null);
                mKernelInterfaceMap = (HashMap<String, String>) result.getSerializable(To.R0);
            } catch (Exception e) {
                if (DEBUG) e.printStackTrace();
            }
        }
        return mKernelInterfaceMap;
    }
    public String getDefinition(String key) {
        if (mKernelInterfaceMap == null || mKernelInterfaceMap.isEmpty()) {
            return "";
        }
        return mKernelInterfaceMap.get(key);
    }
    private static PackageManager mPackageManager;
    protected PackageManager getPackageManager() {
        if (mPackageManager == null) {
            mPackageManager = getContext().getPackageManager();
            if (mPackageManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the PackageManagerService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mPackageManager;
    }
    private static IPackageManager mIPackageManager;
    protected IPackageManager getIPackageManager() {
        if (mIPackageManager == null) {
            mIPackageManager = AppGlobals.getPackageManager();
            if (mIPackageManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the PackageManagerService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mIPackageManager;
    }
    private static DevicePolicyManager mDevicePolicyManager;
    protected DevicePolicyManager getDevicePolicyManager() {
        if (mDevicePolicyManager == null) {
            mDevicePolicyManager = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (mDevicePolicyManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the DevicePolicyService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mDevicePolicyManager;
    }
    private static IPowerManager mIPowerManager;
    protected IPowerManager getIPowerManager() {
        if (mIPowerManager == null) {
            mIPowerManager = IPowerManager.Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE));
            if (mIPowerManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the PowerService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mIPowerManager;
    }
    private static PowerManager mPowerManager;
    protected PowerManager getPowerManager() {
        if (mPowerManager == null) {
            mPowerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
            if (mPowerManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the PowerService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mPowerManager;
    }
    private static DisplayManager mDisplayManager;
    protected DisplayManager getDisplayManager() {
        if (mDisplayManager == null) {
            mDisplayManager = (DisplayManager) getContext().getSystemService(DisplayManager.class);
            if (mDisplayManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the DisplayManagerService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mDisplayManager;
    }
    private static AudioManager mAudioManager;
    protected AudioManager getAudioManager() {
        if (mAudioManager == null) {
		    mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            if (mAudioManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the AudioService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mAudioManager;
    }
    private static TelephonyManager mTelephonyManager;
    protected TelephonyManager getTelephonyManager() {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the TelephonyService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mTelephonyManager;
    }
    private static Vibrator mVibrator;
    protected Vibrator getVibrator() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (mVibrator == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the VibratorService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mVibrator;
    }
    private static NfcAdapter mNfcAdapter;
    protected NfcAdapter getNfcAdapter() {
        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
            if (mNfcAdapter == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the NfcAdapter. Is it existing?", new IllegalStateException("Stack trace:"));
            }
        }
        return mNfcAdapter;
    }
    private static AppOpsManager mAppOpsManager;
    protected AppOpsManager getAppOpsManager() {
        if (mAppOpsManager == null) {
            mAppOpsManager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
            if (mAppOpsManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the AppOpsService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mAppOpsManager;
    }
    private static IWindowManager mIWindowManager;
    protected IWindowManager getIWindowManager() {
        if (mIWindowManager == null) {
            mIWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
            if (mIWindowManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the WindowService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mIWindowManager;
    }
    private static UserManager mUserManager;
    protected UserManager getUserManager() {
        if (mUserManager == null) {
            mUserManager = (UserManager) getContext().getSystemService(Context.USER_SERVICE);
            if (mUserManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the UserService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mUserManager;
    }
    private static UsbManager mUsbManager;
    protected UsbManager getUsbManager() {
        if (mUsbManager == null) {
            mUsbManager = (UsbManager) getContext().getSystemService(UsbManager.class);
            if (mUsbManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the UsbService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mUsbManager;
    }
    private static WifiManager mWifiManager;
    protected WifiManager getWifiManager() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the WifiService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mWifiManager;
    }
    private static IWifiManager mIWifiManager;
    protected IWifiManager getIWifiManager() {
        if (mIWifiManager == null) {
            mIWifiManager = IWifiManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_SERVICE));
            if (mIWifiManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the WifiService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mIWifiManager;
    }
    private static InputManager mInputManager;
    protected InputManager getInputManager() {
        if (mInputManager == null) {
            mInputManager = (InputManager) getContext().getSystemService(Context.INPUT_SERVICE);
            if (mInputManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the InputService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mInputManager;
    }
    private static StatusBarManager mStatusBarManager;
    protected StatusBarManager getStatusBarManager() {
        if (mStatusBarManager == null) {
            mStatusBarManager = (StatusBarManager) getContext().getSystemService(Context.STATUS_BAR_SERVICE);
            if (mStatusBarManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the StatusBarService. Is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return mStatusBarManager;
    }
    private static InputMethodManager mInputMethodManager;
    protected InputMethodManager getInputMethodManager() {
        if (mInputMethodManager == null) {
            mInputMethodManager = InputMethodManager.getInstance();
            if (mInputMethodManager == null && DEBUG_ALL) {
                Log.e(TAG, "Can't get the InputMethodManager.", new IllegalStateException("Stack trace:"));
            }
        }
        return mInputMethodManager;
    }
}
