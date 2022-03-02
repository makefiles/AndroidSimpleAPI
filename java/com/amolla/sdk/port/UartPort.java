/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

import com.amolla.sdk.port;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * TODO: Enter the descriptions
 * @since 1.0
 */
public class UartPort {

    private String mAbsolutePath = "";
    private int mFileDescription = -1;

    public UartPort() {}

    private static UartPort mThis = null;
    public static UartPort get() {
        if (mThis == null) {
            mThis = new UartPort();
        }
        return mThis;
    }

    /**
     * Gets all port paths in the device.
     * Example: "/dev/ttyHSL0"
     * @return List of port paths of string array type.
     * @since 1.0
     */
    public String[] getPortList() {
        Bundle param = new Bundle();
        param.putString(To.P0, "SERIAL_UART_PORT");
        return Tube.getStringArray("STATIC_SERIAL_PORTS", param);
    }

    /**
     * Opens the specified serial port.
     * @param path The absolute path of the serial port.
     * @param baudrate Specify the data transfer rate.
     * @param flags The flags for configuring the serial port.
     * @param hwflow Specify the hardware flow.
     * @return The number of the file description if opening file succeeds, negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int open(String path, int baudrate, int flags, boolean hwflow) {
        if (TextUtils.isEmpty(path)) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        Bundle param = new Bundle();
        param.putInt(To.P0, baudrate);
        param.putInt(To.P1, flags);
        param.putBoolean(To.P2, hwflow);
        int result = Tube.doAction("PORT_UART" + Tube.STR_TOKEN + path, "PORT_UART_OPEN", param);
        if (ErroNo.check(result)) {
            mAbsolutePath = path;
            mFileDescription = result;
        }
        return result;
    }

    /**
     * Closes the open serial port.
     * @return 0 if the setting call succeeds, negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int close() {
        if (TextUtils.isEmpty(mAbsolutePath)) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        Bundle param = new Bundle();
        param.putInt(To.P0, mFileDescription);
        int result = Tube.doAction("PORT_UART" + Tube.STR_TOKEN + mAbsolutePath, "PORT_UART_CLOSE", param);
        if (ErroNo.check(result)) {
            mAbsolutePath = "";
            mFileDescription = -1;
        }
        return result;
    }

    /**
     * Sends a stream of zero valued bits for 0.25 to 0.5 seconds.
     * @return 0 if the setting call succeeds, negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int sendBreak() {
        if (TextUtils.isEmpty(mAbsolutePath)) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        Bundle param = new Bundle();
        param.putInt(To.P0, mFileDescription);
        return Tube.doAction("PORT_UART" + Tube.STR_TOKEN + mAbsolutePath, "PORT_UART_SEND_BREAK", param);
    }

    /**
     * Reads the data from the open serial port into the buffer.
     * @param buf The buffer instance that will contain the contents.
     * @param len The number of bytes to get from the serial port.
     * @return The length in bytes of the content in the buffer. Negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int read(byte[] buf, int len) {
        if (TextUtils.isEmpty(mAbsolutePath)) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        Bundle param = new Bundle();
        param.putInt(To.P0, mFileDescription);
        param.putByteArray(To.P1, buf);
        param.putInt(To.P2, len);
        Bundle result = Tube.getValue("PORT_UART" + Tube.STR_TOKEN + mAbsolutePath, "PORT_UART_READ", param);
        if (result == null) return ErroNo.FAILURE.code();
        return result.getInt(To.R0);
    }

    /**
     * Writes the data to the open serial port.
     * @param buf The buffer for forwarding to the serial port.
     * @param len The number of bytes to set from the serial port.
     * @return 0 if the setting call succeeds, negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int write(byte[] buf, int len) {
        return write(buf, 0, len);
    }

    /**
     * Writes the data to the open serial port.
     * @param buf The buffer for forwarding to the serial port.
     * @param pos The starting position of the buffer to get bytes.
     * @param len The number of bytes to get from the serial port.
     * @return 0 if the setting call succeeds, negative numbers are failures. It is possible to distinguish error type by {@link ErroNo} class.
     * @since 1.0
     */
    public int write(byte[] buf, int pos, int len) {
        if (TextUtils.isEmpty(mAbsolutePath)) {
            return ErroNo.ILLEGAL_ARGUMENT.code();
        }
        Bundle param = new Bundle();
        param.putInt(To.P0, mFileDescription);
        param.putByteArray(To.P1, buf);
        param.putInt(To.P2, pos);
        param.putInt(To.P3, len);
        return Tube.setValue("PORT_UART" + Tube.STR_TOKEN + mAbsolutePath, "PORT_UART_WRITE", param);
    }
}
