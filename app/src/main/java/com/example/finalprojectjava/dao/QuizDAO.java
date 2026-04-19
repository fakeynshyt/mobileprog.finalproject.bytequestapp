package com.example.finalprojectjava.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalprojectjava.data.DatabaseHelper;
import com.example.finalprojectjava.models.Quiz;
import com.example.finalprojectjava.models.QuizProgress;

import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    SQLiteDatabase db;

    public QuizDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    // Get Quiz List
    public List<Quiz> getAllQuizLists() {
        List<Quiz> quizList = new ArrayList<>();

        String query = "SELECT * FROM QUIZ_TABLE";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Quiz quiz = new Quiz();
                quiz.setQuiz_question(cursor.getString(1));
                quiz.setQuiz_subject_id(cursor.getInt(2));
                quiz.setQuiz_choice_a(cursor.getString(3));
                quiz.setQuiz_choice_b(cursor.getString(4));
                quiz.setQuiz_choice_c(cursor.getString(5));
                quiz.setQuiz_choice_d(cursor.getString(6));
                quiz.setQuiz_correct_answer(cursor.getString(7));
                quiz.setQuiz_lvl_req(cursor.getInt(8));

                quizList.add(quiz);
            } while(cursor.moveToNext());
        }

        cursor.close();
        return quizList;
    }

    public List<QuizProgress> getQuizProgressListByUser(int userId) {
        List<QuizProgress> list = new ArrayList<>();

        String query = "SELECT q.QUIZ_ID, q.QUIZ_QUESTION, q.SUBJECT_ID, " +
                "q.QUIZ_CHOICE_A, q.QUIZ_CHOICE_B, q.QUIZ_CHOICE_C, q.QUIZ_CHOICE_D, " +
                "q.QUIZ_CORRECT_ANSWER, q.QUIZ_LVL_REQ, " +
                "qp.QUIZ_ANSWERED " +
                "FROM QUIZ_TABLE q " +
                "LEFT JOIN QUIZ_PROGRESS_TABLE qp " +
                "ON q.QUIZ_ID = qp.QUIZ_ID AND qp.USER_ID = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                QuizProgress q = new QuizProgress();

                q.setQuizId(cursor.getInt(0));
                q.setQuizQuestion(cursor.getString(1));
                q.setQuizSubjectId(cursor.getInt(2));
                q.setQuizChoiceA(cursor.getString(3));
                q.setQuizChoiceB(cursor.getString(4));
                q.setQuizChoiceC(cursor.getString(5));
                q.setQuizChoiceD(cursor.getString(6));
                q.setQuizCorrectAnswer(cursor.getString(7));
                q.setQuizLevelReq(cursor.getInt(8));
                q.setQuizAnswered(cursor.isNull(9) ? false : cursor.getInt(9) == 1);

                list.add(q);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public void markQuizAnswered(int userId, int quizId) {

        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("QUIZ_ID", quizId);
        values.put("QUIZ_ANSWERED", 1);

        db.insertWithOnConflict(
                "QUIZ_PROGRESS_TABLE",
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public void close() {
        db.close();
    }
}
