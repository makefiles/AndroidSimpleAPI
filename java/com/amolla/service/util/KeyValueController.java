package com.amolla.service.util;

import com.amolla.sdk.Tube;
import com.amolla.sdk.To;
import com.amolla.service.DynamicController;

import android.util.Log;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.Map;
import java.util.LinkedHashMap;

public class KeyValueController extends DynamicController {
    private static final boolean DEBUG = DEBUG_ALL || false;

    private final DatabaseHelper mHelper;
    private static final String TABLE = "keyvalue";
    private static final String COLUMN_USER = "_user";
    private static final String COLUMN_KEY = "_key";
    private static final String COLUMN_VALUE = "_value";
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, "key_value.db", null, 1);
            setWriteAheadLoggingEnabled(true);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER + " INTEGER," +
                    COLUMN_KEY + " TEXT," +
                    COLUMN_VALUE + " TEXT" +
                    ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
        }
    }

    public KeyValueController(DynamicController ctrl, String tag) {
        super(ctrl, tag);
    }

    public static KeyValueController init(DynamicController ctrl) {
        if (mInstance == null) {
            mInstance = new KeyValueController(ctrl, KeyValueController.class.getSimpleName());
            get().init();
        }
        return get();
    }

    public static KeyValueController get() {
        return (KeyValueController) mInstance;
    }

    public boolean init() {
        try {
            mHelper = new DatabaseHelper(getContext());
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int user = intent.getIntExtra(Intent.EXTRA_USER_HANDLE, -1);
                    if (user < 1) return;
                    clearUserData(user);
                }
            }, new IntentFilter(Intent.ACTION_USER_REMOVED));
            return true;
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
        }
        return false;
    }

    public boolean clearUserData(int user) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE, COLUMN_USER + "='" + user + "'", null);
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public boolean setUserData(int user, String key, String value) {
        if (key == null || key.isEmpty) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE, COLUMN_KEY + "=? AND " + COLUMN_USER + "=?", new String[] { key, Integer.toString(user) });
            if (value != null) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_USER, user);
                cv.put(COLUMN_KEY, key);
                cv.put(COLUMN_VALUE, value);
                db.insert(TABLE, null, cv);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public String getUserData(int user, String key) {
        String result = null;
        if (key == null || key.isEmpty) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        Cursor cursor = mHelper.getReadableDatabase().query(TABLE, new String[] { COLUMN_VALUE },
                        COLUMN_KEY + "=? AND " + COLUMN_USER + "=?", new String[] { key, Integer.toString(user) },
                        null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public LinkedHashMap<String, String> getUserData(int user) {
        Map<String, String> keyValues = null;
        Cursor cursor = mHelper.getReadableDatabase().query(TABLE, new String[] { COLUMN_KEY, COLUMN_VALUE },
                        COLUMN_USER + "=?", new String[] { Integer.toString(user) },
                        null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                keyValues = new LinkedHashMap<String, String>();

                while (!cursor.isAfterLast()) {
                    keyValues.put(cursor.getString(0), cursor.getString(0));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return keyValues;
    }

    public boolean setUserData(int user, Map<String, String> keyValues) {
        if (keyValues == null || keyValues.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                if (entry.getKey() == null) continue;
                db.delete(TABLE, COLUMN_KEY + "=? AND " + COLUMN_USER + "=?", new String[] { entry.getKey(), Integer.toString(user) });
                if (entry.getValue() != null) {
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_USER, user);
                    cv.put(COLUMN_KEY, entry.getKey());
                    cv.put(COLUMN_VALUE, entry.getValue());
                    db.insert(TABLE, null, cv);
                }
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public LinkedHashMap<String, String> getKeyValues(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG, "File not found : " + path);
            return null;
        }
        Map<String, String> keyValues = new LinkedHashMap<String, String>();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                if (buffer.startsWith("#") ||
                    buffer.isEmpty() ||
                    buffer.length() < 1) { continue; }
                if (buffer.contains("END")) { break; }

                /* Get key string */
                int token = 0;
                if (buffer.contains("=")) {
                    token = buffer.indexOf('=');
                } else if (buffer.contains(":")) {
                    token = buffer.indexOf(':');
                } else { continue; }
                String key = buffer.substring(0, token);
                if (key != null || !key.isEmpty()) {
                    key = key.trim();
                } else { continue; }

                /* Get value string */
                String value = buffer.substring(token + 1);
                if (value != null && !value.isEmpty()) {
                    value = value.trim();
                } else {
                    value = "";
                }
                keyValues.put(key, value);
            }
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return null;
        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {}
            if (fr != null) try { fr.close(); } catch (IOException e) {}
        }
        return keyValues;
    }

    public boolean setKeyValues(String path, Map<String, String> keyValues) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return null;
        }
        if (keyValues == null || keyValues.isEmpty()) {
            Log.e(TAG, "Illegal arguemnt");
            return false;
        }
        File file = new File(path);
        File temp = new File(path + "~");
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(temp);
            bw = new BufferedWriter(fw);
            for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue());
                bw.newLine();
            }
            bw.write("END");
            bw.flash();
        } catch (Exception e) {
            if (DEBUG) { e.printStackTrace(); }
            return false;
        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {}
            if (fr != null) try { fr.close(); } catch (IOException e) {}
            if (bw != null) try { bw.close(); } catch (IOException e) {}
            if (fw != null) try { fw.close(); } catch (IOException e) {}
            if (temp != null && temp.exists()) {
                temp.renameTo(file);
            }
        }
        return true;
    }
}
