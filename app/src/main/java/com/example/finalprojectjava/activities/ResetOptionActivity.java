package com.example.finalprojectjava.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;

public class ResetOptionActivity extends AppCompatActivity {

    ImageView iv_go_back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_options);

        iv_go_back = findViewById(R.id.goBackClickIV);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iv_go_back.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                onBackPressed();
            }, 500);
        });
    }

    // Navigate to verification activity
    public void navigateToVerificationAct(View view) {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(ResetOptionActivity.this, PasswordVerificationActivity.class));
        }, 1000);
    }
}