package com.example.finalprojectjava.styles;

public class OverviewProgressItem {
    private String itemTitle;
    private String itemProgress;
    private int progressValue;
    private int itemImage;

    public OverviewProgressItem(String itemTitle, String itemProgress, int progressValue, int itemImage) {
        this.itemTitle = itemTitle;
        this.itemProgress = itemProgress;
        this.progressValue = progressValue;
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemProgress() {
        return itemProgress;
    }

    public void setItemProgress(String itemProgress) {
        this.itemProgress = itemProgress;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
