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

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    EditText et_first_name, et_last_name, et_email, et_pass, et_confirm_pass;
    Button btn_sign_in;
    TextView txt_login_click;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

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

        txt_login_click.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        btn_sign_in.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {

        if (isEmpty(et_first_name, et_last_name, et_email, et_pass, et_confirm_pass)) {
            SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), "Required to fill-up all fields");
            setErrorFields(et_first_name, et_last_name, et_email, et_pass, et_confirm_pass);
            addTextListeners(et_first_name, et_last_name, et_email, et_pass, et_confirm_pass);
            return;
        }

        if (!emailSyntaxCheck(et_email.getText().toString())) {
            showFieldError("Invalid email", et_email);
            return;
        }

        if (!passwordSyntaxCheck(et_pass.getText().toString())) {
            showFieldError("Password must be 8+ characters with upper & lowercase, number, and special symbol.", et_pass);
            return;
        }

        if (!et_pass.getText().toString().equals(et_confirm_pass.getText().toString())) {
            showFieldError("Password didn't match", et_confirm_pass, et_pass);
            return;
        }

        try {
            User user = new User(-1,
                    et_email.getText().toString(),
                    et_pass.getText().toString(),
                    et_first_name.getText().toString(),
                    et_last_name.getText().toString());

            PrefsHelper prefsHelper = new PrefsHelper(this, user.getUser_email());
            prefsHelper.setString("plain_text_pass_key", user.getUser_pass());

            new PrefsHelper(this).setString("user_email_key", user.getUser_email());

            user.setUser_pass(PasswordHashHelper.getInstance().passwordHasher(user.getUser_pass()));
            UserDAO userDAO = new UserDAO(this);
            boolean success = userDAO.createUserAccount(user);

            if (success) {
                // Fetch user again to get the generated ID from DB
                User savedUser = userDAO.getUserByEmail(user.getUser_email());
                signUpSuccess(savedUser);
            } else {
                showFieldError("User account is already existing", et_email);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error creating account: " + e.getMessage());
            showFieldError("Something went wrong", et_email);
        }
    }

    private void signUpSuccess(User user) {
        UserManager.getInstance().setCurrentUser(user);
        SessionManager session = new SessionManager(this, user.getUser_email());
        session.setKeyNewUser(true);

        SnackBarHelperActivity.showSuccessSnackBar(findViewById(R.id.main), "User account successfully created!");

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }, 1500);
    }

    private void showFieldError(String message, EditText field) {
        SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), message);
        setErrorFields(field);
        addTextListeners(field);
    }

    private void showFieldError(String message, EditText field1, EditText field2) {
        SnackBarHelperActivity.showErrorSnackBar(findViewById(R.id.main), message);
        setErrorFields(field1, field2);
        addTextListeners(field1, field2);
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

    private boolean emailSyntaxCheck(String email) {
        return email.endsWith("@bytequest.com");
    }

    private boolean passwordSyntaxCheck(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(regex).matcher(password).matches();
    }
}
