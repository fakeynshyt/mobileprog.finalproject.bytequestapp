package com.example.finalprojectjava.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String userEmail;

    public SessionManager(Context context, String userEmail) {
        prefs = context.getSharedPreferences("session_" + userEmail, Context.MODE_PRIVATE);
        this.userEmail = userEmail;
        editor = prefs.edit();
        setKeySavedUser();
    }

    // Login state
    public void setKeyRememberMe(boolean status) {
        editor.putBoolean("is_remembered", status);
        editor.apply();
    }

    public boolean isRemembered() {
        return prefs.getBoolean("is_remembered", false);
    }

    // Sign-in state (new account created)
    public void setKeyNewUser(boolean status) {
        editor.putBoolean("is_new_user", status);
        editor.apply();
    }

    public boolean isNewUser() {
        return prefs.getBoolean("is_new_user", false);
    }

    // Save user email for quick lookup
    public void setKeySavedUser() {
        editor.putString("saved_user", userEmail);
        editor.apply();
    }

    public String getKeySavedUser() {
        return prefs.getString("saved_user", null);
    }

    // Clear session for logout
    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public void setBonusClaimed(boolean status) {
        editor.putBoolean("bonus_claimed_key", status);
        editor.apply();
    }

    public boolean getBonusClaimed() {
        return prefs.getBoolean("bonus_claimed_key", false);
    }
}
