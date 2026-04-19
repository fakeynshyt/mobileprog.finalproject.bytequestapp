package com.example.finalprojectjava.models;

public class Quiz {
    private String quiz_question;
    private int quiz_subject_id;
    private String quiz_choice_a;
    private String quiz_choice_b;
    private String quiz_choice_c;
    private String quiz_choice_d;
    private String quiz_correct_answer;
    private int quiz_lvl_req;

    public Quiz() {}

    public Quiz(String quiz_question, int quiz_user_id, int quiz_subject_id, String quiz_choice_a, String quiz_choice_b, String quiz_choice_c, String quiz_choice_d, String quiz_correct_answer, boolean quiz_answered, int quiz_lvl_req) {
        this.quiz_question = quiz_question;
        this.quiz_subject_id = quiz_subject_id;
        this.quiz_choice_a = quiz_choice_a;
        this.quiz_choice_b = quiz_choice_b;
        this.quiz_choice_c = quiz_choice_c;
        this.quiz_choice_d = quiz_choice_d;
        this.quiz_correct_answer = quiz_correct_answer;
        this.quiz_lvl_req = quiz_lvl_req;
    }

    public String getQuiz_question() {
        return quiz_question;
    }

    public void setQuiz_question(String quiz_question) {
        this.quiz_question = quiz_question;
    }

    public int getQuiz_subject_id() {
        return quiz_subject_id;
    }

    public void setQuiz_subject_id(int quiz_subject_id) {
        this.quiz_subject_id = quiz_subject_id;
    }

    public String getQuiz_choice_a() {
        return quiz_choice_a;
    }

    public void setQuiz_choice_a(String quiz_choice_a) {
        this.quiz_choice_a = quiz_choice_a;
    }

    public String getQuiz_choice_b() {
        return quiz_choice_b;
    }

    public void setQuiz_choice_b(String quiz_choice_b) {
        this.quiz_choice_b = quiz_choice_b;
    }

    public String getQuiz_choice_c() {
        return quiz_choice_c;
    }

    public void setQuiz_choice_c(String quiz_choice_c) {
        this.quiz_choice_c = quiz_choice_c;
    }

    public String getQuiz_choice_d() {
        return quiz_choice_d;
    }

    public void setQuiz_choice_d(String quiz_choice_d) {
        this.quiz_choice_d = quiz_choice_d;
    }

    public String getQuiz_correct_answer() {
        return quiz_correct_answer;
    }

    public void setQuiz_correct_answer(String quiz_correct_answer) {
        this.quiz_correct_answer = quiz_correct_answer;
    }

    public int getQuiz_lvl_req() {
        return quiz_lvl_req;
    }

    public void setQuiz_lvl_req(int quiz_lvl_req) {
        this.quiz_lvl_req = quiz_lvl_req;
    }
}
