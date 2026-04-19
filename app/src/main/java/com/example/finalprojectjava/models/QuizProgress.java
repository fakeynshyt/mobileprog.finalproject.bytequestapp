package com.example.finalprojectjava.models;

public class QuizProgress {
    private int quizId;
    private String quizQuestion;
    private int quizSubjectId;
    private String quizChoiceA, quizChoiceB, quizChoiceC, quizChoiceD;
    private String quizCorrectAnswer;
    private int quizLevelReq;
    private boolean quizAnswered;

    public QuizProgress() {}

    public QuizProgress(int quizId, String quizQuestion, int quizSubjectId, String quizChoiceA, String quizChoiceB, String quizChoiceC, String quizChoiceD, String quizCorrectAnswer, int quizLevelReq, boolean quizAnswered) {
        this.quizId = quizId;
        this.quizQuestion = quizQuestion;
        this.quizSubjectId = quizSubjectId;
        this.quizChoiceA = quizChoiceA;
        this.quizChoiceB = quizChoiceB;
        this.quizChoiceC = quizChoiceC;
        this.quizChoiceD = quizChoiceD;
        this.quizCorrectAnswer = quizCorrectAnswer;
        this.quizLevelReq = quizLevelReq;
        this.quizAnswered = quizAnswered;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public int getQuizSubjectId() {
        return quizSubjectId;
    }

    public void setQuizSubjectId(int quizSubjectId) {
        this.quizSubjectId = quizSubjectId;
    }

    public String getQuizChoiceA() {
        return quizChoiceA;
    }

    public void setQuizChoiceA(String quizChoiceA) {
        this.quizChoiceA = quizChoiceA;
    }

    public String getQuizChoiceB() {
        return quizChoiceB;
    }

    public void setQuizChoiceB(String quizChoiceB) {
        this.quizChoiceB = quizChoiceB;
    }

    public String getQuizChoiceC() {
        return quizChoiceC;
    }

    public void setQuizChoiceC(String quizChoiceC) {
        this.quizChoiceC = quizChoiceC;
    }

    public String getQuizChoiceD() {
        return quizChoiceD;
    }

    public void setQuizChoiceD(String quizChoiceD) {
        this.quizChoiceD = quizChoiceD;
    }

    public String getQuizCorrectAnswer() {
        return quizCorrectAnswer;
    }

    public void setQuizCorrectAnswer(String quizCorrectAnswer) {
        this.quizCorrectAnswer = quizCorrectAnswer;
    }

    public int getQuizLevelReq() {
        return quizLevelReq;
    }

    public void setQuizLevelReq(int quizLevelReq) {
        this.quizLevelReq = quizLevelReq;
    }

    public boolean isQuizAnswered() {
        return quizAnswered;
    }

    public void setQuizAnswered(boolean quizAnswered) {
        this.quizAnswered = quizAnswered;
    }
}
