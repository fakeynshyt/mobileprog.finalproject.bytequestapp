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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.UserDAO;
import com.example.finalprojectjava.helpers.PasswordHashHelper;
import com.example.finalprojectjava.helpers.PrefsHelper;
import com.example.finalprojectjava.helpers.SnackBarHelperActivity;
import com.example.finalprojectjava.managers.UserManager;
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

        et_new_pass = findViewById(R.id.inputNewPass);
        et_confirm_pass = findViewById(R.id.inputConfirmPass);
        btn_continue = findViewById(R.id.btnContinue);
        btn_cancel = findViewById(R.id.btnCancel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_cancel.setOnClickListener(v -> onBackPressed());

        btn_continue.setOnClickListener(v -> handlePasswordReset());
    }

    private void handlePasswordReset() {
        String newPass = et_new_pass.getText().toString();
        String confirmPass = et_confirm_pass.getText().toString();

        PrefsHelper prefsHelper = new PrefsHelper(this, UserManager.getInstance().getCurrentUser().getUser_email());
        String oldPassword = prefsHelper.getString("plain_text_pass_key", "");

        if (!validateFields(newPass, confirmPass, oldPassword)) return;

        prefsHelper.setString("plain_text_pass_key", newPass);

        try {
            UserDAO userDAO = new UserDAO(this);
            User user = userDAO.getUserByEmail(UserManager.getInstance().getCurrentUser().getUser_email());
            user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(newPass));
            userDAO.resetUserPassword(UserManager.getInstance().getCurrentUser().getUser_email(), user.getUser_pass());

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, SuccessActivity.class);
                intent.putExtra("title_key", "Your Password Has Been Reset!");
                intent.putExtra("description_key", "Your password has been updated! Please log in with\nyour new credentials to keep your account secure!");
                intent.putExtra("where_key", "Login");
                startActivity(intent);
                finish();
            }, 1500);

        } catch (Exception e) {
            SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), "Something went wrong!");
            Log.e(TAG, "Error resetting password: " + e.getMessage());
        }
    }

    // Validates the fields and shows errors
    private boolean validateFields(String newPass, String confirmPass, String oldPassword) {
        EditText[] fields = {et_new_pass, et_confirm_pass};

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            showFieldError(fields, "Required to fill-up all fields");
            return false;
        }

        if (!passwordSyntaxCheck(newPass)) {
            showFieldError(new EditText[]{et_new_pass}, "Password must be 8+ characters with upper & lowercase, number, and special symbol.");
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            showFieldError(new EditText[]{et_confirm_pass}, "Password didn't match");
            return false;
        }

        if (newPass.equals(oldPassword)) {
            showFieldError(new EditText[]{et_new_pass}, "New password cannot be the same as the old password");
            return false;
        }

        return true;
    }

    // Shows SnackBar and sets background error, also adds listener to reset background
    private void showFieldError(EditText[] fields, String message) {
        SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), message);

        for (EditText field : fields) {
            field.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
            addResetBackgroundListener(field);
        }
    }

    // Resets field background on text change
    private void addResetBackgroundListener(EditText field) {
        field.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { resetBackground(field); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void resetBackground(EditText field) {
        field.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext));
    }

    // Checks password pattern
    private boolean passwordSyntaxCheck(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(regex).matcher(password).matches();
    }

    @Override
    public void onBackPressed() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(15f);
        drawable.setColor(Color.WHITE);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_bytequest_logo_alt)
                .setTitle("Hold on!")
                .setMessage("You forgot your password. Leaving now means you can’t log in. Do you want to cancel?")
                .setPositiveButton("Yes", (d, which) -> super.onBackPressed())
                .setNegativeButton("No", null)
                .create();

        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.show();
    }
}