/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.sdk.info;

import com.amolla.sdk.Tube;
import android.os.Bundle;

public class DeviceInfo {

    public DeviceInfo() {}

    private static DeviceInfo mThis = null;
    public static DeviceInfo get() {
        if (mThis == null) {
            mThis = new DeviceInfo();
        }
        return mThis;
    }

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the name of a device.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_DEVICE_NAME = 0;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when fetching processor information.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_PROCESSOR = 1;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the board revision.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_HW_REV_NUM = 2;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the device serial number.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_SERIAL_NUM = 3;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the device part number.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_PART_NUM = 4;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the manufacturer name.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_MANUFACTURER = 5;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the manufacture date.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_MANUFACTURE_DATE = 6;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when importing the model name based on the project name.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_MODEL_NUM = 7;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when importing the model name based on the device name.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_BASE_MODEL_NUM = 8;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the build number.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_BUILD_NUM = 9;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when importing the Android version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_ANDROID_VER = 10;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the kernel version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_KERNEL_VER = 11;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when getting the sdk version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_SDK_VER = 12;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when importing a version of a system image that is up on the device.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_IMAGE_VER = 13;

    /**
     * The type that reads information from the device.
     * An enumeration to specify when importing a provisioning attestation key.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_DEV_ATTESTATION_KEY = 14;

    private static String getStringOfIndex(int what) {
        switch (what) {
            case INFO_DEV_DEVICE_NAME:      return "INFO_DEV_DEVICE_NAME";
            case INFO_DEV_PROCESSOR:        return "INFO_DEV_PROCESSOR";
            case INFO_DEV_HW_REV_NUM:       return "INFO_DEV_HW_REV_NUM";
            case INFO_DEV_SERIAL_NUM:       return "INFO_DEV_SERIAL_NUM";
            case INFO_DEV_PART_NUM:         return "INFO_DEV_PART_NUM";
            case INFO_DEV_MANUFACTURER:     return "INFO_DEV_MANUFACTURER";
            case INFO_DEV_MANUFACTURE_DATE: return "INFO_DEV_MANUFACTURE_DATE";
            case INFO_DEV_MODEL_NUM:        return "INFO_DEV_MODEL_NUM";
            case INFO_DEV_BASE_MODEL_NUM:   return "INFO_DEV_BASE_MODEL_NUM";
            case INFO_DEV_BUILD_NUM:        return "INFO_DEV_BUILD_NUM";
            case INFO_DEV_ANDROID_VER:      return "INFO_DEV_ANDROID_VER";
            case INFO_DEV_KERNEL_VER:       return "INFO_DEV_KERNEL_VER";
            case INFO_DEV_SDK_VER:          return "INFO_DEV_SDK_VER";
            case INFO_DEV_IMAGE_VER:        return "INFO_DEV_IMAGE_VER";
            case INFO_DEV_ATTESTATION_KEY:  return "INFO_DEV_ATTESTATION_KEY";
        }
        return "N/A";
    }

    /**
     * Reserved for future use. There is nowhere available.
     * <p>Returns the device information according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The device information integer, depending on the input type.
     * @since 1.0
     */
    public int getInfoToInt(int what) {
        return Tube.getInt(getStringOfIndex(what), new Bundle());
    }

    /**
     * Returns the device information according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The device information string, depending on the input type.
     * @since 1.0
     */
    public String getInfoToString(int what) {
        return Tube.getString(getStringOfIndex(what), null);
    }
}
