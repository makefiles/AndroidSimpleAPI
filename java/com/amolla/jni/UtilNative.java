/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.jni;

public class UtilNative {

    static { System.loadLibrary("utilnative_jni"); }

    private native static int nativeWriteBlock(byte[] buf, int pos, int len);
    private native static int nativeReadBlock(byte[] buf, int pos, int len);

    public UtilNative() {}

	public static int writeBlock(byte[] buf, int pos, int len) {
        return nativeWriteBlock(buf, pos, len);
    }

	public static int readBlock(byte[] buf, int pos, int len) {
        return nativeReadBlock(buf, pos, len);
    }
}
