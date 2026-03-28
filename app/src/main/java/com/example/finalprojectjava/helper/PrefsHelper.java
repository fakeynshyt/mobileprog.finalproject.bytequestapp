package com.example.finalprojectjava.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {

    private static final String PREF_G_NAME = "GLOBAL_SESSION";
    private static final String PREF_P_NAME = "PRIVATE_SESSION";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_G_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public PrefsHelper(Context context, String email) {
        sharedPreferences = context.getSharedPreferences(email + "_" + PREF_P_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save a string
    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Get a string
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Save an int
    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Get an int
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    // Get an int
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    // Clear all prefs
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}