package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
        setContentView(R.layout.activity_verification_otp);

        // Assign widgets
        et_otp_1 = findViewById(R.id.otp1ET);
        et_otp_2 = findViewById(R.id.otp2ET);
        et_otp_3 = findViewById(R.id.otp3ET);
        et_otp_4 = findViewById(R.id.otp4ET);

        btn_verify = findViewById(R.id.verifyBtn);

        txt_display_user = findViewById(R.id.displayUserTxt);
        txt_resend_code = findViewById(R.id.resendTxt);
        txt_code_counter = findViewById(R.id.counterTxt);

        String hideUserAccount = hideUserAccount(UserManager.getInstance().getCurrentUser().getUser_email());
        txt_display_user.setText(hideUserAccount);

        et_otp_1.requestFocus();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupOTPFields(et_otp_1, et_otp_2, et_otp_3, et_otp_4);
        removeOTPFields(et_otp_1, et_otp_2, et_otp_3, et_otp_4);

        PrefsHelper prefsHelper = new PrefsHelper(this);
        long expiry = prefsHelper.getLong("otp_expiry_key", 0);
        otpCodeCounter(expiry);

        btn_verify.setOnClickListener(v -> {
            setButtonEnabled(false);
            hideKeyboard(et_otp_1, et_otp_2, et_otp_3, et_otp_4);
            setDefaultFields(et_otp_1, et_otp_2, et_otp_3, et_otp_4);

            new Handler().postDelayed(() -> {
                verifyOTP(true);
                setButtonEnabled(true);
            }, 100);
        });
    }

    private void verifyOTP(boolean checkEmpty) {
        PrefsHelper prefsHelper = new PrefsHelper(this);

        if (checkEmpty) {
            if(et_otp_1.getText().toString().isEmpty()
                    || et_otp_2.getText().toString().isEmpty()
                    || et_otp_3.getText().toString().isEmpty()
                    || et_otp_4.getText().toString().isEmpty()) {

                SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "OTP code cannot be empty");

                if(et_otp_1.getText().toString().isEmpty()) et_otp_1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_otp_2.getText().toString().isEmpty()) et_otp_2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_otp_3.getText().toString().isEmpty()) et_otp_3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
                if(et_otp_4.getText().toString().isEmpty()) et_otp_4.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));

                addTextListeners(et_otp_1, et_otp_2, et_otp_3, et_otp_4);
                return;
            }
        }

        int code = prefsHelper.getInt("otp_code_key", 0);

        String otpField = et_otp_1.getText().toString()
                + et_otp_2.getText().toString()
                + et_otp_3.getText().toString()
                + et_otp_4.getText().toString();

        if(checkCode(otpField, String.valueOf(code))) {
            new Handler().postDelayed(() -> {
                SnackBarHelper.showSuccessSnackBar(findViewById(R.id.main), "OTP code successfully verified!");
            }, 2500);

            new Handler().postDelayed(() -> {
                SnackBarHelper.showInfoSnackBar(findViewById(R.id.main), "Redirecting to reset password...");
            }, 4500);

            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, ResetPasswordActivity.class));
                finish();
            }, 6500);
        } else {
            SnackBarHelper.showErrorSnackBar(findViewById(R.id.main), "Invalid OTP code");

            EditText[] editTexts = {et_otp_1, et_otp_2, et_otp_3, et_otp_4};

            setTextToNull(editTexts);
            setErrorFields(editTexts);
            addTextListeners(editTexts);
            setEditTextsEnabled(true ,editTexts);
            setButtonEnabled(true);
            et_otp_1.requestFocus();
        }
    }

    private void setupOTPFields(EditText... editTexts) {
        for(int i = 0; i < editTexts.length; i++) {
            EditText current = editTexts[i];
            EditText next = (i < editTexts.length - 1) ? editTexts[i + 1] : null;

            current.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if(next == null && s.length() == 1) {
                        current.clearFocus();
                        setEditTextsEnabled(false ,editTexts);
                        new Handler().postDelayed(() -> {
                            verifyOTP(false);
                        }, 1000);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 1 && next != null) {
                        current.setSelection(current.getText().length());
                    } else if(s.length() > 1 && next != null) {
                        int lastChar = s.length() - 1;
                        current.setText(String.valueOf(s.charAt(0)));
                        next.requestFocus();
                        next.setText(String.valueOf(s.charAt(lastChar)));
                    }
                }
            });
        }
    }

    private void removeOTPFields(EditText... editTexts) {
        for(int i = 0; i < editTexts.length; i++) {
            EditText current = editTexts[i];
            EditText prev = (i > 0) ? editTexts[i - 1] : null;

            current.addTextChangedListener(new TextWatcher() {
                @Override public void afterTextChanged(Editable s) {}
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 0 && prev != null) {
                        prev.requestFocus();
                    }
                }
            });
        }
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
        int lengthTo = 1;
        for(int i = 0; i < charLength; i++) {
            if(i <= email.indexOf("@")) {
                if(i > 4) lengthTo++;
            }
        }
        Log.e(TAG, "lengthTo: " + lengthTo);

        StringBuilder transformed = new StringBuilder();
        for(int i = 0; i < charLength; i++) {
            if(i <= email.indexOf("@") - lengthTo) {
                transformed.append(email.charAt(i));
            } else if(i >= charLength - 4) {
                transformed.append(email.charAt(i));
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

    private void addTextListeners(EditText... editTexts) {
        for(EditText edit : editTexts) {
            edit.addTextChangedListener(new TextWatcher() {
                @Override public void afterTextChanged(Editable s) {
                    setDefaultFields(et_otp_1, et_otp_2, et_otp_3, et_otp_4);
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }

    private void setErrorFields(EditText... editTexts) {
        for(EditText edit : editTexts) {
            edit.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_err));
        }
    }

    private void setDefaultFields(EditText... editTexts) {
        for(EditText edit : editTexts) {
            edit.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_background_edittext_otp));
        }
    }

    private void hideKeyboard(EditText... editTexts) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        for(EditText edit : editTexts) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }

    private void setEditTextsEnabled(boolean status, EditText... editTexts) {
        for(EditText edit : editTexts) {
            edit.setEnabled(status);
        }
    }

    private void setTextToNull(EditText... editTexts) {
        for(EditText edit : editTexts) {
            edit.setText(null);
        }
    }

    private void setButtonEnabled(boolean status) {
        if(status) {
            btn_verify.setEnabled(true);
            btn_verify.setTextColor(Color.parseColor("#FFFFFFFF"));
            btn_verify.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6725C7")));
        } else {
            btn_verify.setEnabled(false);
            btn_verify.setTextColor(Color.parseColor("#BEB2C8"));
            btn_verify.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69499E")));
        }
    }
}