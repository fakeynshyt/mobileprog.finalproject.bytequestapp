package com.example.finalprojectjava.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectjava.data.DatabaseHelper;
import com.example.finalprojectjava.models.User;

public class UserDAO {
    private SQLiteDatabase db;
    public UserDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public boolean createUserAccount(User user) {
        ContentValues values = new ContentValues();

        values.put("USER_FIRST_NAME", user.getFirst_name());
        values.put("USER_LAST_NAME", user.getLast_name());
        values.put("USER_EMAIL", user.getUser_email());
        values.put("USER_PASSWORD", user.getUser_pass());
        values.put("USER_USERNAME", user.getUsername());
        values.put("USER_GENDER", user.getGender());
        values.put("USER_BIRTH_DATE", user.getBirth_date());
        values.put("USER_ADDRESS", user.getAddress());
        values.put("USER_LEVEL", user.getLevel());
        values.put("USER_BYTE_POINTS", user.getByte_point());

        long result = db.insert("USER_TABLE", null, values);

        return result != -1;
    }

    public User loginUser(String email) {
        return getUserByEmail(email);
    }

    public User getUserByEmail(String email) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM USER_TABLE WHERE USER_EMAIL = ?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUser_id(cursor.getInt(0));
            user.setFirst_name(cursor.getString(1));
            user.setLast_name(cursor.getString(2));
            user.setUser_email(cursor.getString(3));
            user.setUser_pass(cursor.getString(4));
            user.setUsername(cursor.getString(5));
            user.setGender(cursor.getString(6));
            user.setBirth_date(cursor.getString(7));
            user.setAddress(cursor.getString(8));
            user.setLevel(cursor.getInt(9));
            user.setByte_point(cursor.getInt(10));

            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public void resetUserPassword(String email, String newPassword) {
        ContentValues values = new ContentValues();

        values.put("USER_PASSWORD", newPassword);

        db.update("USER_TABLE", values, "USER_EMAIL = ?", new String[]{email});
    }

    public boolean hasUserAccount() {
        Cursor cursor = db.rawQuery("SELECT * FROM USER_TABLE LIMIT 1", null);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        return exists;
    }

    public boolean updateUserProfile(User user) {
        ContentValues values = new ContentValues();

        values.put("USER_FIRST_NAME", user.getFirst_name());
        values.put("USER_LAST_NAME", user.getLast_name());
        values.put("USER_EMAIL", user.getUser_email());
        values.put("USER_PASSWORD", user.getUser_pass());
        values.put("USER_USERNAME", user.getUsername());
        values.put("USER_GENDER", user.getGender());
        values.put("USER_BIRTH_DATE", user.getBirth_date());
        values.put("USER_ADDRESS", user.getAddress());
        values.put("USER_LEVEL", user.getLevel());
        values.put("USER_BYTE_POINTS", user.getByte_point());

        int rowsAffected = db.update(
                "USER_TABLE",
                values,
                "USER_ID = ?",
                new String[]{ String.valueOf(user.getUser_id()) }
        );

        return rowsAffected > 0;
    }

    public void addUserPoints(int userId, int points) {

        db.execSQL("UPDATE USER_TABLE SET USER_BYTE_POINTS = USER_BYTE_POINTS + ? WHERE USER_ID = ?",
                new Object[]{points, userId});
    }

    public void close() {
        db.close();
    }
}
