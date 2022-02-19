package com.amolla.sdk.util;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;

import android.os.Bundle;

/**
 * A collection of sysfs input and output.
 * Gatters the ability to access and read and write to the file system.
 * Accesses to blocks of production information.
 * @since 1.0
 */
public class SysFs {

    public SysFs() {}

    private static SysFs mThis = null;
    public static SysFs get() {
        if (mThis == null) {
            mThis = new SysFs();
        }
        return mThis;
    }

    /**
     * Reads the value set in the specified system setting.
     * If there's no such key, returns <code>null</code>.
     * @param which The setting area selection. One of {@link To#GLOBAL}, {@link To#SECURE} or {@link To#SYSTEM}.
     * @param key The key name.
     * @return The value corresponding to the key. <code>null</code> check required.
     * @since 1.0
     */
    public String getSetting(int which, String key) {
        if (key == null || key.isEmpty()) return null;
        Bundle param = new Bundle();
        param.putInt(To.P0, which);
        param.putString(To.P1, key);
        return getString("UTIL_FS_SYSTEM_SETTINGS", param);
    }

    /**
     * Sets keys and values in the specified system settings.
     * If there is no existing key value, it's registered as a new one and remains until the factory reset.
     * @param which The setting area selection. One of {@link To#GLOBAL}, {@link To#SECURE} or {@link To#SYSTEM}.
     * @param key The key name.
     * @param value The value to enter in the system settings.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setSetting(int which, String key, String value) {
        if (key == null || key.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putInt(To.P0, which);
        param.putString(To.P1, key);
        param.putString(To.P2, value);
        return ErroNo.check(setValue("UTIL_FS_SYSTEM_SETTINGS", param));
    }

    /**
     * Reads the value set in the specified system property.
     * If there's no such key, returns <code>null</code>.
     * @param key The key name.
     * @param def The default value returned if no key or value exists.
     * @return The value corresponding to the key.
     * @since 1.0
     */
    public String getProperty(String key, String def) {
        if (key == null || key.isEmpty()) return null;
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        param.putString(To.P1, def);
        return getString("UTIL_FS_SYSTEM_PROPERTY", param);
    }

    /**
     * Sets keys and values in the specified system property.
     * If there is no existing key value, it's registered as a new one and remains until the factory reset.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param key The key name.
     * @param value The value to enter in the system property.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean setProperty(String key, String value) {
        if (key == null || key.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putString(To.P0, key);
        param.putString(To.P1, value);
        return ErroNo.check(setValue("UTIL_FS_SYSTEM_PROPERTY", param));
    }

    /**
     * Reads the file at the specified path from the file system.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param path The user-specified path. Can be <code>null</code>.
     * @return The data that reads up to 1MB in byte array format.
     * @since 1.0
     */
    public byte[] readBytes(String path) {
        if (path == null || path.isEmpty()) return null;
        Bundle param = new Bundle();
        param.putString(To.P0, path);
        return getByteArray("UTIL_FS_SYSTEM_BYTES", param);
    }

    /**
     * Writes the file at the specified path from the file system.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param path The user-specified path. Can be <code>null</code>.
     * @param buf The data that writes up to 1MB in byte array format.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean writeBytes(String path, byte[] buf) {
        if (path == null || path.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putString(To.P0, path);
        param.putByteArray(To.P1, buf);
        return ErroNo.check(setValue("UTIL_FS_SYSTEM_BYTES", param));
    }

    /**
     * Reads the file at the specified path from the file system.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param path The user-specified path. Can be <code>null</code>.
     * @return The data that reads up to 1MB in string format.
     * @since 1.0
     */
    public String readString(String path) {
        if (path == null || path.isEmpty()) return null;
        Bundle param = new Bundle();
        param.putString(To.P0, path);
        return getString("UTIL_FS_SYSTEM_STRING", param);
    }

    /**
     * Writes the file at the specified path from the file system.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param path The user-specified path. Can be <code>null</code>.
     * @param buf The data that writes up to 1MB in string format.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean writeString(String path, String buf) {
        if (path == null || path.isEmpty()) return false;
        Bundle param = new Bundle();
        param.putString(To.P0, path);
        param.putString(To.P1, buf);
        return ErroNo.check(setValue("UTIL_FS_SYSTEM_BYTES", param));
    }

    /**
     * Executes the command specified in the shell.
     * <p><strong>WARNING:</strong> Security policy must be met.
     * @param args Variable arguments in string format.
     * @return <code>true</code> if the setting call succeeds.
     * @since 1.0
     */
    public boolean execute(String... args) {
        Bundle param = new Bundle();
        param.putStringArray(To.P0, args);
        return ErroNo.check(doAction("UTIL_FS_EXECUTE", param));
    }
}
