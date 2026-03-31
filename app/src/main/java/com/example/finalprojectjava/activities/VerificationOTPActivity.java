package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.helper.NotificationHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.helper.SnackBarHelper;
import com.example.finalprojectjava.manager.UserManager;

import java.util.Random;

public class VerificationOTPActivity extends AppCompatActivity {

    EditText et_otp_1, et_otp_2, et_otp_3, et_otp_4;
    Button btn_verify;
    TextView txt_display_user, txt_resend_code, txt_code_counter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_otpactivity);

        // Assign widgets from XML
        et_otp_1 = findViewById(R.id.otp1ET);
        et_otp_2 = findViewById(R.id.otp2ET);
        et_otp_3 = findViewById(R.id.otp3ET);
        et_otp_4 = findViewById(R.id.otp4ET);

        btn_verify = findViewById(R.id.verifyBtn);

        txt_display_user = findViewById(R.id.displayUserTxt);
        txt_resend_code = findViewById(R.id.resendTxt);
        txt_code_counter = findViewById(R.id.counterTxt);

        String hideUserAccount = hideUserAccount(UserManager.getInstance().getCurrentUser().getUser_email());

        // Sets display user to the UI
        txt_display_user.setText(hideUserAccount);

        // Autofocus otp fields
        et_otp_1.requestFocus();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up OTP fields
        setupOTPField(et_otp_1, et_otp_2);
        setupOTPField(et_otp_2, et_otp_3);
        setupOTPField(et_otp_3, et_otp_4);
        setupOTPField(et_otp_4, null);

        // Remove OTP fields
        removeOTPField(et_otp_4, et_otp_3);
        removeOTPField(et_otp_3, et_otp_2);
        removeOTPField(et_otp_2, et_otp_1);

        // Checks if otp counter is currently threading
        PrefsHelper prefsHelper = new PrefsHelper(this);
        long expiry = prefsHelper.getLong("otp_expiry_key", 0);

        // Starts counter thread
        otpCodeCounter(expiry);

        // Verify code
        btn_verify.setOnClickListener(v -> {

            InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(et_otp_1.getWindowToken(), 0);

            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(et_otp_2.getWindowToken(), 0);

            InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm3.hideSoftInputFromWindow(et_otp_3.getWindowToken(), 0);

            InputMethodManager imm4 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm4.hideSoftInputFromWindow(et_otp_4.getWindowToken(), 0);

            if(et_otp_1.getText().toString().isEmpty()
                    || et_otp_2.getText().toString().isEmpty()
                    || et_otp_3.getText().toString().isEmpty()
                    || et_otp_4.getText().toString().isEmpty()) {

                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "OTP code cannot be empty");

                if(et_otp_1.getText().toString().isEmpty()) et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                if(et_otp_2.getText().toString().isEmpty()) et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                if(et_otp_3.getText().toString().isEmpty()) et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                if(et_otp_4.getText().toString().isEmpty()) et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));

                et_otp_1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_otp_2.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_otp_3.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                et_otp_4.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                return;
            }

            // Gets the otp code from prefs key
            int code = prefsHelper.getInt("otp_code_key", 0);

            // Gets all the otp fields and converts to string
            String otpField = et_otp_1.getText().toString()
                    + et_otp_2.getText().toString()
                    + et_otp_3.getText().toString()
                    + et_otp_4.getText().toString();

            // Checks if otpField matches to the notification code
            if(checkCode(otpField, String.valueOf(code))) {
                new Handler().postDelayed(() -> {
                    SnackBarHelper.showSuccessSnackBar(findViewById(R.id.main), "OTP code successfully verified!");
                }, 1000);
                new Handler().postDelayed(() -> {
                    SnackBarHelper.showInfoSnackBar(findViewById(R.id.main), "Redirecting to reset password...");
                }, 4500);
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(VerificationOTPActivity.this, ResetPasswordActivity.class));
                    finish();
                }, 7500);
            } else {
                new Handler().postDelayed(() -> {

                    SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Invalid OTP code");

                    et_otp_1.setText("");
                    et_otp_2.setText("");
                    et_otp_3.setText("");
                    et_otp_4.setText("");

                    et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                    et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                    et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));
                    et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_err));

                    et_otp_1.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                            et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });

                    et_otp_2.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                            et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });

                    et_otp_3.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                            et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });

                    et_otp_4.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                            et_otp_1.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_2.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_3.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                            et_otp_4.setBackground(ContextCompat.getDrawable(VerificationOTPActivity.this, R.drawable.bg_background_edittext_otp));
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });

                    et_otp_1.setKeyListener(new EditText(this).getKeyListener());
                    et_otp_2.setKeyListener(new EditText(this).getKeyListener());
                    et_otp_3.setKeyListener(new EditText(this).getKeyListener());
                    et_otp_4.setKeyListener(new EditText(this).getKeyListener());

                    et_otp_1.requestFocus();
                }, 1000);
            }
        });
    }

    // private methods
    private void setupOTPField(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1 && next != null) {
                    next.requestFocus();
                } else if(s.length() > 1) {
                    int lastChar = s.length() - 1;
                    current.setText(String.valueOf(s.charAt(lastChar)));
                    current.setSelection(current.getText().length());
                } else if(s.length() == 1 && next == null){
                    current.clearFocus();
                    btn_verify.performClick();
                    et_otp_1.setKeyListener(null);
                    et_otp_2.setKeyListener(null);
                    et_otp_3.setKeyListener(null);
                    et_otp_4.setKeyListener(null);
                }
            }
        });
    }

    private void removeOTPField(EditText current, EditText prev) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0 && prev != null) {
                    prev.requestFocus();
                }
            }
        });
    }

    private void otpCodeCounter(long timer) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long remaining = timer - System.currentTimeMillis();

                if(remaining > 0) {
                    int secs = (int) (remaining / 1000);
                    secs = secs % 60;

                    txt_code_counter.setText(" - 00:" + String.format("%02d", secs));

                    handler.postDelayed(this, 1000);

                    if(secs <= 0)  {
                        int verificationCode = new Random().nextInt(9000) + 1000;

                        txt_code_counter.setText(null);

                        txt_resend_code.setTextColor(ContextCompat.getColor(VerificationOTPActivity.this, R.color.purple));
                        txt_resend_code.setOnClickListener(v -> {
                            PrefsHelper prefsHelper = new PrefsHelper(VerificationOTPActivity.this);

                            prefsHelper.setLong("otp_expiry_key", System.currentTimeMillis() + 59000);
                            prefsHelper.setInt("otp_code_key", verificationCode);

                            long expiry = prefsHelper.getLong("otp_expiry_key", 0);
                            int code = prefsHelper.getInt("otp_code_key", 0);

                            Log.e(TAG, "OTP code: " + code);

                            int relayCodeMillis = new Random().nextInt(6500) + (2000 * 2);

                            NotificationHelper otpNotification = new NotificationHelper(VerificationOTPActivity.this);

                            new Handler().postDelayed(() -> {
                                otpNotification.showNotification("ByteQuest", "Your OTP is " + verificationCode  + ", do not share this code to anyone.", 1);
                            }, relayCodeMillis);

                            txt_resend_code.setTextColor(ContextCompat.getColor(VerificationOTPActivity.this, R.color.lightGray));
                            otpCodeCounter(expiry);
                        });
                    }
                }
            }
        };
        handler.post(runnable);
    }

    private boolean checkCode(String input, String otp) {
        return input.equals(otp);
    }

    private String hideUserAccount(String email) {
        int charLength = email.length();

        int removeLength = email.length() >= 13 && email.length() <= 17 ? 1 : (email.length() >= 18 && email.length() <= 19 ? 2 : (email.length() >= 20 && email.length() <= 22 ? 4 : 5));

        Log.e(TAG, "Remove length: " + removeLength);

        StringBuilder transformed = new StringBuilder();
        for(int i = 0; i < charLength; i++) {
            if(i <= email.indexOf("@") - removeLength) {
                transformed.append(String.valueOf(email.charAt(i)));
            } else if(i >= charLength - 4) {
                transformed.append(String.valueOf(email.charAt(i)));
            } else {
                transformed.append("*");
            }
        }
        return String.valueOf(transformed);
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