package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.NotificationHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.helper.SnackBarHelper;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.Random;

public class PasswordVerificationActivity extends AppCompatActivity {

    Button send_btn;
    EditText et_email;
    TextView txt_prompt ,txt_resend_code, txt_counter;
    ImageView iv_go_back_click;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_verification_activity);

        // Assign widgets from XML
        send_btn = findViewById(R.id.sendBtn);
        et_email = findViewById(R.id.emailInput);

        iv_go_back_click = findViewById(R.id.goBackClickIV);
        txt_prompt = findViewById(R.id.promptTxt);
        txt_resend_code = findViewById(R.id.resendTxt);
        txt_counter = findViewById(R.id.counterTxt);

        et_email.requestFocus();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iv_go_back_click.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                onBackPressed();
            }, 500);
        });


        // Checks if otp counter is currently threading
        PrefsHelper prefsHelper = new PrefsHelper(this);
        long expiry = prefsHelper.getLong("otp_expiry_key", 0);

        // Set text views to null if expiry = 0, if not start counter
        if(System.currentTimeMillis() > expiry) {
            txt_prompt.setText(null);
            txt_resend_code.setText(null);
            txt_counter.setText(null);
        } else {
            otpCodeCounter(expiry);
        }

        // Verify email
        send_btn.setOnClickListener(view -> {
            String email = et_email.getText().toString();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0);


            // Checks if email is empty
            if(email.isEmpty()) {
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Email cannot be empty");

                et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_email.setBackground(ContextCompat.getDrawable(PasswordVerificationActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                return;
            }

            // Checks if email syntax matches with the specified domain
            if(!et_email.getText().toString().endsWith("@bytequest.com")) {
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Invalid email");
                et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        et_email.setBackground(ContextCompat.getDrawable(PasswordVerificationActivity.this, R.drawable.bg_background_edittext));
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                return;
            }
            try {
                // Returns user from database if email input matches
                DatabaseHelper db = new DatabaseHelper(this);
                User userExists = db.getUserByEmail(email);

                // Generates random number for otp code
                int verificationCode = new Random().nextInt(9000) + 1000;
                Log.e(TAG, "OTP code: " + verificationCode);

                // Checks if user exists in database
                if (userExists != null) {
                    UserManager.getInstance().setCurrentUser(userExists);

                    // Sends otp notification
                    NotificationHelper otpNotification = new NotificationHelper(this);
                    int relayCodeMillis = new Random().nextInt(6500) + (2000 * 2);

                    // handles the sending of notification
                    new Handler().postDelayed(() -> {
                        otpNotification.showNotification("ByteQuest", "Your OTP is " + verificationCode  + ", do not share this code to anyone.", 1);
                    }, relayCodeMillis);

                    new Handler().postDelayed(() -> {
                        SnackBarHelper.showSuccessSnackBar(findViewById(R.id.main), "User account successfully found!");
                    }, 800);

                    // handles the navigation to forgot password otp activity
                    new Handler().postDelayed(() -> {
                        // Sets 59 seconds for otp code to resend
                        prefsHelper.setLong("otp_expiry_key", System.currentTimeMillis() + 59000);

                        // Sets otp code for later checking
                        prefsHelper.setInt("otp_code_key", verificationCode);

                        // Gets otp code expiry
                        long codeExpiry = prefsHelper.getLong("otp_expiry_key", 0);

                        // Starts counter
                        otpCodeCounter(codeExpiry);

                        startActivity(new Intent(PasswordVerificationActivity.this, VerificationOTPActivity.class));
                        finish();
                    }, 2000);
                } else {
                    new Handler().postDelayed(() -> {
                        SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "User account is not existing");
                        et_email.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                        et_email.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable s) {
                                et_email.setBackground(ContextCompat.getDrawable(PasswordVerificationActivity.this, R.drawable.bg_background_edittext));
                            }
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {}
                        });
                    }, 500);
                }
            } catch(Exception e) {
                Log.e(TAG, "User account not found!");
                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Something went wrong!");
            }
        });
    }

    // Private method
    private void otpCodeCounter(long timer) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long remaining = timer - System.currentTimeMillis();

                if(remaining > 0) {
                    int secs = (int) (remaining / 1000);
                    secs = secs % 60;

                    txt_prompt.setText("Didn't receive the code?");
                    txt_resend_code.setText(" Resend");
                    txt_counter.setText(" - 00:" + String.format("%02d", secs));

                    handler.postDelayed(this, 1000);

                    send_btn.setEnabled(false);
                    send_btn.setTextColor(Color.parseColor("#BEB2C8"));
                    send_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69499E")));

                    if(secs <= 0) {
                        txt_prompt.setText(null);
                        txt_counter.setText(null);
                        txt_resend_code.setText(null);
                        send_btn.setEnabled(true);
                        send_btn.setTextColor(Color.parseColor("#FFFFFFFF"));
                        send_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6725C7")));
                    }
                }
            }
        };
        handler.post(runnable);
    }
}