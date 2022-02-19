package com.amolla.jni;

public class InfoNative {
    public static final int MODULE_CAMERA1 = 0;
    public static final int MODULE_CAMERA2 = 1;
    public static final int MODULE_DISPLAY = 2;
    public static final int MODULE_KEYPAD = 3;
    public static final int MODULE_MEMORY = 4;
    public static final int MODULE_SCANNER = 5;
    public static final int MODULE_TOUCH = 6;
    public static final int MODULE_NFC = 7;
    public static final int MODULE_IFM = 8;
    public static final int MODULE_SEM = 9;
    public static final int MODULE_BLUETOOTH = 10;
    public static final int MODULE_GPS = 11;
    public static final int MODULE_PHONE = 12;
    public static final int MODULE_WLAN = 13;
    public static final int MODULE_ACCELERATION = 14;
    public static final int MODULE_LIGHT = 15;
    public static final int MODULE_PROXIMITY = 16;
    public static final int MODULE_TEMPERATURE = 17;
    public static final int MODULE_GYROSCOPE = 18;
    public static final int MODULE_MAGNETIC = 19;
    public static final int MODULE_PRESURE = 20;
    public static final int MODULE_END = 21;

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
