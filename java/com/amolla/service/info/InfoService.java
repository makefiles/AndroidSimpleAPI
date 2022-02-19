package com.amolla.service.info;

import com.amolla.sdk.ITube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.service.DynamicReceiver;
import com.amolla.service.info.DeviceInformation;
import com.amolla.service.info.ModuleInformation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class InfoService extends ITube.Stub {
    private static final String TAG = InfoService.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static DynamicController mCtrl;
    private static enum INFO_DEV {
        INFO_DEV_DEVICE_NAME,
        INFO_DEV_PROCESSOR,
        INFO_DEV_HW_REV_NUM,
        INFO_DEV_SERIAL_NUM,
        INFO_DEV_PART_NUM,
        INFO_DEV_MANUFACTURER,
        INFO_DEV_MANUFACTURE_DATE,
        INFO_DEV_MODEL_NUM,
        INFO_DEV_BASE_MODEL_NUM,
        INFO_DEV_BUILD_NUM,
        INFO_DEV_ANDROID_VER,
        INFO_DEV_KERNEL_VER,
        INFO_DEV_SDK_VER,
        INFO_DEV_IMAGE_VER,
        INFO_DEV_ATTESTATION_KEY
    }
    private static enum INFO_TYPE {
        INFO_TYPE_CAMERA1,
        INFO_TYPE_CAMERA2,
        INFO_TYPE_DISPLAY,
        INFO_TYPE_KEYPAD,
        INFO_TYPE_MEMORY,
        INFO_TYPE_SCANNER,
        INFO_TYPE_TOUCH,
        INFO_TYPE_NFC,
        INFO_TYPE_IFM,
        INFO_TYPE_SEM,
        INFO_TYPE_BLUETOOTH,
        INFO_TYPE_GPS,
        INFO_TYPE_PHONE,
        INFO_TYPE_WLAN,
        INFO_TYPE_ACCELERATION,
        INFO_TYPE_LIGHT,
        INFO_TYPE_PROXIMITY,
        INFO_TYPE_TEMPERATURE,
        INFO_TYPE_GYROSCOPE,
        INFO_TYPE_MAGNETIC,
        INFO_TYPE_PRESURE
    }
    private static enum INFO_MOD {
        INFO_MOD_SCANNER_CLASS,
        INFO_MOD_TOUCH_FW_VER,
        INFO_MOD_1_CAMERA_FW_VER,
        INFO_MOD_2_CAMERA_FW_VER,
        INFO_MOD_BT_MAC_ADDR,
        INFO_MOD_WLAN_DRIVER_VER,
        INFO_MOD_WLAN_FW_VER,
        INFO_MOD_WLAN_CONFIG_VER,
        INFO_MOD_WLAN_MAC_ADDR,
        INFO_MOD_WLAN_IP_ADDR,
        INFO_MOD_MAIN_BATT_STATE,
        INFO_MOD_BACK_BATT_STATE,
        INFO_MOD_MAIN_BATT_CHARGING,
        INFO_MOD_BACK_BATT_CHARGING,
        INFO_MOD_BATT_LOW_LEVEL,
        INFO_MOD_BATT_CRITICAL_LEVEL,
        INFO_MOD_MAIN_BATT_CURRENT,
        INFO_MOD_MAIN_BATT_REALSOC,
        INFO_MOD_BACK_BATT_PERCENT,
        INFO_MOD_BACK_BATT_VOLT
    }

    public InfoService(Context context) {
        mCtrl = new DynamicController(context, TAG);
        mCtrl.setHandler(new Handler(mCtrl.getLooper()) {
            @Override
            public synchronized void handleMessage(Message msg) {
            }
        });
        mCtrl.registerReceiver(new DynamicReceiver(this), DynamicReceiver.getFilter(INFO_DEV.class, INFO_TYPE.class, INFO_MOD.class));
        DeviceInformation.init(mCtrl);
        ModuleInformation.init(mCtrl);
    }

    @Override
    public synchronized int doAction(String key, Bundle val) {
        return ErroNo.UNSUPPORTED.code();
    }

    @Override
    public synchronized int setValue(String key, Bundle val) {
        return ErroNo.UNSUPPORTED.code();
    }

    @Override
    public synchronized Bundle getValue(String key, Bundle val) {
        Bundle result = new Bundle();

        if (key == null || key.isEmpty()) {
            result.putInt(To.R0, ErroNo.ILLEGAL_ARGUMENT.code());
            return result;
        }

        if (val != null) {
            result.putInt(To.R0, ErroNo.UNSUPPORTED.code());
            return result;
        }

        try {
            if (key.indexOf(INFO_DEV.class.getSimpleName()) == 0) {

                switch (INFO_DEV.valueOf(key)) {
                    case INFO_DEV_DEVICE_NAME:
                        result.putString(To.R0, DeviceInformation.get().getDeviceName(false));
                        break;
                    case INFO_DEV_PROCESSOR:
                        result.putString(To.R0, DeviceInformation.get().getProcessor(false));
                        break;
                    case INFO_DEV_HW_REV_NUM:
                        result.putString(To.R0, DeviceInformation.get().getHardwareRevision(false));
                        break;
                    case INFO_DEV_SERIAL_NUM:
                        result.putString(To.R0, DeviceInformation.get().getSerialNumber(true));
                        break;
                    case INFO_DEV_PART_NUM:
                        result.putString(To.R0, DeviceInformation.get().getPartNumber(true));
                        break;
                    case INFO_DEV_MANUFACTURER:
                        result.putString(To.R0, DeviceInformation.get().getManufacturer(false));
                        break;
                    case INFO_DEV_MANUFACTURE_DATE:
                        result.putString(To.R0, DeviceInformation.get().getManufactureDate(true));
                        break;
                    case INFO_DEV_MODEL_NUM:
                        result.putString(To.R0, DeviceInformation.get().getModelNumber(false));
                        break;
                    case INFO_DEV_BASE_MODEL_NUM:
                        result.putString(To.R0, DeviceInformation.get().getBaseModelNumber(false));
                        break;
                    case INFO_DEV_BUILD_NUM:
                        result.putString(To.R0, DeviceInformation.get().getBuildNumber(false));
                        break;
                    case INFO_DEV_ANDROID_VER:
                        result.putString(To.R0, DeviceInformation.get().getAndroidVersion(false));
                        break;
                    case INFO_DEV_KERNEL_VER:
                        result.putString(To.R0, DeviceInformation.get().getKernelVersion(false));
                        break;
                    case INFO_DEV_SDK_VER:
                        result.putString(To.R0, DeviceInformation.get().getSDKVersion(false));
                        break;
                    case INFO_DEV_IMAGE_VER:
                        result.putString(To.R0, DeviceInformation.get().getImageVersion(false));
                        break;
                    case INFO_DEV_ATTESTATION_KEY:
                        result.putString(To.R0, DeviceInformation.get().getAttestationKey(false));
                        break;
                    default: throw new IllegalArgumentException();
                }

            } else if (key.indexOf(INFO_TYPE.class.getSimpleName()) == 0) {

                result.putString(To.R0, ModuleInformation.get().getName(INFO_TYPE.valueOf(key).ordinal(), false));

            } else if (key.indexOf(INFO_MOD.class.getSimpleName()) == 0) {

                switch(INFO_MOD.valueOf(key)) {
                    case INFO_MOD_SCANNER_CLASS:
                        result.putString(To.R0, ModuleInformation.get().getScannerClass(false));
                        break;
                    case INFO_MOD_TOUCH_FW_VER:
                        result.putString(To.R0, ModuleInformation.get().getTouchFirmwareVersion(false));
                        break;
                    case INFO_MOD_1_CAMERA_FW_VER:
                        result.putString(To.R0, ModuleInformation.get().getFirstCameraFirmwareVersion(false));
                        break;
                    case INFO_MOD_2_CAMERA_FW_VER:
                        result.putString(To.R0, ModuleInformation.get().getSecondCameraFirmwareVersion(false));
                        break;
                    case INFO_MOD_BT_MAC_ADDR:
                        result.putString(To.R0, ModuleInformation.get().getBluetoothMacAddress(false));
                        break;
                    case INFO_MOD_WLAN_DRIVER_VER:
                        result.putString(To.R0, ModuleInformation.get().getWlanDriverVersion(false));
                        break;
                    case INFO_MOD_WLAN_FW_VER:
                        result.putString(To.R0, ModuleInformation.get().getWlanFirmwareVersion(false));
                        break;
                    case INFO_MOD_WLAN_CONFIG_VER:
                        result.putString(To.R0, ModuleInformation.get().getWlanConfigVersion(false));
                        break;
                    case INFO_MOD_WLAN_MAC_ADDR:
                        result.putString(To.R0, ModuleInformation.get().getWlanMacAddress(false));
                        break;
                    case INFO_MOD_WLAN_IP_ADDR:
                        result.putString(To.R0, ModuleInformation.get().getWlanIpAddress(true));
                        break;
                    case INFO_MOD_MAIN_BATT_STATE:
                        result.putString(To.R0, ModuleInformation.get().getMainBatteryState(true));
                        break;
                    case INFO_MOD_BACK_BATT_STATE:
                        result.putString(To.R0, ModuleInformation.get().getBackupBatteryState(true));
                        break;
                    case INFO_MOD_MAIN_BATT_CHARGING:
                        result.putString(To.R0, ModuleInformation.get().getMainBatteryCharging(true));
                        break;
                    case INFO_MOD_BACK_BATT_CHARGING:
                        result.putString(To.R0, ModuleInformation.get().getBackupBatteryCharging(true));
                        break;
                    case INFO_MOD_BATT_LOW_LEVEL:
                        result.putString(To.R0, ModuleInformation.get().getBatteryLowLevel(true));
                        break;
                    case INFO_MOD_BATT_CRITICAL_LEVEL:
                        result.putString(To.R0, ModuleInformation.get().getBatteryCriticalLevel(true));
                        break;
                    case INFO_MOD_MAIN_BATT_CURRENT:
                        result.putString(To.R0, ModuleInformation.get().getMainBatteryCurrent(true));
                        break;
                    case INFO_MOD_MAIN_BATT_REALSOC:
                        result.putString(To.R0, ModuleInformation.get().getMainBatteryRealSoc(true));
                        break;
                    case INFO_MOD_BACK_BATT_PERCENT:
                        result.putString(To.R0, ModuleInformation.get().getBackupBatteryPercentage(true));
                        break;
                    case INFO_MOD_BACK_BATT_VOLT:
                        result.putString(To.R0, ModuleInformation.get().getBackupBatteryVoltage(true));
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
