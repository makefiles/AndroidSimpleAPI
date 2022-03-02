/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.sdk;

/**
 * TODO: Enter the descriptions
 * @since 1.0
 */
public enum ErroNo {

    SUCCESS(0),
    FAILURE(-1),
    UNKNOWN(-2),
    UNSUPPORTED(-3),
    DEPRECATED(-4),
    INTERRUPTED(-5),
    SDCARD_NOT_MOUNTED(-6),
    FILE_NOT_FOUND(-7),
    TOO_MANY_FILES(-8),
    NOT_ENOUGH_BATTERY_CAPACITY(-9),
    FILE_COPY_FAILED(-10),
    NOT_ENOUGH_FREE_SPACE(-11),
    PERMISSION_DENIED(-12),
    NO_SUCH_FIELD(-13),
    NO_SUCH_METHOD(-14),
    ILLEGAL_ARGUMENT(-15),
    ILLEGAL_STATE(-16),
    INDEX_OUT_OF_BOUND(-17),
    NULL_POINTER(-18),
    NOT_PROVISIONED(-19),
    TOO_BUSY(-20),
    ARGUMENT_OUT_OF_MEMORY(-21);

    private final int code;
    private ErroNo(int code) { this.code = check(code) ? code : code - 2000; }
    private static final class StringTable {
        public static final java.util.Map<ErroNo, String> descriptions = generateTable();
        public static final java.util.Map<ErroNo, String> generateTable() {
            java.util.Map<ErroNo, String> map = new java.util.EnumMap<ErroNo, String>(ErroNo.class);
            map.put(SUCCESS, "Success");
            map.put(FAILURE, "Failure");
            map.put(UNKNOWN, "Unknown");
            map.put(UNSUPPORTED, "Unsupported");
            map.put(DEPRECATED, "Deprecated");
            map.put(INTERRUPTED, "Interrupted");
            map.put(SDCARD_NOT_MOUNTED, "SD card not mounted");
            map.put(FILE_NOT_FOUND, "File not found");
            map.put(TOO_MANY_FILES, "Too many files");
            map.put(NOT_ENOUGH_BATTERY_CAPACITY, "Not enough battery capacity");
            map.put(FILE_COPY_FAILED, "File copy failed");
            map.put(NOT_ENOUGH_FREE_SPACE, "Not enough free space");
            map.put(PERMISSION_DENIED, "Permission denied");
            map.put(NO_SUCH_FIELD, "No such field");
            map.put(NO_SUCH_METHOD, "No such method");
            map.put(ILLEGAL_ARGUMENT, "Illegal argument");
            map.put(ILLEGAL_STATE, "Illegal state");
            map.put(INDEX_OUT_OF_BOUND, "Index out of bound");
            map.put(NULL_POINTER, "Null pointer");
            map.put(NOT_PROVISIONED, "Not provisioned");
            map.put(TOO_BUSY, "Too busy");
            map.put(ARGUMENT_OUT_OF_MEMORY, "Arguement out of memory");
            return map;
        }
    }
    public static final boolean check(int code) {
        return code > -1;
    }
    public static final String toString(int code) {
        for (ErroNo value : ErroNo.values()) {
            if (value.code() == code) {
                return value.toString();
            }
        }
        return "";
    }
    public final String toString() { return StringTable.descriptions.get(this); }
    public final int code() { return this.code; }
}
