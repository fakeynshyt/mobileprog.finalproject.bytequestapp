package com.example.finalprojectjava.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectjava.data.DatabaseHelper;
import com.example.finalprojectjava.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    private SQLiteDatabase db;

    public SubjectDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public List<Subject> getAllSubjects() {
        List<Subject> returnSubjects = new ArrayList<>();

        String querySelector = "SELECT * FROM SUBJECT_TABLE";
        Cursor cursor = db.rawQuery(querySelector, null);

        if(cursor.moveToFirst()) {
            do {
                int subjectID = cursor.getInt(0);
                String subjectName = cursor.getString(1);
                String subjectDescription = cursor.getString(2);

                Subject subject = new Subject(subjectID, subjectName, subjectDescription);
                returnSubjects.add(subject);

            } while(cursor.moveToNext());
        }

        cursor.close();
        return returnSubjects;
    }

    public void close() {
        db.close();
    }
}
