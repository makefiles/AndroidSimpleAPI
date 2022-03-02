/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.jni;

import com.amolla.sdk.To;
import com.amolla.sdk.ErroNo;

public class PortUartNative {

    static { System.loadLibrary("portuartnative_jni"); }

    private String mAbsolutePath = "";
    private int mFileDescription = -1;
    private boolean isInaccessible(int fd) {
        return mFileDescription < 0 || mFileDescription != fd;
    }
    private native boolean nativeRequests(long[] requests);
    private native int nativeOpen(String path, int baudrate, int flags, boolean hwflow);
    private native int nativeClose(int fd);
    private native int nativeRead(int fd, byte[] buf, int len);
    private native int nativeWrite(int fd, byte[] buf, int pos, int len);
    private native int nativeSendBreak(int fd);
    private native int nativeSleep(int fd, boolean enable);

    public PortUartNative() {}
    public PortUartNative(String path) { setPortPath(path); }

    public void setPortPath(String path) {
        mAbsolutePath = path;
    }

    public String getPortPath() {
        return mAbsolutePath;
    }

    public boolean isAlreadyOpen() {
        return ErroNo.check(mFileDescription);
    }

    public boolean setRequests(long... requests) {
        return nativeRequests(requests);
    }

    public int open(int baudrate, int flags, boolean hwflow) {
        if (isAlreadyOpen()) return ErroNo.TOO_BUSY.code();
        int result = nativeOpen(mAbsolutePath, baudrate, flags, hwflow);
        if (ErroNo.check(result)) {
            mFileDescription = result;
        }
        return result;
    }

    public int close(int fd) {
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        int result = nativeClose(fd);
        if (ErroNo.check(result)) {
            mFileDescription = -1;
        }
        return result;
    }

    public int sendBreak(int fd) {
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeSendBreak(fd);
    }

    public int read(int fd, byte[] buf, int len) {
        if (buf == null || buf.length == 0) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeRead(fd, buf, len);
    }

    public int write(int fd, byte[] buf, int pos, int len) {
        if (buf == null || buf.length == 0) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeWrite(fd, buf, pos, len);
    }

    public int sleep(boolean enable) {
        if (!isAlreadyOpen()) return ErroNo.ILLEGAL_STATE.code();
        return nativeSleep(mFileDescription, enable);
    }
}
