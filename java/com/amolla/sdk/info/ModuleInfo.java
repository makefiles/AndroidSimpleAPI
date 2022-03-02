/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

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
     * The type that specifies to get the name of the keypad module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_KEYPAD = 0;

    /**
     * The type that specifies to get the name of the touch module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_TOUCH = 1;

    /**
     * The type that specifies to get the name of the display module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_DISPLAY = 2;

    /**
     * The type that specifies to get the name of the bluetooth module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_BLUETOOTH = 3;

    /**
     * The type that specifies to get the name of WLAN. (Wireless Local Area Network)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_WLAN = 4;

    /**
     * The type that specifies to get the name of the phone module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PHONE = 5;

    /**
     * The type that specifies to get the name of GPS. (Global Positioning System)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_GPS = 6;

    /**
     * The type that specifies to get the name of the memory module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_MEMORY = 7;

    /**
     * The type that specifies to get the name of the first camera module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_CAMERA1 = 8;

    /**
     * The type that specifies to get the name of the scanner module.
     * {@link #getInfoByInt(int)} method can be used to return an enumeration of scanner module names.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_SCANNER = 9;

    /**
     * The type that specifies to get the name of NFC. (Near Field Communication)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_NFC = 10;

    /**
     * The type that specifies to get the name of the acceleration sensor. (Accelerometer)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_ACCELERATION = 11;

    /**
     * The type that specifies to get the name of the light sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_LIGHT = 12;

    /**
     * The type that specifies to get the name of the temperature sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_TEMPERATURE = 13;

    /**
     * The type that specifies to get the name of the gyroscope sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_GYROSCOPE = 14;

    /**
     * The type that specifies to get the name of the magnetic field sensor. (Magnetometer)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_MAGNETIC = 15;

    /**
     * The type that specifies to get the name of the presure sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PRESURE = 16;

    /**
     * The type that specifies to get the name of the proximity sensor.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_PROXIMITY = 17;

    /**
     * The type that specifies to get the name of SAM. (Secure Access Module)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_SAM = 18;

    /**
     * The type that specifies to get the name of RFID. (Radio-Frequency Identification)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_RFID = 19;

    /**
     * The type that specifies to get the name of the second camera module.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_TYPE_CAMERA2 = 20;

    /**
     * The type that specifies to get the name of MSR. (Magnatic Strip Reader)
     * @see #getInfoByString(int)
     * @since 1.0
     */
	public static final int INFO_TYPE_MSR = 21;

    /**
     * TODO: Enter the description
     * @ since 1.0
     */
    public static final int INFO_TYPE_PRINTER = 22;

    /**
     * TODO: Enter the description
     * @ since 1.0
     */
	public static final int INFO_TYPE_FISCAL = 23;

    /**
     * TODO: Enter the description
     * @ since 1.0
     */
	public static final int INFO_TYPE_FINGERPRINTER = 24;

    /**
     * TODO: Enter the description
     * @ since 1.0
     */
	public static final int INFO_TYPE_ICR = 25;

    private static String getStringOfIndex(int what) {
        switch (what) {
            case INFO_TYPE_KEYPAD:          return "INFO_TYPE_KEYPAD";
            case INFO_TYPE_TOUCH:           return "INFO_TYPE_TOUCH";
            case INFO_TYPE_DISPLAY:         return "INFO_TYPE_DISPLAY";
            case INFO_TYPE_BLUETOOTH:       return "INFO_TYPE_BLUETOOTH";
            case INFO_TYPE_WLAN:            return "INFO_TYPE_WLAN";
            case INFO_TYPE_PHONE:           return "INFO_TYPE_PHONE";
            case INFO_TYPE_GPS:             return "INFO_TYPE_GPS";
            case INFO_TYPE_MEMORY:          return "INFO_TYPE_MEMORY";
            case INFO_TYPE_CAMERA1:         return "INFO_TYPE_CAMERA1";
            case INFO_TYPE_SCANNER:         return "INFO_TYPE_SCANNER";
            case INFO_TYPE_NFC:             return "INFO_TYPE_NFC";
            case INFO_TYPE_ACCELERATION:    return "INFO_TYPE_ACCELERATION";
            case INFO_TYPE_LIGHT:           return "INFO_TYPE_LIGHT";
            case INFO_TYPE_TEMPERATURE:     return "INFO_TYPE_TEMPERATURE";
            case INFO_TYPE_GYROSCOPE:       return "INFO_TYPE_GYROSCOPE";
            case INFO_TYPE_MAGNETIC:        return "INFO_TYPE_MAGNETIC";
            case INFO_TYPE_PRESURE:         return "INFO_TYPE_PRESURE";
            case INFO_TYPE_PROXIMITY:       return "INFO_TYPE_PROXIMITY";
            case INFO_TYPE_SAM:             return "INFO_TYPE_SAM";
            case INFO_TYPE_RFID:            return "INFO_TYPE_RFID";
            case INFO_TYPE_CAMERA2:         return "INFO_TYPE_CAMERA2";
            case INFO_TYPE_MSR:             return "INFO_TYPE_MSR";
            case INFO_TYPE_PRINTER:         return "INFO_TYPE_PRINTER";
            case INFO_TYPE_FISCAL:          return "INFO_TYPE_FISCAL";
            case INFO_TYPE_FINGERPRINTER:   return "INFO_TYPE_FINGERPRINTER";
            case INFO_TYPE_ICR:             return "INFO_TYPE_ICR";
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
        return Tube.getString(getStringOfIndex(what), null);
    }
}
