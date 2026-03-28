package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.MainActivity;
import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.PasswordHashHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.regex.Pattern;

public class SigningInActivity extends AppCompatActivity {

    EditText firstName, lastName, email, password, confirmPass;
    Button signInAcc;
    TextView loginClick;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signing_in);

        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        confirmPass = findViewById(R.id.inputConfirmPass);

        signInAcc = findViewById(R.id.signInBtn);
        loginClick = findViewById(R.id.loginClick);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginClick.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SigningInActivity.this, LoggingInActivity.class));
            }, 500);
        });

        signInAcc.setOnClickListener(v -> {
            User user;
            try {
                // Checks user input if empty
                if (firstName.getText().toString().isEmpty() ||
                        lastName.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {
                    Log.e(TAG, "Empty fields!");
                    Toast.makeText(this, "Please fill up all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checks user email if matches to email syntax
                if(!emailSyntaxCheck(email.getText().toString())) {
                    Log.e(TAG, "Invalid Email!");
                    email.setError("Email must contain @bytequest.com");
                    return;
                }

                // Checks user password if matches to the pattern
                if(!passwordSyntaxCheck(password.getText().toString())) {
                    Log.e(TAG, "Invalid Password!");
                    password.setError("Password must contain at least 8 characters,\n1 uppercase, 1 lowercase, 1 number and 1 special character");
                    return;
                }

                // Checks user password and confirm password input matches
                if(!password.getText().toString().equals(confirmPass.getText().toString())) {
                    Log.e(TAG, "Passwords do not match!");
                    confirmPass.setError("Passwords do not match!");
                    return;
                }

                // Creates a new user object
                user = new User(-1, email.getText().toString(),
                        password.getText().toString(), firstName.getText().toString(), lastName.getText().toString());

                // Hash Password
                user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(user.getUser_pass()));

                // Checks if error occurs in creating an account
            } catch (Exception e) {
                Log.e(TAG, "Error creating an account: " + e.getMessage());
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                user = new User(-1, null, null, null, null);
            }

            // Checks if creating an account is complete and successful
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            boolean success = databaseHelper.createUserAccount(user);
            Log.e(TAG, "Creating account status: " + success);

            if (success) {
                UserManager.getInstance().setCurrentUser(user);

                SessionManager session = new SessionManager(this, user.getUser_email());
                session.setKeySignedIn(true);
                session.setKeySavedUser(user.getUser_email());

                SharedPreferences globalPrefs = getSharedPreferences("global_session", MODE_PRIVATE);
                globalPrefs.edit().putString("lastUserEmail", user.getUser_email()).apply();

                Toast.makeText(this, "Account successfully created!", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SigningInActivity.this, DashboardActivity.class));
                }, 1500);
                finish();

            } else {
                Toast.makeText(this, "Account creation failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean emailSyntaxCheck(String email) {
        return email.endsWith("@bytequest.com");
    }
    private boolean passwordSyntaxCheck(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(password).matches();
    }

}