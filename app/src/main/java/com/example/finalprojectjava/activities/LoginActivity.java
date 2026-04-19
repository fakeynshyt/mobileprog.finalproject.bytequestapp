package com.example.finalprojectjava.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import com.example.finalprojectjava.managers.SessionManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.User;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText et_email, et_password;
    CheckBox cbx_remember_me;
    TextView txt_sign_in_click, txt_forgot_password_click;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

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

        txt_sign_in_click.setOnClickListener(v -> startActivity(new Intent(this, SignInActivity.class)));

        txt_forgot_password_click.setOnClickListener(v -> {
            hideKeyboard(et_email, et_password);
            SnackBarHelperActivity.showInfoSnackBar(findViewById(R.id.main), "Please wait...");
            new Handler().postDelayed(() -> startActivity(new Intent(this, ResetOptionActivity.class)), 1500);
        });

        btn_login.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        hideKeyboard(et_email, et_password);

        if (isEmpty(et_email, et_password)) {
            SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), "Required to fill-up all fields");
            setErrorFields(et_email, et_password);
            addTextListeners(et_email, et_password);
            return;
        }

        if (!et_email.getText().toString().endsWith("@bytequest.com")) {
            SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), "Invalid email");
            setErrorFields(et_email);
            addTextListeners(et_email);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO(this);
            User user = userDAO.loginUser(et_email.getText().toString());

            boolean passwordChecker = PasswordHashHelper.getInstance()
                    .passwordChecker(et_password.getText().toString(), user.getUser_pass());

            if (user.getUser_email() != null && passwordChecker) {
                loginSuccess(user);
            } else {
                loginError("Invalid password", et_password);
            }

        } catch (Exception e) {
            loginError("User account is not existing", et_email);
        }
    }

    private void loginSuccess(User user) {
        UserManager.getInstance().setCurrentUser(user);
        SessionManager session = new SessionManager(this, user.getUser_email());

        if (cbx_remember_me.isChecked()) {
            session.setKeyRememberMe(true);
            new PrefsHelper(this).setString("user_email_key", user.getUser_email());
        }

        SnackBarHelperActivity.showSuccessSnackBar(findViewById(R.id.main), "User account successfully logged in!");

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }, 1500);
    }

    private void loginError(String message, EditText field) {
        SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), message);
        setErrorFields(field);
        addTextListeners(field);
    }

    private boolean isEmpty(EditText... fields) {
        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) return true;
        }
        return false;
    }

    private void setErrorFields(EditText... fields) {
        for (EditText field : fields) {
            field.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
        }
    }

    private void setDefaultFields(EditText... fields) {
        for (EditText field : fields) {
            field.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext));
        }
    }

    private void addTextListeners(EditText... fields) {
        for (EditText field : fields) {
            field.addTextChangedListener(new TextWatcher() {
                @Override public void afterTextChanged(Editable s) {
                    setDefaultFields(fields);
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }

    private void hideKeyboard(EditText... fields) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        for (EditText field : fields) {
            imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
        }
    }
}