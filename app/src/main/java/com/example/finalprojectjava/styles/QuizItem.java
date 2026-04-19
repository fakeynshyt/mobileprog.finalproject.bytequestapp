package com.example.finalprojectjava.styles;

public class QuizItem {
    private int itemId;
    private int itemNumber;
    private String itemQuestion;
    private String itemTitle;
    private int itemReqLevel;
    private boolean itemAnswered;

    public QuizItem(int itemId, int itemNumber, String itemQuestion, String itemTitle, int itemReqLevel, boolean itemAnswered) {
        this.itemId = itemId;
        this.itemNumber = itemNumber;
        this.itemQuestion = itemQuestion;
        this.itemTitle = itemTitle;
        this.itemReqLevel = itemReqLevel;
        this.itemAnswered = itemAnswered;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemQuestion() {
        return itemQuestion;
    }

    public void setItemQuestion(String itemQuestion) {
        this.itemQuestion = itemQuestion;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getItemReqLevel() {
        return itemReqLevel;
    }

    public void setItemReqLevel(int itemReqLevel) {
        this.itemReqLevel = itemReqLevel;
    }

    public boolean isItemAnswered() {
        return itemAnswered;
    }

    public void setItemAnswered(boolean itemAnswered) {
        this.itemAnswered = itemAnswered;
    }
}
