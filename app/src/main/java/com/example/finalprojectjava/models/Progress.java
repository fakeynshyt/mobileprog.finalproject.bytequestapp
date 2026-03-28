package com.example.finalprojectjava.models;

public class Progress {
    private int progress_module_id;
    private int progress_user_id;
    private boolean progress_status;

    public Progress(int progress_module_id, int progress_user_id, boolean progress_status) {
        this.progress_module_id = progress_module_id;
        this.progress_user_id = progress_user_id;
        this.progress_status = progress_status;
    }

    public Progress() {}

    public int getProgress_module_id() {
        return progress_module_id;
    }

    public void setProgress_module_id(int progress_module_id) {
        this.progress_module_id = progress_module_id;
    }

    public int getProgress_user_id() {
        return progress_user_id;
    }

    public void setProgress_user_id(int progress_user_id) {
        this.progress_user_id = progress_user_id;
    }

    public boolean isProgress_status() {
        return progress_status;
    }

    public void setProgress_status(boolean progress_status) {
        this.progress_status = progress_status;
    }
}
