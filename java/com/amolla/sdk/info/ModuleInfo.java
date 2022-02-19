package com.amolla.sdk.info;

import com.amolla.sdk.Tube;
import android.os.Bundle;

public class ModuleInfo {

    public ModuleInfo() {}

    private static ModuleInfo mThis = null;
    public static ModuleInfo get() {
        if (mThis == null) {
            mThis = new ModuleInfo();
        }
        return mThis;
    }

    /**
     * The type that specifies to get the name of the first camera module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_CAMERA1 = 0;

    /**
     * The type that specifies to get the name of the second camera module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_CAMERA2 = 1;

    /**
     * The type that specifies to get the name of the display module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_DISPLAY = 2;

    /**
     * The type that specifies to get the name of the keypad module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_KEYPAD = 3;

    /**
     * The type that specifies to get the name of the memory module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_MEMORY = 4;

    /**
     * The type that specifies to get the name of the scanner module.
     * {@link #getInfoByInt(int)} method can be used to return an enumeration of scanner module names.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_SCANNER = 5;

    /**
     * The type that specifies to get the name of the touch module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_TOUCH = 6;

    /**
     * The type that specifies to get the name of NFC. (Near Field Communication)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_NFC = 7;

    /**
     * The type that specifies to get the name of IFM or MSR. (Interface Module or Magnatic Strip Reader)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_IFM = 8;

    /**
     * The type that specifies to get the name of SEM or SAM. (Secure Element Module or Secure Access Module)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_SEM = 9;

    /**
     * The type that specifies to get the name of the bluetooth module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_BLUETOOTH = 10;

    /**
     * The type that specifies to get the name of GPS. (Global Positioning System)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_GPS = 11;

    /**
     * The type that specifies to get the name of the phone module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PHONE = 12;

    /**
     * The type that specifies to get the name of WLAN. (Wireless Local Area Network)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_WLAN = 13;

    /**
     * The type that specifies to get the name of the acceleration sensor. (Accelerometer)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_ACCELERATION = 14;

    /**
     * The type that specifies to get the name of the light sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_LIGHT = 15;

    /**
     * The type that specifies to get the name of the proximity sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PROXIMITY = 16;

    /**
     * The type that specifies to get the name of the temperature sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_TEMPERATURE = 17;

    /**
     * The type that specifies to get the name of the gyroscope sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_GYROSCOPE = 18;

    /**
     * The type that specifies to get the name of the magnetic field sensor. (Magnetometer)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_MAGNETIC = 19;

    /**
     * The type that specifies to get the name of the presure sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PRESURE = 20;

    private static String getStringOfIndex(int what) {
        switch (what) {
            case INFO_TYPE_CAMERA1:         return "INFO_TYPE_CAMERA1";
            case INFO_TYPE_CAMERA2:         return "INFO_TYPE_CAMERA2";
            case INFO_TYPE_DISPLAY:         return "INFO_TYPE_DISPLAY";
            case INFO_TYPE_KEYPAD:          return "INFO_TYPE_KEYPAD";
            case INFO_TYPE_MEMORY:          return "INFO_TYPE_MEMOR";
            case INFO_TYPE_SCANNER:         return "INFO_TYPE_SCANNER";
            case INFO_TYPE_TOUCH:           return "INFO_TYPE_TOUCH";
            case INFO_TYPE_NFC:             return "INFO_TYPE_NFC";
            case INFO_TYPE_IFM:             return "INFO_TYPE_IFM";
            case INFO_TYPE_SEM:             return "INFO_TYPE_SEM";
            case INFO_TYPE_BLUETOOTH:       return "INFO_TYPE_BLUETOOTH";
            case INFO_TYPE_GPS:             return "INFO_TYPE_GPS";
            case INFO_TYPE_PHONE:           return "INFO_TYPE_PHONE";
            case INFO_TYPE_WLAN:            return "INFO_TYPE_WLAN";
            case INFO_TYPE_ACCELERATION:    return "INFO_TYPE_ACCELERATION";
            case INFO_TYPE_LIGHT:           return "INFO_TYPE_LIGHT";
            case INFO_TYPE_PROXIMITY:       return "INFO_TYPE_PROXIMITY";
            case INFO_TYPE_TEMPERATURE:     return "INFO_TYPE_TEMPERATURE";
            case INFO_TYPE_GYROSCOPE:       return "INFO_TYPE_GYROSCOPE";
            case INFO_TYPE_MAGNETIC:        return "INFO_TYPE_MAGNETIC";
            case INFO_TYPE_PRESURE:         return "INFO_TYPE_PRESURE";
        }
        return "N/A";
    }

    /**
     * Returns the module type according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The module type integer, depending on the input type. (TODO: Specifying an enumeration)
     * @since 1.0
     */
    public int getInfoToInt(int what) {
        return Tube.getInt(getStringOfIndex(what), new Bundle());
    }

    /**
     * Returns the module type according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The module type string, depending on the input type.
     * @since 1.0
     */
    public String getInfoToString(int what) {
        return Tube.getString(getStringByIndex(what), null);
    }
}
