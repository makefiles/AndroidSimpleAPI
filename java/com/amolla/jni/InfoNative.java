/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.jni;

public class InfoNative {

    public static final int MODULE_KEYPAD = 0;
    public static final int MODULE_TOUCH = 1;
    public static final int MODULE_DISPLAY = 2;
    public static final int MODULE_BLUETOOTH = 3;
    public static final int MODULE_WLAN = 4;
    public static final int MODULE_PHONE = 5;
    public static final int MODULE_GPS = 6;
    public static final int MODULE_MEMORY = 7;
    public static final int MODULE_CAMERA1 = 8;
    public static final int MODULE_SCANNER = 9;
    public static final int MODULE_NFC = 10;
    public static final int MODULE_ACCELERATION = 11;
    public static final int MODULE_LIGHT = 12;
    public static final int MODULE_TEMPERATURE = 13;
    public static final int MODULE_GYROSCOPE = 14;
    public static final int MODULE_MAGNETIC = 15;
    public static final int MODULE_PRESURE = 16;
    public static final int MODULE_PROXIMITY = 17;
    public static final int MODULE_SAM = 18;
    public static final int MODULE_RFID = 19;
    public static final int MODULE_CAMERA2 = 20;
    public static final int MODULE_MSR = 21;
    public static final int MODULE_PRINTER = 22;
	public static final int MODULE_FISCAL = 23;
	public static final int MODULE_FINGERPRINTER = 24;
	public static final int MODULE_ICR = 25;
    public static final int MODULE_END = 26;

    static { System.loadLibrary("infonative_jni"); }

	private native static String nativeGetModelName();
	private native static String nativeGetProcessorName();
	private native static int    nativeGetHardwareRevision();
	private native static String nativeGetHardwareRevisionName();
	private native static boolean nativeSetDeviceModuleName(int index, String name);
	private native static String nativeGetDeviceModuleName(int index);

    public InfoNative() {}

	public static String getModelName() {
        return nativeGetModelName();
    }

	public static String getProcessorName() {
        return nativeGetProcessorName();
    }

	public static int getHardwareRevision() {
		return nativeGetHardwareRevision();
	}

	public static String getHardwareRevisionName() {
        return nativeGetHardwareRevisionName();
    }

	public static boolean setModuleName(int index, String name) {
        return nativeSetDeviceModuleName(index, name);
    }

	public static String getModuleName(int index) {
        return nativeGetDeviceModuleName(index);
    }
}
