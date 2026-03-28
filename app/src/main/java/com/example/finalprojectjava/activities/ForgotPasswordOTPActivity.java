package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.example.finalprojectjava.helper.NotificationHelper;
import com.example.finalprojectjava.manager.UserManager;

import java.util.Random;

public class ForgotPasswordOTPActivity extends AppCompatActivity {

    EditText et_otp_1, et_otp_2, et_otp_3, et_otp_4;
    Button btn_verify;
    TextView txt_display_user, txt_resend_code, txt_code_counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_otpactivity);

        et_otp_1 = findViewById(R.id.etOTP1);
        et_otp_2 = findViewById(R.id.etOTP2);
        et_otp_3 = findViewById(R.id.etOTP3);
        et_otp_4 = findViewById(R.id.etOTP4);

        btn_verify = findViewById(R.id.sendBtn);

        txt_display_user = findViewById(R.id.txtDisplayUser);
        txt_resend_code = findViewById(R.id.txtResend);
        txt_code_counter = findViewById(R.id.txtResendCounter);

        txt_display_user.setText(UserManager.getInstance().getCurrentUser().getUser_email());
        et_otp_1.requestFocus();

        setupOTPField(et_otp_1, et_otp_2);
        setupOTPField(et_otp_2, et_otp_3);
        setupOTPField(et_otp_3, et_otp_4);

        removeOTPField(et_otp_4, et_otp_3);
        removeOTPField(et_otp_3, et_otp_2);
        removeOTPField(et_otp_2, et_otp_1);

        SharedPreferences globalPrefs = getSharedPreferences("global_session", MODE_PRIVATE);

        long expiry = globalPrefs.getLong("otp_expiry_key", 0);

        otpCodeCounter(expiry);

        btn_verify.setOnClickListener(v -> {
            int code = globalPrefs.getInt("otp_code_key", 0);
            String otpField = et_otp_1.getText().toString()
                    + et_otp_2.getText().toString()
                    + et_otp_3.getText().toString()
                    + et_otp_4.getText().toString();

            if(checkCode(otpField, String.valueOf(code))) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ForgotPasswordOTPActivity.this, ForgotPasswordEditActivity.class));
                }, 3000);
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupOTPField(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1 && next != null) {
                    next.requestFocus();
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

                        txt_resend_code.setTextColor(ContextCompat.getColor(ForgotPasswordOTPActivity.this, R.color.purple));
                        txt_resend_code.setOnClickListener(v -> {
                            SharedPreferences globalPrefs = getSharedPreferences("global_session", MODE_PRIVATE);

                            globalPrefs.edit()
                                    .putLong("otp_expiry_key", System.currentTimeMillis() + 59000)
                                    .apply();

                            globalPrefs.edit()
                                    .putInt("otp_code_key", verificationCode)
                                    .apply();

                            long expiry = globalPrefs.getLong("otp_expiry_key", 0);
                            int code = globalPrefs.getInt("otp_code_key", 0);

                            otpCodeCounter(expiry);
                            Log.e(TAG, "OTP code: " + code);

                            NotificationHelper otpNotification = new NotificationHelper(ForgotPasswordOTPActivity.this);
                            otpNotification.showNotification("ByteQuest", "Your OTP is " + verificationCode  + ", do not share this code to anyone.", 1);
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
}