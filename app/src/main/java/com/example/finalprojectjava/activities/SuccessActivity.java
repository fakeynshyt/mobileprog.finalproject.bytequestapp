package com.example.finalprojectjava.activities;

import android.annotation.SuppressLint;
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

    TextView txt_title, txt_desc ,txt_where;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success);

        txt_title = findViewById(R.id.titleTxt);
        txt_desc = findViewById(R.id.descTxt);
        txt_where = findViewById(R.id.whereTxt);

        Intent intent = getIntent();
        String key_title = intent.getStringExtra("title_key");
        String key_desc = intent.getStringExtra("description_key");
        String key_where = intent.getStringExtra("where_key");

        txt_title.setText(key_title);
        txt_desc.setText(key_desc);

        long staticTimer = System.currentTimeMillis() + 6900;

        redirectingToLoginCounter(staticTimer, key_where);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void redirectingToLoginCounter(long timer, String where) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long remaining = timer - System.currentTimeMillis();

                if(remaining > 0) {
                    int secs = (int) (remaining / 1000);
                    secs = secs % 60;

                    txt_where.setText("Redirecting to " + where + " in " + String.format("%02d", secs));

                    handler.postDelayed(this, 1000);

                    if(secs <= 0) {
                        startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        handler.post(runnable);
    }
}