package com.example.finalprojectjava.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.UserDAO;
import com.example.finalprojectjava.helpers.PrefsHelper;
import com.example.finalprojectjava.helpers.SnackBarHelperActivity;
import com.example.finalprojectjava.managers.SessionManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.User;

public class StartUpActivity extends AppCompatActivity {

    Button btn_login, btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startup);

        // Assign widgets from XML
        btn_login = findViewById(R.id.loginBtn);
        btn_sign_in = findViewById(R.id.signInBtn);

        // Asks user permission to open notifications for the application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Get user email from shared preferences
        PrefsHelper prefsHelper = new PrefsHelper(this);
        String savedEmail = prefsHelper.getString("user_email_key", null);

        // Checks if saved email is null or not null
        if(savedEmail != null) {
            UserDAO userDAO = new UserDAO(this);
            SessionManager session = new SessionManager(this, savedEmail);
            boolean loggedIn = session.isRemembered();
            boolean hasUser = userDAO.hasUserAccount();

            // Checks and clears session if user is not remembered and database is null
            if(loggedIn && !hasUser) {
                session.clearSession();
                return;
            }

            // If user is logged in, navigate to dashboard activity
            if(loggedIn) {
                Intent intent = new Intent(this, DashboardActivity.class);
                String savedUser = session.getKeySavedUser();
                User user = userDAO.getUserByEmail(savedUser);

                UserManager.getInstance().setCurrentUser(user);
                startActivity(intent);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to logging in activity
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackBarHelperActivity.showWarningSnackBar(findViewById(R.id.main), "Error!");
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(StartUpActivity.this, LoginActivity.class));
                }, 500);
            }
        });

        // Navigate to signing in activity
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(StartUpActivity.this, SignInActivity.class));
                }, 500);
            }
        });
    }
}