package com.amolla.jni;

import com.amolla.sdk.To;
import com.amolla.sdk.ErroNo;

public class PortUartNative {

    static { System.loadLibrary("portuartnative_jni"); }

    private static String mAbsolutePath = "";
    private static int mFileDescription = -1;
    private static boolean isInaccessible(int fd) {
        return mFileDescription < 0 || mFileDescription != fd;
    }
    private static native boolean nativeRequests(long[] requests);
    private static native int nativeOpen(String path, int baudrate, int flags, boolean hwflow);
    private static native int nativeClose(int fd);
    private static native int nativeRead(int fd, byte[] buf, int len);
    private static native int nativeWrite(int fd, byte[] buf, int pos, int len);
    private static native int nativeSendBreak(int fd);
    private static native int nativeSleep(int fd, boolean enable);

    public PortUartNative() {}

    public static void setPortPath(String path) {
        mAbsolutePath = path;
    }

    public static String getPortPath() {
        return mAbsolutePath;
    }

    public static boolean isAlreadyOpen() {
        return ErroNo.check(mFileDescription);
    }

    public static boolean setRequests(long... requests) {
        return nativeRequests(requests);
    }

    public static int open(int baudrate, int flags, boolean hwflow) {
        if (isAlreadyOpen()) return ErroNo.TOO_BUSY.code();
        int result = nativeOpen(mAbsolutePath, baudrate, flags, hwflow);
        if (ErroNo.check(result)) {
            mFileDescription = result;
        }
        return result;
    }

    public static int close(int fd) {
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        int result = nativeClose(fd);
        if (ErroNo.check(result)) {
            mFileDescription = -1;
        }
        return result;
    }

    public static int sendBreak(int fd) {
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeSendBreak(fd);
    }

    public static int read(int fd, byte[] buf, int len) {
        if (buf == null || buf.length == 0) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeRead(fd, buf, len);
    }

    public static int write(int fd, byte[] buf, int pos, int len) {
        if (buf == null || buf.length == 0) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        if (isInaccessible(fd)) return ErroNo.ILLEGAL_STATE.code();
        return nativeWrite(fd, buf, pos, len);
    }

    public static int sleep(boolean enable) {
        if (!isAlreadyOpen()) return ErroNo.ILLEGAL_STATE.code();
        return nativeSleep(mFileDescription, enable);
    }
}
