package com.example.finalprojectjava.helpers;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashHelper {
    private static PasswordHashHelper instance;

    public PasswordHashHelper() {}

    public static PasswordHashHelper getInstance() {
        if(instance == null) {
            instance = new PasswordHashHelper();
        }
        return instance;
    }
    public String passwordHasher(String password) {
        String passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return passwordHashed;
    }

    public boolean passwordChecker(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
