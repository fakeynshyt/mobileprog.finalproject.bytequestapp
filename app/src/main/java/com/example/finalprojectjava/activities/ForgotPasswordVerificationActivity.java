package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.helper.NotificationHelper;
import com.example.finalprojectjava.helper.PrefsHelper;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

import java.util.Random;

public class ForgotPasswordVerificationActivity extends AppCompatActivity {

    Button send_btn;
    EditText input_email;
    TextView txt_prompt ,txt_resend_code, txt_counter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_verification_activity);

        // Assign widgets from XML
        send_btn = findViewById(R.id.sendBtn);
        input_email = findViewById(R.id.emailInput);

        txt_prompt = findViewById(R.id.promptTxt);
        txt_resend_code = findViewById(R.id.resendTxt);
        txt_counter = findViewById(R.id.counterTxt);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
            String email = input_email.getText().toString();

            // Checks if email is empty
            if(email.isEmpty()) {
                input_email.setError("Email cannot be empty");
                return;
            }

            // Checks if email syntax matches with the specified domain
            if(!email.endsWith("@bytequest.com")) {
                input_email.setError("Email must contain @bytequest.com");
                return;
            }

            // Returns user from database if email input matches
            DatabaseHelper db = new DatabaseHelper(this);
            User userExists = db.getUserByEmail(email);

            // Generates random number for otp code
            int verificationCode = new Random().nextInt(9000) + 1000;
            Log.e(TAG, "OTP code: " + verificationCode);

            // Checks if user exists in database
            if (userExists != null) {
                UserManager.getInstance().setCurrentUser(userExists);

                Toast.makeText(this, "User found!",Toast.LENGTH_SHORT).show();

                // Sends otp notification
                NotificationHelper otpNotification = new NotificationHelper(this);
                int relayCodeMillis = new Random().nextInt(6500) + (2000 * 2);

                // handles the sending of notification
                new Handler().postDelayed(() -> {
                    otpNotification.showNotification("ByteQuest", "Your OTP is " + verificationCode  + ", do not share this code to anyone.", 1);
                }, relayCodeMillis);

                // handles the navigation to forgot password otp activity
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ForgotPasswordVerificationActivity.this, ForgotPasswordOTPActivity.class));
                    finish();
                }, 1500);

                // Sets 59 seconds for otp code to resend
                prefsHelper.setLong("otp_expiry_key", System.currentTimeMillis() + 59000);

                // Sets otp code for later checking
                prefsHelper.setInt("otp_code_key", verificationCode);

                // Gets otp code expiry
                long codeExpiry = prefsHelper.getLong("otp_expiry_key", 0);

                // Starts counter
                otpCodeCounter(codeExpiry);

            } else {
                Toast.makeText(this, "User not found!",Toast.LENGTH_SHORT).show();
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

                    if(secs <= 0) {
                        txt_prompt.setText(null);
                        txt_counter.setText(null);
                        txt_resend_code.setText(null);
                        send_btn.setEnabled(true);
                    }
                }
            }
        };
        handler.post(runnable);
    }
}