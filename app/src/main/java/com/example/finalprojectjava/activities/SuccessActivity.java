package com.example.finalprojectjava.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;

public class SuccessActivity extends AppCompatActivity {

    TextView txt_counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success);

        txt_counter = findViewById(R.id.counterTxt);
        long staticTimer = System.currentTimeMillis() + 6500;

        redirectingToLoginCounter(staticTimer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void redirectingToLoginCounter(long timer) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long remaining = timer - System.currentTimeMillis();

                if(remaining > 0) {
                    int secs = (int) (remaining / 1000);
                    secs = secs % 60;

                    txt_counter.setText("Redirecting to Login in " + String.format("%02d", secs));

                    handler.postDelayed(this, 1000);

                    if(secs <= 0) {
                        startActivity(new Intent(SuccessActivity.this, DashboardActivity.class));
                        finish();
                    }
                }
            }
        };
        handler.post(runnable);
    }
}