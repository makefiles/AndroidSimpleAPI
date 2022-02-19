package com.amolla.jni;

public class InfoNative {

    static { System.loadLibrary("utilnative_jni"); }

    private native static int nativeWriteBlock(byte[] buf, int pos, int len);
    private native static int nativeReadBlock(byte[] buf, int pos, int len);

    public InfoNative() {}

	public static int writeBlock(byte[] buf, int pos, int len) {
        return nativeWriteBlock(pos, buf, len);
    }

	public static int readBlock(byte[] buf, int pos, int len) {
        return nativeReadBlock(pos, buf, len);
    }
}
