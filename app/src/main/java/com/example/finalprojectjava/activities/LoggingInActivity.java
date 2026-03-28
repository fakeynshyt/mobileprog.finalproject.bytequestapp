package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.PasswordHashHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

public class LoggingInActivity extends AppCompatActivity {

    Button loginAccount;
    EditText email, password;
    CheckBox checkRememberMe;
    TextView signInAct, forgotPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logging_in);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        loginAccount = findViewById(R.id.loginBtn);

        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);

        checkRememberMe = findViewById(R.id.rememberMeCb);

        signInAct = findViewById(R.id.signInClick);
        forgotPass = findViewById(R.id.forgotPassClick);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signInAct.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(LoggingInActivity.this,SigningInActivity.class));
            }, 500);
        });

        forgotPass.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(LoggingInActivity.this, ForgotPasswordOptionsActivity.class));
            }, 2000);
        });

        loginAccount.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Log.e(TAG, "Empty fields!");
                Toast.makeText(this, "Please fill up all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!email.getText().toString().endsWith("@bytequest.com")) {
                Log.e(TAG, "Invalid Email!");
                email.setError("Email must contain @bytequest.com");
                return;
            }

            SharedPreferences globalPrefs = getSharedPreferences("global_session", MODE_PRIVATE);
            User user = databaseHelper.loginUser(email.getText().toString());

            Log.e(TAG, "User Found: " + user.getUser_email());

            boolean passwordChecker = PasswordHashHelper.getInstance().passwordChecker(password.getText().toString(), user.getUser_pass());

            if(user != null && passwordChecker) {
                Log.e(TAG, "User found!");
                UserManager.getInstance().setCurrentUser(user);

                SessionManager session = new SessionManager(this, user.getUser_email());

                if(checkRememberMe.isChecked())  {
                    session.setKeyLoggedIn(true);
                    session.setKeySavedUser(user.getUser_email());

                    globalPrefs.edit().putString("lastUserEmail", user.getUser_email()).apply();

                    Log.e(TAG, "User registered: " + UserManager.getInstance().getCurrentUser().getFull_name());
                }
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(LoggingInActivity.this, DashboardActivity.class));
                }, 1500);
                finish();
            } else {
                Log.e(TAG, "User not found!");
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}