package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.PasswordHashHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

public class LoggingInActivity extends AppCompatActivity {

    Button btn_login;
    EditText et_email, et_password;
    CheckBox cbx_remember_me;
    TextView txt_sign_in_click, txt_forgot_password_click;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logging_in);

        // Assign widgets from XML
        btn_login = findViewById(R.id.loginBtn);

        et_email = findViewById(R.id.emailInput);
        et_password = findViewById(R.id.passwordInput);

        cbx_remember_me = findViewById(R.id.rememberMeCb);

        txt_sign_in_click = findViewById(R.id.signInClick);
        txt_forgot_password_click = findViewById(R.id.forgotPassClick);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to signing in activity
        txt_sign_in_click.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(LoggingInActivity.this,SigningInActivity.class));
            }, 500);
        });

        // Navigate to forgot password activity
        txt_forgot_password_click.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(LoggingInActivity.this, ForgotPasswordOptionsActivity.class));
            }, 2000);
        });

        // Login user
        btn_login.setOnClickListener(v -> {

            // Checks user input if empty
            if(et_email.getText().toString().isEmpty() || et_password.getText().toString().isEmpty()) {
                Log.e(TAG, "Empty fields!");
                Toast.makeText(this, "Please fill up all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checks user email if matches to email syntax
            if(!et_email.getText().toString().endsWith("@bytequest.com")) {
                Log.e(TAG, "Invalid Email!");
                et_email.setError("Email must contain @bytequest.com");
                return;
            }

            // Open database connection and login user
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            User user = databaseHelper.loginUser(et_email.getText().toString());

            Log.e(TAG, "User Found: " + user.getUser_email());

            // Checks if user password is correct
            boolean passwordChecker = PasswordHashHelper.getInstance().passwordChecker(et_password.getText().toString(), user.getUser_pass());

            // Checks if user is found and password is correct
            if(user != null && passwordChecker) {
                Log.e(TAG, "User found!");

                // Set user as current user for quick lookup
                UserManager.getInstance().setCurrentUser(user);

                // Save user email for backup & safety
                SessionManager session = new SessionManager(this, user.getUser_email());

                if(cbx_remember_me.isChecked())  {

                    // Set remembered me for quick login
                    session.setKeyRememberMe(true);

                    // Set user email backup for UserSession
                    PrefsHelper prefsHelper = new PrefsHelper(this);
                    prefsHelper.setString("user_email_key", user.getUser_email());

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