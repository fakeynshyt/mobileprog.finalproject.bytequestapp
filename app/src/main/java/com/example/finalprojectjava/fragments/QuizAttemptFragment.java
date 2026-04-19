package com.example.finalprojectjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.QuizDAO;
import com.example.finalprojectjava.dao.UserDAO;
import com.example.finalprojectjava.managers.QuizManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.QuizProgress;

import java.util.List;

public class QuizAttemptFragment extends Fragment {

    private View fragmentQuizAttemptView;

    TextView displaySubjectName, displayCurrentLvl, displayTotalQuizCount;
    ProgressBar lvlProgressBar;

    private String subjectTitle;
    private int currentLevel;
    private int totalQuiz;
    private int quizId;

    private int currentIndex = 0;
    private List<QuizProgress> quizList;
    private String selectedAnswer = null;

    private QuizDAO quizDAO;
    private UserDAO userDAO;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentQuizAttemptView = inflater.inflate(R.layout.fragment_quiz_attempt, container, false);

        displaySubjectName = fragmentQuizAttemptView.findViewById(R.id.displaySubjectName);
        displayCurrentLvl = fragmentQuizAttemptView.findViewById(R.id.displayCurrentLvl);
        displayTotalQuizCount = fragmentQuizAttemptView.findViewById(R.id.displayTotalQuizItems);
        lvlProgressBar = fragmentQuizAttemptView.findViewById(R.id.lvlProgressBar);

        Button nextBtn = fragmentQuizAttemptView.findViewById(R.id.nextQuizBtn);

        quizDAO = new QuizDAO(requireContext());
        userDAO = new UserDAO(requireContext()); // ✅ INIT
        userId = UserManager.getInstance().getCurrentUser().getUser_id();

        if (getArguments() != null) {
            subjectTitle = getArguments().getString("subjectTitle");
            currentLevel = getArguments().getInt("currentLevel");
            totalQuiz = getArguments().getInt("totalQuiz");
            quizId = getArguments().getInt("quizId");
        }

        displaySubjectName.setText(subjectTitle);
        displayCurrentLvl.setText("Question " + currentLevel + "/");
        displayTotalQuizCount.setText(String.valueOf(totalQuiz));

        lvlProgressBar.setMax(totalQuiz);
        lvlProgressBar.setProgress(currentLevel);

        quizList = QuizManager.getInstance()
                .getQuizListBySubId(getSubjectIdFromTitle(subjectTitle));

        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).getQuizId() == quizId) {
                currentIndex = i;
                break;
            }
        }

        loadQuestion();

        nextBtn.setOnClickListener(v -> {

            if (selectedAnswer == null) {
                Log.d("QUIZ", "No answer selected");
                return;
            }

            QuizProgress currentQuiz = quizList.get(currentIndex);

            if (selectedAnswer.equals(currentQuiz.getQuizCorrectAnswer())) {
                Log.d("QUIZ", "Correct!");

                if (!currentQuiz.isQuizAnswered()) {

                    quizDAO.markQuizAnswered(userId, currentQuiz.getQuizId());

                    userDAO.addUserPoints(userId, 5);

                    Log.d("QUIZ_POINTS",
                            "Added +5 → userId=" + userId +
                                    " quizId=" + currentQuiz.getQuizId());

                    currentQuiz.setQuizAnswered(true);

                } else {
                    Log.d("QUIZ", "Already answered, no points given");
                }

                currentIndex++;

                if (currentIndex < quizList.size()) {

                    currentLevel++;
                    lvlProgressBar.setProgress(currentLevel);
                    displayCurrentLvl.setText("Question " + currentLevel + "/");

                    selectedAnswer = null;
                    loadQuestion();

                } else {
                    Log.d("QUIZ", "Quiz Finished!");
                }

            } else {
                Log.d("QUIZ", "Wrong!");
            }
        });

        return fragmentQuizAttemptView;
    }

    private void loadQuestion() {

        QuizProgress quiz = quizList.get(currentIndex);

        QuizContainerFragment fragment = new QuizContainerFragment();

        Bundle bundle = new Bundle();
        bundle.putString("question", quiz.getQuizQuestion());
        bundle.putString("choiceA", quiz.getQuizChoiceA());
        bundle.putString("choiceB", quiz.getQuizChoiceB());
        bundle.putString("choiceC", quiz.getQuizChoiceC());
        bundle.putString("choiceD", quiz.getQuizChoiceD());

        fragment.setArguments(bundle);

        fragment.setOnAnswerSelectedListener(answer -> {
            selectedAnswer = answer;
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
        );
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.commit();
    }

    private int getSubjectIdFromTitle(String title) {
        switch (title) {
            case "Java Programming": return 2;
            case "C# Programming": return 3;
            case "Web Development": return 5;
            case "Database Management": return 6;
            default: return 1;
        }
    }
}