package com.amolla.sdk.util;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;

import android.os.Bundle;

import java.io.File;
import java.util.Map;

/**
 * A tool that makes it easy to create and use INI files. (Initialization format file)
 * Specififes the file path when creating the class. If the file does not exist, create it.
 * It reads the file specified by the constructor and holds the data.
 * @since 1.0
 */
public class KeyValueFile {

    private String mAbsolutePath;
    private Map<String, String> mKeyValues = null;
    private boolean update(boolean force) {
        if (force || mKeyValues == null) {
            if (mKeyValues != null && !mKeyValues.isEmpty()) {
                mKeyValues.clear();
            }
            Bundle param = new Bundle();
            param.putString(To.P0, mAbsolutePath);
            Bundle result = getValue("UTIL_INI_FILE_KEY_VALUES", param);
            if (result != null && !result.isEmpty()) {
                mKeyValues = (Map<String, String>) result.getSerializable(To.R0);
                return true;
            }
        }
        return false;
    }

    /**
     * Constructs a KeyValueFile with the given absolute path.
     * @param absolutePath The absolute path of the file to control. The path is fixed as a constant.
     * @since 1.0
     */
    public KeyValueFile(String absolutePath) {
        mAbsolutePath = absolutePath;
        update(true);
    }

    /**
     * Constructs a KeyValueFile with the given {@link java.io.File} class.
     * @param file File class that contains the absolute path of the file to control. The path is fixed as a constant.
     * @since 1.0
     */
    public KeyValueFile(File file) {
        this(file.getAbsolutePath());
    }

    /**
     * Returns the value corresponding to the specified key.
     * @param key The specified key in the form of a string.
     * @return A string value. <code>null</code> if there's no value.
     * @since 1.0
     */
    public String getValue(String key) {
        update(false);
        return mKeyValues == null ? "" : mKeyValues.get(key);
    }

    /**
     * Saves the key and value to a file.
     * @param key The specified key in the form of a string.
     * @param value A string value. Empty string is allowed. If <code>null</code> is entered, the key is deleted.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setValue(String key, String value) {
        update(false);
        if (key == null || key.isEmpty()) return false;
        if (value == null) {
            mKeyValues.remove(key);
        } else {
            mKeyValues.put(key, value);
        }
        return setKeyValues(mKeyValues);
    }

    /**
     * Returns the result in Map type for all keys and values.
     * @return A result of the Map type. <code>null</code> value is an exception.
     * @since 1.0
     */
    public Map<String, String> getKeyValues() {
        update(false);
        return mKeyValues;
    }

    /**
     * Saves all keys and values entered in Map type to a file.
     * @param values The value to store in Map type.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setKeyValues(Map<String, String> values) {
        Bundle param = new Bundle();
        param.putString(To.P0, mAbsolutePath);
        param.putSerializable(To.P1, values);
        boolean result = ErroNo.check(Tube.setValue("UTIL_INI_FILE_KEY_VALUES", param));
        update(true);
        return result;
    }
}
