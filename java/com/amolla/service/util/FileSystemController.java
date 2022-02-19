package com.amolla.service.util;

import com.amolla.sdk.Tube;
import com.amolla.sdk.To;
import com.amolla.jni.UtilNative;
import com.amolla.service.DynamicController;

import android.os.Binder;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Arrays;
import android.provider.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

public class FileSystemController extends DynamicController {
    private static final boolean DEBUG = DEBUG_ALL || false;

    public FileSystemController(DynamicController ctrl, String tag) {
        super(ctrl, tag);
    }

    public static FileSystemController init(DynamicController ctrl) {
        if (mInstance == null) {
            mInstance = new FileSystemController(ctrl, FileSystemController.class.getSimpleName());
        }
        return get();
    }

    public static FileSystemController get() {
        return (FileSystemController) mInstance;
    }

    public String getSetting(int which, String key) {
        switch (which) {
            case To.GLOBAL: return Settings.Global.getString(getContentResolver(), key);
            case To.SECURE: return Settings.Secure.getString(getContentResolver(), key);
            case To.SYSTEM: return Settings.System.getString(getContentResolver(), key);
        }
        return null;
    }

    public boolean setSetting(int which, String key, String value) {
        long ident = Binder.clearCallingIdentity();
        try {
            switch (which) {
                case To.GLOBAL:
                    Settings.Global.setString(getContentResolver(), key, value);
                    break;
                case To.SECURE:
                    Settings.Secure.setString(getContentResolver(), key, value);
                    break;
                case To.SYSTEM:
                    Settings.System.setString(getContentResolver(), key, value);
                    break;
                default: return false;
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return false;
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
        return true;
    }

    public String getProperty(String key, String def) {
        return SystemProperties.get(key, def);
    }

    public boolean setProperty(String key, String value) {
        long ident = Binder.clearCallingIdentity();
        try {
            SystemProperties.set(key, value);
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return false;
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
        return true;
    }

    public byte[] readBytes(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return null;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            Log.e(TAG, "File not found : " + path);
            return null;
        }
        byte[] buffer = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            buffer = new byte[(int)file.length()];
            if (fis != null) { fis.read(buffer); }
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return null;
        } finally {
            if (fis != null) try { fis.close(); } catch (IOException e) {}
        }
        return buffer;
    }

    public boolean writeBytes(String path, byte[] buf) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            Log.e(TAG, "File not found : " + path);
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                fos.write(buf);
                fos.flush();
                fos.getFD().sync();
            }
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return false;
        } finally {
            if (fos != null) try { fos.close(); } catch (IOException e) {}
        }
        return true;
    }

    public String readString(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return null;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            Log.e(TAG, "File not found : " + path);
            return null;
        }
        String buffer = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr, 1024);
            buffer = br.readLine();
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return null;
        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {}
            if (fr != null) try { fr.close(); } catch (IOException e) {}
        }
        return buffer;
    }

    public boolean writeString(String path, String buf) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            Log.e(TAG, "File not found : " + path);
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fr, 1024);
            bw.write(buf);
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return false;
        } finally {
            if (bw != null) try { bw.close(); } catch (IOException e) {}
            if (fw != null) try { fw.close(); } catch (IOException e) {}
        }
        return true;
    }

    public boolean execute(String... cmd) {
        long ident = Binder.clearCallingIdentity();
        Process pc = null;
        BufferedReader br = null;
        try {
            if (DEBUG) Log.i(TAG, "Command : " + cmd);
            pc = new ProcessBuilder(cmd).start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = null;
            while ((ouput = br.readLine()) != null) { Log.i(TAG, "Command Result : " + output); }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return false;
        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {}
            if (pc != null) pc.waitFor();
            Binder.restoreCallingIdentity(ident);
        }
        return true;
    }
}
