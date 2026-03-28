package com.example.finalprojectjava.models;

public class Subject {
    private int subject_id;
    private String subject_name;
    private String subject_description;

    public Subject(int subject_id, String subject_name, String subject_description) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.subject_description = subject_description;
    }

    public Subject() {}

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_description() {
        return subject_description;
    }

    public void setSubject_description(String subject_description) {
        this.subject_description = subject_description;
    }
}
