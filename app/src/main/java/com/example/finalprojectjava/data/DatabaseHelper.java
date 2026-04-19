package com.example.finalprojectjava.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finalprojectjava.models.Quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bytequest.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    // ******USER TABLE******
    private static final String USER_TABLE = "USER_TABLE";
    private static final String USER_ID = "USER_ID";
    private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_USERNAME = "USER_USERNAME";
    private static final String USER_GENDER = "USER_GENDER";
    private static final String USER_BIRTH_DATE = "USER_BIRTH_DATE";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_LEVEL = "USER_LEVEL";
    private static final String USER_BYTE_POINTS = "USER_BYTE_POINTS";


    // ******SUBJECT TABLE*******
    private static final String SUBJECT_TABLE = "SUBJECT_TABLE";
    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String SUBJECT_DESCRIPTION = "SUBJECT_DESCRIPTION";


    // ******MODULE TABLE*******
    private static final String MODULE_TABLE = "MODULE_TABLE";
    private static final String MODULE_ID = "MODULE_ID";
    private static final String MODULE_NAME = "MODULE_NAME";
    private static final String MODULE_USER_ID = "USER_ID";
    private static final String MODULE_SUBJECT_ID = "SUBJECT_ID";
    private static final String MODULE_LEVEL = "MODULE_LEVEL";

    // ******QUIZ TABLE*******
    private static final String QUIZ_TABLE = "QUIZ_TABLE";
    private static final String QUIZ_ID = "QUIZ_ID";
    private static final String QUIZ_QUESTION = "QUIZ_QUESTION";
    private static final String QUIZ_SUBJECT_ID = "SUBJECT_ID";
    private static final String QUIZ_CHOICE_A = "QUIZ_CHOICE_A";
    private static final String QUIZ_CHOICE_B = "QUIZ_CHOICE_B";
    private static final String QUIZ_CHOICE_C = "QUIZ_CHOICE_C";
    private static final String QUIZ_CHOICE_D = "QUIZ_CHOICE_D";
    private static final String QUIZ_CORRECT_ANSWER = "QUIZ_CORRECT_ANSWER";
    private static final String QUIZ_LVL_REQ = "QUIZ_LVL_REQ";

    // ******QUIZ_PROGRESS TABLE*******
    private static final String QUIZ_PROGRESS_TABLE = "QUIZ_PROGRESS_TABLE";
    private static final String QUIZ_PROGRESS_QUIZ_ID = "QUIZ_ID";
    private static final String QUIZ_PROGRESS_USER_ID = "USER_ID";
    private static final String QUIZ_PROGRESS_IS_ANSWERED = "QUIZ_ANSWERED";


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
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ******CREATE USER TABLE*******
        String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_FIRST_NAME + " TEXT, "
                + USER_LAST_NAME + " TEXT, "
                + USER_EMAIL + " TEXT UNIQUE, "
                + USER_PASSWORD + " TEXT, "
                + USER_USERNAME + " TEXT, "
                + USER_GENDER + " TEXT, "
                + USER_BIRTH_DATE + " TEXT, "
                + USER_ADDRESS + " TEXT,"
                + USER_LEVEL + " INTEGER, "
                + USER_BYTE_POINTS + " INTEGER)";

        // ******CREATE SUBJECT TABLE*******
        String CREATE_TABLE_SUBJECT = "CREATE TABLE " + SUBJECT_TABLE + " ("
                + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT,"
                + SUBJECT_DESCRIPTION + " TEXT)";

        // ******CREATE MODULE TABLE*******
        String CREATE_TABLE_MODULE = "CREATE TABLE " + MODULE_TABLE + " ("
                + MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MODULE_NAME + " TEXT, "
                + MODULE_USER_ID + " INTEGER, "
                + MODULE_SUBJECT_ID + " INTEGER, "
                + MODULE_LEVEL + " INTEGER, "
                + "FOREIGN KEY(" + MODULE_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
                + "FOREIGN KEY(" + MODULE_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + "(" + SUBJECT_ID + "))";

        // ******CREATE QUIZ TABLE*******
        String CREATE_TABLE_QUIZ = "CREATE TABLE " + QUIZ_TABLE + " ("
                + QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUIZ_QUESTION + " TEXT, "
                + QUIZ_SUBJECT_ID + " INTEGER, "
                + QUIZ_CHOICE_A + " TEXT, "
                + QUIZ_CHOICE_B + " TEXT, "
                + QUIZ_CHOICE_C + " TEXT, "
                + QUIZ_CHOICE_D + " TEXT, "
                + QUIZ_CORRECT_ANSWER + " TEXT, "
                + QUIZ_LVL_REQ + " INTEGER, "
                + "FOREIGN KEY(" + QUIZ_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + "(" + SUBJECT_ID + "))";

        String CREATE_TABLE_QUIZ_PROGRESS = "CREATE TABLE " + QUIZ_PROGRESS_TABLE + " ("
                + QUIZ_PROGRESS_QUIZ_ID + " INTEGER, "
                + QUIZ_PROGRESS_USER_ID + " INTEGER, "
                + QUIZ_PROGRESS_IS_ANSWERED + " INTEGER, "
                + "PRIMARY KEY(" + QUIZ_PROGRESS_QUIZ_ID + ", " + QUIZ_PROGRESS_USER_ID + "), "
                + "FOREIGN KEY(" + QUIZ_PROGRESS_QUIZ_ID + ") REFERENCES " + QUIZ_TABLE + "(" + QUIZ_ID + "),"
                + "FOREIGN KEY(" + QUIZ_PROGRESS_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "))";


        // ******CREATE PROGRESS TABLE*******
        String CREATE_TABLE_PROGRESS = "CREATE TABLE " + PROGRESS_TABLE + " ("
                + PROGRESS_MODULE_ID + " INTEGER, "
                + PROGRESS_USER_ID + " INTEGER, "
                + PROGRESS_STATUS + " INTEGER, "
                + "FOREIGN KEY(" + PROGRESS_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
                + "FOREIGN KEY(" + PROGRESS_MODULE_ID + ") REFERENCES " + MODULE_TABLE + "(" + MODULE_ID + "))";

        // ******CREATE GAME TABLE*******
        String CREATE_TABLE_GAME = "CREATE TABLE " + GAME_TABLE + " ("
                + GAME_USER_ID + " INTEGER, "
                + GAME_NAME + " TEXT, "
                + GAME_DESCRIPTION + " TEXT, "
                + GAME_SCORE + " INTEGER, "
                + "FOREIGN KEY(" + GAME_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "))";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SUBJECT);
        db.execSQL(CREATE_TABLE_MODULE);
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_QUIZ_PROGRESS);
        db.execSQL(CREATE_TABLE_PROGRESS);
        db.execSQL(CREATE_TABLE_GAME);

        insertDefaultSubject(db);
        initializeQuizList(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MODULE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUIZ_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUIZ_PROGRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);

        onCreate(db);
    }


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

    // Java default quiz item
    public void insertDefaultQuiz(SQLiteDatabase db, List<Quiz> quizList, int subjectId) {
        for(Quiz quiz : quizList) {
            ContentValues values = new ContentValues();
            values.put(QUIZ_QUESTION, quiz.getQuiz_question());
            values.put(QUIZ_SUBJECT_ID, subjectId);
            values.put(QUIZ_CHOICE_A, quiz.getQuiz_choice_a());
            values.put(QUIZ_CHOICE_B, quiz.getQuiz_choice_b());
            values.put(QUIZ_CHOICE_C, quiz.getQuiz_choice_c());
            values.put(QUIZ_CHOICE_D, quiz.getQuiz_choice_d());
            values.put(QUIZ_CORRECT_ANSWER, quiz.getQuiz_correct_answer());
            values.put(QUIZ_LVL_REQ, quiz.getQuiz_lvl_req());

            db.insert(QUIZ_TABLE, null, values);
        }
    }

    private List<Quiz> extractQuizList(Context context, String fileName) {
        List<Quiz> quizList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(fileName)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 7) {
                    Quiz quiz = new Quiz();

                    quiz.setQuiz_question(parts[0].trim());
                    quiz.setQuiz_choice_a(parts[1].trim());
                    quiz.setQuiz_choice_b(parts[2].trim());
                    quiz.setQuiz_choice_c(parts[3].trim());
                    quiz.setQuiz_choice_d(parts[4].trim());
                    quiz.setQuiz_correct_answer(parts[5].trim());
                    quiz.setQuiz_lvl_req(Integer.parseInt(parts[6].trim()));

                    quizList.add(quiz);
                }
            }
        } catch (IOException e) {
            Log.e("TAG", "Couldn't load quiz: " + e.getMessage());
        }

        return quizList;
    }

    private void initializeQuizList(SQLiteDatabase db) {
        //Java Quizzes
        List<Quiz> javaQuizList = extractQuizList(context, "java_quiz_items.txt");
        insertDefaultQuiz(db, javaQuizList, 2);

        //C# Quizzes
        List<Quiz> csharpQuizList = extractQuizList(context, "csharp_quiz_items.txt");
        insertDefaultQuiz(db, csharpQuizList, 3);

        //C# Webdev Quizzes
        List<Quiz> webdevQuizList = extractQuizList(context, "webdev_quiz_items.txt");
        insertDefaultQuiz(db, webdevQuizList, 5);

        //C# DbManagement Quizzes
        List<Quiz> dbmanagementQuizList = extractQuizList(context, "dbmanagement_quiz_items.txt");
        insertDefaultQuiz(db, dbmanagementQuizList, 6);
    }
}
