package com.example.finalprojectjava.managers;

import com.example.finalprojectjava.models.QuizProgress;

import java.util.ArrayList;
import java.util.List;

public class QuizManager {

    private static QuizManager instance;
    private List<QuizProgress> quizList;

    private QuizManager() {
        quizList = new ArrayList<>();
    }

    public static synchronized QuizManager getInstance() {
        if(instance == null) {
            instance = new QuizManager();
        }
        return instance;
    }

    public void setQuizList(List<QuizProgress> quizList) {
        this.quizList = new ArrayList<>(quizList);
    }

    public List<QuizProgress> getQuizListBySubId(int subjectId) {
        List<QuizProgress> filteredList = new ArrayList<>();

        for(QuizProgress quiz : quizList) {
            if(quiz.getQuizSubjectId() == subjectId) {
                filteredList.add(quiz);
            }
        }
        return filteredList;
    }

    public void clearQuizList() {
        quizList.clear();
    }
}
