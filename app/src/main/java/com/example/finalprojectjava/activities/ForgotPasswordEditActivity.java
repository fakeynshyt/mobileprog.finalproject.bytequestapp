package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.regex.Pattern;

public class ForgotPasswordEditActivity extends AppCompatActivity {

    EditText et_new_pass, et_confirm_pass;
    Button btn_continue, btn_cancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_edit);

        // Assign widgets from XML
        et_new_pass = findViewById(R.id.inputNewPass);
        et_confirm_pass = findViewById(R.id.inputConfirmPass);

        btn_continue = findViewById(R.id.btnContinue);
        btn_cancel = findViewById(R.id.btnCancel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to logging in activity
        btn_cancel.setOnClickListener(v -> {
            finish();
        });

        // Create new password
        btn_continue.setOnClickListener(v -> {
            String newPass = et_new_pass.getText().toString();
            String confirmPass = et_confirm_pass.getText().toString();

            // Gets old password from shared preferences
            PrefsHelper prefsHelper = new PrefsHelper(this, UserManager.getInstance().getCurrentUser().getUser_email());
            String oldPassword = prefsHelper.getString("plain_text_pass_key", "");

            Log.e(TAG, "User email: " + UserManager.getInstance().getCurrentUser().getUser_email()
                    + "\nOld password: " + oldPassword);

            try {
                // Checks if password is empty
                if(newPass.isEmpty()) {
                    et_confirm_pass.setError("Password cannot be empty. Please try again");
                    return;
                }

                // Checks if passwords align with the conditions of pattern
                if(!passwordSyntaxCheck(newPass)) {
                    et_new_pass.setError("Password must contain at least 8 characters,\n1 uppercase, 1 lowercase, 1 number and 1 special character");
                    return;
                }

                // Checks if passwords match to confirm password
                if(!newPass.equals(confirmPass)) {
                    et_confirm_pass.setError("Passwords do not match. Please try again");
                    return;
                }

                // Checks if new password is the same as the old password
                if(newPass.equals(oldPassword)) {
                    et_new_pass.setError("New password cannot be the same as the old. Please try again");
                    return;
                }

                // Save new password
                prefsHelper.setString("plain_text_pass_key", newPass);

                String newPlainPass = prefsHelper.getString("plain_text_pass_key", "");

                Log.e(TAG, "User email: " + UserManager.getInstance().getCurrentUser().getUser_email()
                        + "\nNew password: " + newPlainPass);

                DatabaseHelper db = new DatabaseHelper(this);

                // Return user object from database using UserManager
                User user = db.getUserByEmail(UserManager.getInstance().getCurrentUser().getUser_email());

                // Returns and set hashed password
                user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(newPass));

                // Sets new hashed password to the database
                db.resetUserPassword(UserManager.getInstance().getCurrentUser().getUser_email(), user.getUser_pass());

                new Handler().postDelayed(() -> {
                    Toast.makeText(this, "Successfully change password!", Toast.LENGTH_SHORT).show();
                }, 1000);

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ForgotPasswordEditActivity.this, LoggingInActivity.class));
                    finish();
                }, 2500);

            } catch(Exception e) {
                Toast.makeText(this, "Something went wrong!\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Private Method
    private boolean passwordSyntaxCheck(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(password).matches();
    }
}