package com.example.finalprojectjava.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.Subject;

import java.util.List;

public class LessonActivity extends AppCompatActivity {

    Button testBtn;
    TextView testText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson);

        testBtn = findViewById(R.id.testViewSubs);
        testText = findViewById(R.id.testViewText);

        testBtn.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            List<Subject> viewAllSubs = dbHelper.getAllSubjects();

            testText.setText(viewAllSubs.toString());
        });

        Log.e("TAG", UserManager.getInstance().getCurrentUser().getFull_name());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}