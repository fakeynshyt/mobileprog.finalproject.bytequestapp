package com.example.finalprojectjava.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context, String userEmail) {
        prefs = context.getSharedPreferences("session_" + userEmail, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Login state
    public void setKeyLoggedIn(boolean status) {
        editor.putBoolean("isLoggedIn", status);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("isLoggedIn", false);
    }

    // Sign-in state (new account created)
    public void setKeySignedIn(boolean status) {
        editor.putBoolean("isSignedIn", status);
        editor.apply();
    }

    public boolean isSignedIn() {
        return prefs.getBoolean("isSignedIn", false);
    }

    // Save user email for quick lookup
    public void setKeySavedUser(String email) {
        editor.putString("savedUser", email);
        editor.apply();
    }

    public String getKeySavedUser() {
        return prefs.getString("savedUser", null);
    }

    // Clear session for logout
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}