package com.example.finalprojectjava.managers;

import com.example.finalprojectjava.models.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;
    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    public User getCurrentUser() {
        return currentUser;
    }
}
