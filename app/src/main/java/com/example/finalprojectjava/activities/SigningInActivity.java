package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.PasswordHashHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.helper.SnackBarHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.regex.Pattern;

public class SigningInActivity extends AppCompatActivity {

    EditText et_first_name, et_last_name, et_email, et_pass, et_confirm_pass;
    Button btn_sign_in;
    TextView txt_login_click;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signing_in);

        // Assign widgets from XML
        et_first_name = findViewById(R.id.inputFirstName);
        et_last_name = findViewById(R.id.inputLastName);
        et_email = findViewById(R.id.inputEmail);
        et_pass = findViewById(R.id.inputPassword);
        et_confirm_pass = findViewById(R.id.inputConfirmPass);

        btn_sign_in = findViewById(R.id.signInBtn);
        txt_login_click = findViewById(R.id.loginClick);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to logging in activity
        txt_login_click.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SigningInActivity.this, LoggingInActivity.class));
            }, 500);
        });

        // Create a new account
        btn_sign_in.setOnClickListener(v -> {

            // Checks user input if empty
            if (et_first_name.getText().toString().isEmpty() ||
                    et_last_name.getText().toString().isEmpty() ||
                    et_email.getText().toString().isEmpty() ||
                    et_pass.getText().toString().isEmpty() ||
                    et_confirm_pass.getText().toString().isEmpty()) {

                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Required to fill-up all fields");

                if(et_first_name.getText().toString().isEmpty()) et_first_name.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_last_name.getText().toString().isEmpty()) et_last_name.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_email.getText().toString().isEmpty()) et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_pass.getText().toString().isEmpty()) et_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_confirm_pass.getText().toString().isEmpty()) et_confirm_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_first_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_last_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_last_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_last_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_last_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_last_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));

                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_confirm_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_first_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_last_name.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                return;
            }

            // Checks user email if matches to email syntax
            if(!emailSyntaxCheck(et_email.getText().toString())) {

                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Invalid email");

                et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
            }

            // Checks user password if matches to the pattern
            if(!passwordSyntaxCheck(et_pass.getText().toString())) {

                new Handler().postDelayed(() -> {
                    SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Password must be 8+ characters with upper & lowercase, number, and special symbol.");
                }, 500);

                et_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                return;
            }

            // Checks user password and confirm password input matches
            if(!et_pass.getText().toString().equals(et_confirm_pass.getText().toString())) {
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Password didn't match");
                et_confirm_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_confirm_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                return;
            }

            // Handles application error
            try {
                // Creates a new user object
                User user = new User(-1, et_email.getText().toString(),
                        et_pass.getText().toString(), et_first_name.getText().toString(), et_last_name.getText().toString());

                // Saved plain text password
                PrefsHelper prefsHelper = new PrefsHelper(this, user.getUser_email());
                prefsHelper.setString("plain_text_pass_key", user.getUser_pass());

                // Hash password
                user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(user.getUser_pass()));

                // Checks if creating an account is complete and successful and store user in database
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                boolean success = databaseHelper.createUserAccount(user);
                Log.e(TAG, "Creating account status: " + success);

                // If creating an account is success
                if (success) {
                    // Saved user for quick lookup
                    UserManager.getInstance().setCurrentUser(user);

                    // Save user email for backup & safety
                    SessionManager session = new SessionManager(this, user.getUser_email());

                    // Set new user and edit profile when new user
                    session.setKeyNewUser(true);

                    new Handler().postDelayed(() -> {
                        SnackBarHelper.showSuccessSnackBar(findViewById(R.id.main), "User account successfully created!");
                    }, 1000);

                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(SigningInActivity.this, DashboardActivity.class));
                        finish();
                    }, 1500);
                } else {
                    new Handler().postDelayed(() -> {
                        SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "User account is already existing");

                        et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                        et_email.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable s) {
                                et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                            }
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {}
                        });
                    }, 500);
                }
                // Catches application error to prevent crash
            } catch (Exception e) {
                Log.e(TAG, "Error creating an account: " + e.getMessage());
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Something went wrong");

                et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_email.setBackground(ContextCompat.getDrawable(SigningInActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
            }
        });
    }

    // Private Methods
    private boolean emailSyntaxCheck(String email) {
        return email.endsWith("@bytequest.com");
    }
    private boolean passwordSyntaxCheck(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(password).matches();
    }

}