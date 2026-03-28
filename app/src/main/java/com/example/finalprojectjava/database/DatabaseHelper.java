package com.example.finalprojectjava.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.Subject;
import com.example.finalprojectjava.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bytequest.db";
    private static final int DATABASE_VERSION = 1;


    // ******USER TABLE******
    private static final String USER_TABLE = "USER_TABLE";
    private static final String USER_ID = "USER_ID";
    private static final String COLUMN_USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String COLUMN_USER_LAST_NAME = "USER_LAST_NAME";
    private static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    private static final String COLUMN_USER_PASS = "USER_PASS";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_GENDER = "GENDER";
    private static final String COLUMN_BIRTH_DATE = "BIRTH_DATE";
    private static final String COLUMN_ADDRESS = "ADDRESS";
    private static final String COLUMN_LEVEL = "LEVEL";
    private static final String COLUMN_EXP = "EXP";


    // ******SUBJECT TABLE*******
    private static final String SUBJECT_TABLE = "SUBJECT_TABLE";
    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String SUBJECT_DESCRIPTION = "DESCRIPTION";


    // ******MODULE TABLE*******
    private static final String MODULE_TABLE = "MODULE_TABLE";
    private static final String MODULE_ID = "MODULE_ID";
    private static final String MODULE_NAME = "MODULE_NAME";
    private static final String MODULE_USER_ID = "USER_ID";
    private static final String MODULE_SUBJECT_ID = "SUBJECT_ID";
    private static final String MODULE_LEVEL = "MODULE_LEVEL";


    // ******PROGRESS TABLE*******
    private static final String PROGRESS_TABLE = "PROGRESS_TABLE";
    private static final String PROGRESS_MODULE_ID = "MODEL_ID";
    private static final String PROGRESS_USER_ID = "USER_ID";
    private static final String PROGRESS_STATUS = "STATUS";

    // ******GAME TABLE*******
    private static final String GAME_TABLE = "GAME_TABLE";
    private static final String GAME_USER_ID = "USER_ID";
    private static final String GAME_NAME = "GAME_NAME";
    private static final String GAME_DESCRIPTION = "GAME_DESCRIPTION";
    private static final String GAME_SCORE = "GAME_SCORE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ******CREATE USER TABLE*******
        String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_FIRST_NAME + " TEXT, "
                + COLUMN_USER_LAST_NAME + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT UNIQUE, "
                + COLUMN_USER_PASS + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_BIRTH_DATE + " TEXT, "
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_LEVEL + " INTEGER, "
                + COLUMN_EXP + " INTEGER)";

        String CREATE_TABLE_SUBJECT = "CREATE TABLE " + SUBJECT_TABLE + " ("
                + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT,"
                + SUBJECT_DESCRIPTION + " TEXT)";

        String CREATE_TABLE_MODULE = "CREATE TABLE " + MODULE_TABLE + " ("
                + MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MODULE_NAME + " TEXT, "
                + MODULE_USER_ID + " INTEGER UNIQUE, "
                + MODULE_SUBJECT_ID + " INTEGER UNIQUE, "
                + MODULE_LEVEL + " INTEGER, "
                + "FOREIGN KEY(" + MODULE_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
                + "FOREIGN KEY(" + MODULE_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + "(" + SUBJECT_ID + "))";

        String CREATE_TABLE_PROGRESS = "CREATE TABLE " + PROGRESS_TABLE + " ("
                + PROGRESS_MODULE_ID + " INTEGER UNIQUE, "
                + PROGRESS_USER_ID + " INTEGER UNIQUE, "
                + PROGRESS_STATUS + " INTEGER, "
                + "FOREIGN KEY(" + PROGRESS_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
                + "FOREIGN KEY(" + PROGRESS_MODULE_ID + ") REFERENCES " + MODULE_TABLE + "(" + MODULE_ID + "))";


        String CREATE_TABLE_GAME_TABLE = "CREATE TABLE " + GAME_TABLE + " ("
                + GAME_USER_ID + " INTEGER UNIQUE, "
                + GAME_NAME + " TEXT, "
                + GAME_DESCRIPTION + " TEXT, "
                + GAME_SCORE + " INTEGER, "
                + "FOREIGN KEY(" + GAME_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "))";

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SUBJECT);
        db.execSQL(CREATE_TABLE_MODULE);
        db.execSQL(CREATE_TABLE_PROGRESS);
        db.execSQL(CREATE_TABLE_GAME_TABLE);

        insertDefaultSubject(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MODULE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);

        onCreate(db);
    }

    // ***** User Section
    public boolean createUserAccount(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_FIRST_NAME, user.getFirst_name());
        values.put(COLUMN_USER_LAST_NAME, user.getLast_name());
        values.put(COLUMN_USER_EMAIL, user.getUser_email());
        values.put(COLUMN_USER_PASS, user.getUser_pass());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_BIRTH_DATE, user.getBirth_date());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_LEVEL, user.getLevel());
        values.put(COLUMN_EXP, user.getExp());

        long result = db.insert(USER_TABLE, null, values);

        return result != -1;
    }

    public User loginUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ? ";
        Cursor cursor = db.rawQuery(query, new String[] { email});

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setUser_email(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setUser_pass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASS)));
            user.setFirst_name(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST_NAME)));
            user.setLast_name(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LAST_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
            user.setBirth_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            user.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
            user.setExp(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXP)));

            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{ email });

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setUser_email(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setUser_pass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASS)));
            user.setFirst_name(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
            user.setBirth_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            user.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
            user.setExp(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXP)));

            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public void updateUserProfile(int user_id, String username, String gender, String birthDate, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_BIRTH_DATE, birthDate);
        values.put(COLUMN_ADDRESS, address);

        db.update(USER_TABLE, values, USER_ID + " = ?", new String[] { String.valueOf(user_id)});
        db.close();
    }

    public boolean hasUserAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " LIMIT 1", null);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    // ***** Subject Section
    public void insertDefaultSubject(SQLiteDatabase db) {
        ContentValues defaultValues = new ContentValues();

        String subject1 = "Foundation of Computing";
        String subject2 = "Java Programming";
        String subject3 = "C# Programming";
        String subject4 = "Mobile Application Development";
        String subject5 = "Web Development";
        String subject6 = "Database Management";
        String subject7 = "Discrete Mathematics";

        // Insert subject 1
        defaultValues.put(SUBJECT_NAME, subject1);
        defaultValues.put(SUBJECT_DESCRIPTION, "Foundation of Computing is a subject that introduces the core principles of computer science, focusing on algorithms, programming, data, hardware, software, and the societal impact of computing.");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 2;
        defaultValues.put(SUBJECT_NAME, subject2);
        defaultValues.put(SUBJECT_DESCRIPTION, "Java programming is a high-level, object-oriented programming language widely used for building applications across platforms.");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 3;
        defaultValues.put(SUBJECT_NAME, subject3);
        defaultValues.put(SUBJECT_DESCRIPTION, "C# (pronounced \"C-sharp\") is a modern, object-oriented programming language developed by Microsoft. It is primarily used for building applications on the .NET framework, ranging from desktop software to web services and mobile apps.");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 4;
        defaultValues.put(SUBJECT_NAME, subject4);
        defaultValues.put(SUBJECT_DESCRIPTION, "Mobile Application Development is the process of creating software applications that run on mobile devices such as smartphones and tablets. It combines programming, design, and user experience principles to deliver functional and engaging apps for platforms like Android and iOS.");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 5;
        defaultValues.put(SUBJECT_NAME, subject5);
        defaultValues.put(SUBJECT_DESCRIPTION, "Web development is the process of designing, building, and maintaining websites and web applications. It involves both the front-end (what users see and interact with) and the back-end (server-side logic, databases, and application functionality).");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 6;
        defaultValues.put(SUBJECT_NAME, subject6);
        defaultValues.put(SUBJECT_DESCRIPTION, "Database Management is the discipline of organizing, storing, and retrieving data efficiently using specialized systems called Database Management Systems (DBMS). It focuses on structuring data, ensuring accuracy, and enabling secure access for applications and users.");
        db.insert(SUBJECT_TABLE, null, defaultValues);

        // Insert subject 7;
        defaultValues.put(SUBJECT_NAME, subject7);
        defaultValues.put(SUBJECT_DESCRIPTION, "Discrete Mathematics is the study of mathematical structures that are fundamentally discrete rather than continuous, such as integers, graphs, and logical statements. It is a core subject in computer science because it provides the theoretical foundation for algorithms, data structures, cryptography, and programming logic.");
        db.insert(SUBJECT_TABLE, null, defaultValues);
    }

    public List<Subject> getAllSubjects() {
        List<Subject> returnSubject = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String querySelector = "SELECT * FROM " + SUBJECT_TABLE;
        Cursor cursor = db.rawQuery(querySelector, null);

        if(cursor.moveToFirst()) {
            do {
                int subjectID = cursor.getInt(0);
                String subjectName = cursor.getString(1);
                String subjectDescription = cursor.getString(2);

                Subject subject = new Subject(subjectID, subjectName, subjectDescription);
                returnSubject.add(subject);

            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return returnSubject;
    }

}
