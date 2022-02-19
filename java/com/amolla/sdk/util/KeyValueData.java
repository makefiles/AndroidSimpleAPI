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
public class KeyValueData {

    private static KeyValueData mThis = null;
    public static KeyValueData get() {
        if (mThis == null) {
            mThis = new KeyValueData();
        }
        return mThis;
    }

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
     * Constructs a new KeyValueData instance with the root UID.
     * @since 1.0
     */
    public KeyValueData() {
        this(android.os.Process.ROOT_UID);
    }

    /**
     * Constructs a new KeyValueData instance with the specified UID.
     * @since 1.0
     */
    public KeyValueData(int user) {
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
     * Sets the current user ID for key-value data setting.
     * @param user The identification number.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setCurrentUser(int user) {
        mCurrentUser = user;
    }

    /**
     * Deletes all key-value data set by user ID.
     * @param user The identification number.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean clearUserData(int user) {
        Bundle param = new Bundle();
        param.putInt(To.P0, user);
        return ErroNo.check(Tube.doAction("UTIL_INI_DB_CLEAR_USER", param));
    }

    /**
     * Returns the value corresponding to the specified key.
     * @param key The specified key in the form of a string.
     * @return A string value. <code>null</code> if there's no value.
     * @since 1.0
     */
    public String getValue(String key) {
        if (key == null || key.isEmpty()) return null;
        Bundle param = new Bundle();
        param.putInt(To.P0, getCurrentOrCallingUser());
        param.putString(To.P1, key);
        return getString("UTIL_INI_DB_SINGLE_USER_DATA", param);
    }

    /**
     * Saves the key and value to the database.
     * @param key The specified key in the form of a string.
     * @param value A string value. Empty string is allowed. If <code>null</code> is entered, the key is deleted.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setValue(String key, String value) {
        if (key == null || key.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putInt(To.P0, getCurrentOrCallingUser());
        param.putString(To.P1, key);
        param.putString(To.P2, value);
        return ErroNo.check(setString("UTIL_INI_DB_SINGLE_USER_DATA", param));
    }

    /**
     * Returns the result in Map type for all keys and values.
     * @return A result of the Map type. <code>null</code> value is an exception.
     * @since 1.0
     */
    public Map<String, String> getKeyValues() {
        Bundle param = new Bundle();
        param.putInt(To.P0, getCurrentOrCallingUser());
        Bundle result = getValue("UTIL_INI_DB_MULTI_USER_DATA", param);
        if (result == null) return null;
        return (Map<String, String>) result.getSerializable(To.R0);
    }

    /**
     * Saves all keys and values entered in Map type to the database.
     * @param values The value to store in Map type.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setKeyValues(Map<String, String> values) {
        if (values == null || values.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putInt(To.P0, getCurrentOrCallingUser());
        param.putSerializable(To.P1, values);
        return ErroNo.check(Tube.setValue("UTIL_INI_DB_MULTI_USER_DATA", param));
    }
}
