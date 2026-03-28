package com.example.finalprojectjava.models;

public class Game {
    private int game_user_id;
    private String game_name;
    private String game_description;
    private int game_score;

    public Game(int game_user_id, String game_name, String game_description, int game_score) {
        this.game_user_id = game_user_id;
        this.game_name = game_name;
        this.game_description = game_description;
        this.game_score = game_score;
    }

    public Game() {}

    public int getGame_user_id() {
        return game_user_id;
    }

    public void setGame_user_id(int game_user_id) {
        this.game_user_id = game_user_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_description() {
        return game_description;
    }

    public void setGame_description(String game_description) {
        this.game_description = game_description;
    }

    public int getGame_score() {
        return game_score;
    }

    public void setGame_score(int game_score) {
        this.game_score = game_score;
    }
}
