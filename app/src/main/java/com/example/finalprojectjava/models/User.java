package com.example.finalprojectjava.models;

public class User {
    private int user_id;
    private String user_email;
    private String user_pass;
    private String first_name;
    private String last_name;
    private String birth_date;
    private String gender;
    private String username;
    private String address;
    private int level;
    private int exp;

    public User(int user_id, String user_email, String user_pass, String first_name, String last_name) {
        this.user_email = user_email;
        this.user_id = user_id;
        this.user_pass = user_pass;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = "";
        this.birth_date = "";
        this.address = "";
        this.username = "";
        this.level = 1;
        this.exp = 0;
    }

    public User() {}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getFull_name() {
        return getFirst_name() + " " + getLast_name();
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
