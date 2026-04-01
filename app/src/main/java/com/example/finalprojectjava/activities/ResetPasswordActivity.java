package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.data.Database;
import com.example.finalprojectjava.helper.PasswordHashHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.helper.SnackBarHelper;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText et_new_pass, et_confirm_pass;
    Button btn_continue, btn_cancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

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
            onBackPressed();
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

            // Checks if password is empty
            if(newPass.isEmpty() || confirmPass.isEmpty()) {
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Required to fill-up all fields");

                if(newPass.isEmpty()) et_new_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(confirmPass.isEmpty())et_confirm_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_new_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_new_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_confirm_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_new_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                return;
            }

            // Checks if passwords align with the conditions of pattern
            if(!passwordSyntaxCheck(newPass)) {
                new Handler().postDelayed(() -> {
                    SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Password must be 8+ characters with upper & lowercase, number, and special symbol.");
                }, 500);

                et_new_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_new_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_new_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                return;
            }

            // Checks if passwords match to confirm password
            if(!newPass.equals(confirmPass)) {
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Password didn't match");
                et_confirm_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_confirm_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_new_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                        et_confirm_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                return;
            }

            // Checks if new password is the same as the old password
            if(newPass.equals(oldPassword)) {
                new Handler().postDelayed(() -> {
                    SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "New password cannot be the same as the old password");
                    et_new_pass.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                    et_new_pass.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            et_new_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                            et_confirm_pass.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this, R.drawable.bg_background_edittext));
                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });
                }, 800);
                return;
            }

            // Save new password
            prefsHelper.setString("plain_text_pass_key", newPass);

            String newPlainPass = prefsHelper.getString("plain_text_pass_key", "");

            Log.e(TAG, "User email: " + UserManager.getInstance().getCurrentUser().getUser_email()
                    + "\nNew password: " + newPlainPass);

            try {
                Database db = new Database(this);

                // Return user object from database using UserManager
                User user = db.getUserByEmail(UserManager.getInstance().getCurrentUser().getUser_email());

                // Returns and set hashed password
                user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(newPass));

                // Sets new hashed password to the database
                db.resetUserPassword(UserManager.getInstance().getCurrentUser().getUser_email(), user.getUser_pass());

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(this, SuccessActivity.class);
                    intent.putExtra("title_key", "Your Password Has Been Reset!");
                    intent.putExtra("description_key", "Your password has been updated! Please log in with\nyour new credentials to keep your account secure!");
                    intent.putExtra("where_key", "Login");
                    startActivity(intent);
                    finish();
                }, 4500);

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

    @Override
    public void onBackPressed() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(15f); // corner radius in pixels
        drawable.setColor(Color.WHITE); // background color

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Hold on!")
                .setMessage("You forgot your password. Leaving now means you can’t log in. Do you want to cancel?")
                .setPositiveButton("Yes", (dialogOpen, which) -> super.onBackPressed())
                .setNegativeButton("No", null)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(drawable);
        }

        dialog.show();
    }
}