package com.example.finalprojectjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.finalprojectjava.R;

public class QuizContainerFragment extends Fragment {

    private View fragmentQuizContainerView;

    private TextView displayQuizQuestion;
    private RadioButton displayChoiceA, displayChoiceB, displayChoiceC, displayChoiceD;

    private String question, choiceA, choiceB, choiceC, choiceD, correctAnswer;
    private OnAnswerSelectedListener listener;

    public void setOnAnswerSelectedListener(OnAnswerSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentQuizContainerView = inflater.inflate(R.layout.fragment_quiz_container, container, false);

        displayQuizQuestion = fragmentQuizContainerView.findViewById(R.id.quizQuestion);
        displayChoiceA = fragmentQuizContainerView.findViewById(R.id.quizChoiceA);
        displayChoiceB = fragmentQuizContainerView.findViewById(R.id.quizChoiceB);
        displayChoiceC = fragmentQuizContainerView.findViewById(R.id.quizChoiceC);
        displayChoiceD = fragmentQuizContainerView.findViewById(R.id.quizChoiceD);

        if (getArguments() != null) {
            question = getArguments().getString("question");
            choiceA = getArguments().getString("choiceA");
            choiceB = getArguments().getString("choiceB");
            choiceC = getArguments().getString("choiceC");
            choiceD = getArguments().getString("choiceD");
            correctAnswer = getArguments().getString("correctAnswer");
        }

        displayQuizQuestion.setText(question);
        displayChoiceA.setText(choiceA);
        displayChoiceB.setText(choiceB);
        displayChoiceC.setText(choiceC);
        displayChoiceD.setText(choiceD);

        displayChoiceA.setOnClickListener(v -> notifyAnswer("A"));
        displayChoiceB.setOnClickListener(v -> notifyAnswer("B"));
        displayChoiceC.setOnClickListener(v -> notifyAnswer("C"));
        displayChoiceD.setOnClickListener(v -> notifyAnswer("D"));

        return fragmentQuizContainerView;
    }

    private void notifyAnswer(String answer) {
        if (listener != null) {
            listener.onAnswerSelected(answer);
        }
    }
}