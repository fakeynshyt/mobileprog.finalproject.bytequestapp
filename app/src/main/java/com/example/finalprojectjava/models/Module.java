package com.example.finalprojectjava.models;

public class Module {
    private int module_id;
    private String module_name;
    private int module_user_id;
    private int module_subject_id;
    private int module_level;

    public Module(int module_id, String module_name, int module_user_id, int module_subject_id, int module_level) {
        this.module_id = module_id;
        this.module_name = module_name;
        this.module_user_id = module_user_id;
        this.module_subject_id = module_subject_id;
        this.module_level = module_level;
    }

    public Module() {}

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public int getModule_user_id() {
        return module_user_id;
    }

    public void setModule_user_id(int module_user_id) {
        this.module_user_id = module_user_id;
    }

    public int getModule_subject_id() {
        return module_subject_id;
    }

    public void setModule_subject_id(int module_subject_id) {
        this.module_subject_id = module_subject_id;
    }

    public int getModule_level() {
        return module_level;
    }

    public void setModule_level(int module_level) {
        this.module_level = module_level;
    }
}
