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
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

public class LoginActivity extends AppCompatActivity {

    Button loginAct, signInAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        String lastUserEmail = getSharedPreferences("global_session", MODE_PRIVATE).getString("lastUserEmail", null);

        if(lastUserEmail != null) {

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SessionManager session = new SessionManager(this, lastUserEmail);
            boolean loggedIn = session.isLoggedIn();
            boolean hasUser = dbHelper.hasUserAccount();

            if(loggedIn && !hasUser) {
                session.clearSession();
                return;
            }

            if(loggedIn) {
                Intent intent = new Intent(this, LessonActivity.class);
                String savedUser = session.getKeySavedUser();
                User user = dbHelper.getUserByEmail(savedUser);

                UserManager.getInstance().setCurrentUser(user);
                startActivity(intent);
            }
        }

        loginAct = findViewById(R.id.loginBtn);
        signInAct = findViewById(R.id.signInBtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(LoginActivity.this, LoggingInActivity.class));
                }, 500);
            }
        });

        signInAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(LoginActivity.this, SigningInActivity.class));
                }, 500);
            }
        });
    }
}