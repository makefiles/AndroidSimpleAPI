package com.amolla.service.info;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.jni.InfoNative;
import com.amolla.sdk.scan.DecoderConst;
import com.amolla.service.DynamicController;

import android.net.wifi.WifiInfo;
import android.net.DhcpInfo;
import android.bluetooth.BluetoothAdapter;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class ModuleInformation extends DynamicController {
    private static final boolean DEBUG = DEBUG_ALL || false;

    private String[] mModuleNameArray = new String[InfoNative.MODULE_MAX];

    private String mScannerClass;
    private String mTouchFirmwareVersion;
    private String mFirstCameraFirmwareVersion;
    private String mSecondCameraFirmwareVersion;
    private String mBluetoothMacAddress;
    private String mWlanDriverVersion;
    private String mWlanFirmwareVersion;
    private String mWlanConfigVersion;
    private String mWlanMacAddress;
    private String mWlanIpAddress;
    private String mMainBatteryState;
    private String mBackupBatteryState;
    private String mMainBatteryCharging;
    private String mBackupBatteryCharging;
    private String mBatteryLowLevel;
    private String mBatteryCriticalLevel;
    private String mMainBatteryCurrent;
    private String mMainBatteryRealSoc;
    private String mBackupBatteryPercentage;
    private String mBackupBatteryVoltage;

    public ModuleInformation(DynamicController ctrl, String tag) {
        super(ctrl, tag);
    }

    public static ModuleInformation init(DynamicController ctrl) {
        if (mInstance == null) {
            mInstance = new ModuleInformation(ctrl, ModuleInformation.class.getSimpleName());
            get().updateAll();
        }
        return get();
    }

    public static ModuleInformation get() {
        return (ModuleInformation) mInstance;
    }

    public static String getName(int index, boolean update) {
        if (mModuleNameArray[index] == null || update) {
            mModuleNameArray[index] = InfoNative.getModelName(index);
        }
        return mModuleNameArray[index];
    }

    public boolean updateAll() {
        for (int index = 0; index < InfoNative.MODULE_END; index++) {
            getName(index, true);
        }
        getScannerClass(true);
        getTouchFirmwareVersion(true);
        getFirstCameraFirmwareVersion(true);
        getSecondCameraFirmwareVersion(true);
        getBluetoothMacAddress(true);
        getWlanDriverVersion(true);
        getWlanFirmwareVersion(true);
        getWlanConfigVersion(true);
        getWlanMacAddress(true);
        getWlanIpAddress(true);
        getMainBatteryState(true);
        getBackupBatteryState(true);
        getMainBatteryCharging(true);
        getBackupBatteryCharging(true);
        getBatteryLowLevel(true);
        getBatteryCriticalLevel(true);
        getMainBatteryCurrent(true);
        getMainBatteryRealSoc(true);
        getBackupBatteryPercentage(true);
        getBackupBatteryVoltage(true);
        return true;
    }

    private String readLine(String path) {
        return readLine(path, ErroNo.UNKNOWN.toString());
    }

    private String readLine(String path, String def) {
        Bundle param = new Bundle();
        param.putString(To.P0, path);
        String result = getString("UTIL_FS_SYSTEM_STRING", param);
        if (result == null) return def;
        return result;
    }

    public String getScannerClass(boolean update) {
        return ""; //TODO:
    }

    public String getTouchFirmwareVersion(boolean update) {
        if (mTouchFirmwareVersion == null || update) {
            mTouchFirmwareVersion = readLine(getDefinition("FS_TOUCH_FW_VER"));
        }
        return mTouchFirmwareVersion;
    }

    public String getFirstCameraFirmwareVersion(boolean update) {
        if (mFirstCameraFirmwareVersion == null || update) {
            String cameraName = getName(InfoNative.MODULE_CAMERA1, false);
            mFirstCameraFirmwareVersion = readLine(getDefinition("FS_SYSTEM_VENDOR_ETC") + cameraName + ".ver");
        }
        return mFirstCameraFirmwareVersion;
    }

    public String getSecondCameraFirmwareVersion(boolean update) {
        if (mSecondCameraFirmwareVersion == null || update) {
            String cameraName = getName(InfoNative.MODULE_CAMERA2, false);
            mSecondCameraFirmwareVersion = readLine(getDefinition("FS_SYSTEM_VENDOR_ETC") + cameraName + ".ver");
        }
        return mSecondCameraFirmwareVersion;
    }

    public String getBluetoothMacAddress(boolean update) {
        if (mBluetoothMacAddress == null || update) {
            mBluetoothMacAddress = ErroNo.UNKNOWN.toString();
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter != null && adapter.isEnabled()) {
                mBluetoothMacAddress = getDefinition("DEF_BT_MAC_ADDR");
            }
        }
        return mBluetoothMacAddress;
    }

    public String getWlanDriverVersion(boolean update) {
        if (mWlanDriverVersion == null || update) {
            mWlanDriverVersion = readLine(getDefinition("FS_WLAN_DRIVER_VER"));
        }
        return mWlanDriverVersion;
    }

    public String getWlanFirmwareVersion(boolean update) {
        if (mWlanFirmwareVersion == null || update) {
            mWlanFirmwareVersion = readLine(getDefinition("FS_WLAN_FW_VER"));
        }
        return mWlanFirmwareVersion;
    }

    // TODO: The configuration type must be be changed.
    public String getWlanConfigVersion(boolean update) {
        if (mWlanConfigVersion == null || update) {
            mWlanConfigVersion = readLine(getDefinition("FS_WLAN_CONFIG_VER"));
        }
        return mWlanConfigVersion;
    }

    public String getWlanMacAddress(boolean update) {
        if (mWlanMacAddress == null || update) {
            mWlanMacAddress = ErroNo.UNKNOWN.toString();
            WifiInfo info = null;
            try {
                info = getWifiManager().getConnectionInfo();
                for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                    byte[] address = ni.getHardwareAddress();
                    if (address == null || address.length != 6) {
                        throw new Exception();
                    }
                    mWlanMacAddress = String.format("%02X:%02X:%02X:%02X:%02X:%02X", address[0], address[1], address[2], address[3], address[4], address[5])
                }
            } catch (Exception e) {
                if (info != null) {
                    mWlanMacAddress = info.getMacAddress();
                }
            }
        }
        return mWlanMacAddress;
    }

    public String getWlanIpAddress(boolean update) {
        if (mWlanIpAddress == null || update) {
            mWlanIpAddress = ErroNo.UNKNOWN.toString();
            DhcpInfo info = null;
            try {
                info = getWifiManager().getDhcpInfo();
                if (info != null) {
                    mWlanIpAddress = String.format("%d.%d.%d.%d", (info.ipAddress & 0xFF), (info.ipAddress >> 8 & 0xFF), (info.ipAddress >> 16 & 0xFF), (info.ipAddress >> 24 & 0xFF));
                }
            } catch (Exception e) {
                if (DEBUG) e.printStackTrace();
            }
        }
        return mWlanIpAddress;
    }

    public String getMainBatteryState(boolean update) {
        if (mMainBatteryState == null || update) {
            mMainBatteryState = readLine(getDefinition("FS_MAIN_BATT_STATE"));
        }
        return mMainBatteryState;
    }

    public String getBackupBatteryState(boolean update) {
        if (mBackupBatteryState == null || update) {
            mBackupBatteryState = readLine(getDefinition("FS_BACK_BATT_STATE"));
        }
        return mBackupBatteryState;
    }

    public String getMainBatteryCharging(boolean update) {
        if (mMainBatteryCharging == null || update) {
            if (Tube.check("STATIC_SUPPORTED", "IS_NOT_CHECK_BATT_CHARGING_IN_SYSFS")) {
                mMainBatteryCharging = (Settings.Global.getInt(getContentResolver(), "USB_CHARGING_ENABLED", 1) == 1 ? "Enabled" : "Disabled");
            } else {
                mMainBatteryCharging = (readLine(getDefinition("FS_MAIN_BATT_CHARGING")).equals("1") ? "Enabled" : "Disabled");
            }
        }
        return mMainBatteryCharging;
    }

    public String getBackupBatteryCharging(boolean update) {
        if (mBackupBatteryCharging == null || update) {
            if (Tube.check("STATIC_SUPPORTED", "IS_NOT_CHECK_BATT_CHARGING_IN_SYSFS")) {
                mBackupBatteryCharging = (Settings.Global.getInt(getContentResolver(), "BACKUP_CHARGING_ENABLED", 1) == 1 ? "Enabled" : "Disabled");
            } else {
                mBackupBatteryCharging = (readLine(getDefinition("FS_BACK_BATT_CHARGING")).equals("1") ? "Enabled" : "Disabled");
            }
        }
        return mBackupBatteryCharging;
    }

    public String getBatteryLowLevel(boolean update) {
        if (mBatteryLowLevel == null || update) {
            mBatteryLowLevel = Settings.Global.getString(getContentResolver(), "LOW_BATTERY_WARNING_LEVEL");
        }
        return mBatteryLowLevel;
    }

    public String getBatteryCriticalLevel(boolean update) {
        if (mBatteryCriticalLevel == null || update) {
            mBatteryCriticalLevel = Settings.Global.getString(getContentResolver(), "CRITICAL_BATTERY_WARNING_LEVEL");
        }
        return mBatteryCriticalLevel;
    }

    public String getMainBatteryCurrent(boolean update) {
        if (mMainBatteryCurrent == null || update) {
            String current = readLine(getDefinition("FS_MAIN_BATT_CURRENT"), "0");
            try {
                mMainBatteryCurrent = String.valueOf(Integer.parseInt(current) / 1000);
            } catch (Exception e) {
                mMainBatteryCurrent = "0";
            }
        }
        return mMainBatteryCurrent;
    }

    public String getMainBatteryRealSoc(boolean update) {
        if (mMainBatteryRealSoc == null || update) {
            mMainBatteryRealSoc = readLine(getDefinition("FS_MAIN_BATT_REAL_SOC"), "0");
        }
        return mMainBatteryRealSoc;
    }

    public String getBackupBatteryPercentage(boolean update) {
        if (mBackupBatteryPercentage == null || update) {
            mBackupBatteryPercentage = readLine(getDefinition("FS_BACK_BATT_PERCENTAGE"), "0");
        }
        return mBackupBatteryPercentage;
    }

    public String getBackupBatteryVoltage(boolean update) {
        if (mBackupBatteryVoltage == null || update) {
            String voltage = readLine(getDefinition("FS_BACK_BATT_VOLTAGE"));
            try {
                mBackupBatteryVoltage = String.valueOf(Integer.parseInt(voltage) / 1000);
            } catch (Exception e) {
                mBackupBatteryVoltage = 0;
            }
        }
        return mBackupBatteryVoltage;
    }
}
