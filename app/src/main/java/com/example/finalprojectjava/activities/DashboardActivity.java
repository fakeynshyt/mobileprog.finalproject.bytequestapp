package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.QuizDAO;
import com.example.finalprojectjava.fragments.DashboardFragment;
import com.example.finalprojectjava.fragments.FunGamesFragment;
import com.example.finalprojectjava.fragments.LessonFragment;
import com.example.finalprojectjava.fragments.QuizAttemptFragment;
import com.example.finalprojectjava.fragments.QuizFragment;
import com.example.finalprojectjava.helpers.SnackBarHelperFragment;
import com.example.finalprojectjava.managers.QuizManager;
import com.example.finalprojectjava.managers.SessionManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.QuizProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private int currentIdIndex = R.id.item1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        FragmentManager fragmentManager = getSupportFragmentManager();

        initializeQuiz();

        BottomNavigationView bottomNavigationView = findViewById(R.id.customBottomNavigation);
        bottomNavigationView.setItemBackgroundResource(R.color.transparent);

        fragmentManager.addOnBackStackChangedListener(() -> {
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerView);
            View mainBg = findViewById(R.id.main);

            if (currentFragment instanceof QuizAttemptFragment) {
                bottomNavigationView.setEnabled(false);
                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    bottomNavigationView.getMenu().getItem(i).setEnabled(false);
                }
                mainBg.setBackground(getResources().getDrawable(R.drawable.img_bg_quiz_attempt));
            } else {
                bottomNavigationView.setEnabled(true);
                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    bottomNavigationView.getMenu().getItem(i).setEnabled(true);
                }
                mainBg.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int nextIdIndex = item.getItemId();
            if (nextIdIndex == currentIdIndex) return true;

            int animEnter, animExit;
            if (currentIdIndex < nextIdIndex) {
                animEnter = R.anim.slide_in_right;
                animExit = R.anim.slide_out_left;
            } else {
                animEnter = R.anim.slide_in_left;
                animExit = R.anim.slide_out_right;
            }

            if (nextIdIndex == R.id.item1) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(animEnter, animExit)
                        .replace(R.id.fragmentContainerView, DashboardFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("dashboard")
                        .commit();

                SnackBarHelperFragment.showInfoSnackBar(findViewById(R.id.main), "You clicked dashboard");
            } else if (nextIdIndex == R.id.item2) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(animEnter, animExit)
                        .replace(R.id.fragmentContainerView, LessonFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("lesson")
                        .commit();
            } else if (nextIdIndex == R.id.item3) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(animEnter, animExit)
                        .replace(R.id.fragmentContainerView, QuizFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("quiz")
                        .commit();
            } else if (nextIdIndex == R.id.item4) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(animEnter, animExit)
                        .replace(R.id.fragmentContainerView, FunGamesFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("funGames")
                        .commit();
            }

            currentIdIndex = nextIdIndex;
            return true;
        });

        Log.e(TAG, "USER_USERNAME: " + UserManager.getInstance().getCurrentUser().getUsername());
        Log.e(TAG, "USER_ID: " + UserManager.getInstance().getCurrentUser().getUser_id());

        SessionManager session = new SessionManager(this, UserManager.getInstance().getCurrentUser().getUser_email());

        if (session.isNewUser()) {
            startActivity(new Intent(this, EditProfileActivity.class));
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customBottomNavigation), (v, insets) -> {
            v.setPadding(0, 0, 0, 0);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeQuiz() {
        QuizDAO quizDAO = new QuizDAO(this);
        List<QuizProgress> quizProgressList =
                quizDAO.getQuizProgressListByUser(UserManager.getInstance().getCurrentUser().getUser_id());

        QuizManager.getInstance().setQuizList(quizProgressList);

        Log.d("QUIZ_TEST", "TOTAL QUIZZES: " + quizProgressList.size());
    }
}