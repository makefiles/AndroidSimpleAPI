/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.sdk.util;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;

import android.app.ActivityManagerNative;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserHandle;

/**
 * TODO: Enter the descriptions
 * @since 1.0
 */
public class Password {

    private static volatile int mCurrentUser = UserHandle.USER_NULL;
    private int getCurrentOrCallingUser() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == android.os.Process.ROOT_UID ||
            callingUid == android.os.Process.SYSTEM_UID) {
            return getCurrentUser();
        }
        return UserHandle.getUserId(callingUid);
    }

    /**
     * Constructs a new Password instance with the root UID.
     * @since 1.0
     */
    public Password() {
        this(android.os.Process.ROOT_UID);
    }

    /**
     * Constructs a new Password instance with the specified UID.
     * @since 1.0
     */
    public Password(int user) {
        setCurrentUser(user);
    }

    /**
     * Returns the identification of the current user.
     * @since 1.0
     */
    public int getCurrentUser() {
        if (mCurrentUser != UserHandle.USER_NULL) {
            return mCurrentUser;
        }
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (Exception e) {
            return UserHandle.USER_OWNER;
        }
    }

    /**
     * Sets the current user ID for password setting.
     * @param user The identification number.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setCurrentUser(int user) {
        mCurrentUser = user;
        return mCurrentUser == user;
    }

    /**
     * Deletes all password data set by user ID.
     * @param user The identification number.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean clearUserData(int user) {
        Bundle param = new Bundle();
        param.putInt(To.P0, user);
        return ErroNo.check(Tube.doAction("UTIL_PW_CLEAR_USER", param));
    }

    /**
     * Confirms the password corresponding to the currently set user ID.
     * @param key The entered key name when setting the password.
     * @param password The password of the string type.
     * @return <code>true</code> if the setting call succeeds.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public boolean checkKey(String key, String password) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        param.putString(To.P1, password);
        return Tube.getBoolean("UTIL_PW_CHECK_KEY", param);
    }

    /**
     * Confirms the password in the history that was input during that time.
     * @param key The entered key name when setting the password.
     * @param password The password of the string type.
     * @return <code>true</code> if the setting call succeeds.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public boolean checkKeyOnHistory(String key, String password) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        param.putString(To.P1, password);
        return Tube.getBoolean("UTIL_PW_CHECK_KEY_ON_HISTORY", param);
    }

    /**
     * Verifies that the same key name exists.
     * @param key The entered key name when setting the password.
     * @return <code>true</code> if the same key name exists.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public boolean hasKey(String key) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        return Tube.getBoolean("UTIL_PW_HAS_KEY", param);
    }

    /**
     * Deletes the password stored with the key name.
     * @param key The entered key name when setting the password.
     * @return <code>true</code> if the setting call succeeds.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public boolean clearKey(String key) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        return ErroNo.check(Tube.doAction("UTIL_PW_KEY", param));
    }

    /**
     * Sets the password corresponding to the currently set user ID.
     * @param key The entered key name when setting the password.
     * @param password The password of the string type.
     * @return <code>true</code> if the setting call succeeds.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public boolean setKey(String key, String password) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        param.putString(To.P1, password);
        return ErroNo.check(Tube.setValue("UTIL_PW_KEY", param));
    }

    /**
     * Returns the password stored in the specified key name as a hash value.
     * @param key The entered key name when setting the password.
     * @return The converted hash value of the byte array type.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public byte[] getKey(String key) {
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        return Tube.getByteArray("UTIL_PW_KEY", param);
    }

    /**
     * Converts the entered password to a hash value.
     * @param password The password of the string type.
     * @return The converted hash value of the byte array type.
     * @see #getCurrentUser()
     * @since 1.0
     */
    public byte[] keyToHash(String password) {
        Bundle param = new Bundle();
        param.putString(To.P0, password);
        return Tube.getByteArray("UTIL_PW_TO_HASH", param);
    }

    /**
     * Returns the Salt value used when generating the password.
     * @since 1.0
     */
    public long getSalt() {
        Bundle result = Tube.getValue("UTIL_PW_SALT", null);
        if (result == null) return 0;
        return result.getLong(To.R0);
    }

    /**
     * Sets the Salt value used when generating the password.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setSalt(long salt) {
        Bundle param = new Bundle();
        param.putLong(To.P0, salt);
        return ErroNo.check(Tube.setValue("UTIL_PW_SALT", param));
    }
}
