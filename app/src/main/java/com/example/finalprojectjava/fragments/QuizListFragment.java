package com.example.finalprojectjava.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.QuizDAO;
import com.example.finalprojectjava.managers.QuizManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.QuizProgress;
import com.example.finalprojectjava.styles.QuizAdapter;
import com.example.finalprojectjava.styles.QuizItem;

import java.util.ArrayList;
import java.util.List;

public class QuizListFragment extends Fragment {

    private View fragmentView;
    private RecyclerView recyclerView;
    private QuizAdapter adapter;

    private int userId;
    private int userLevel;
    private int subjectId;
    private String subjectName;

    private LinearLayout goBackClick;

    private List<QuizItem> quizItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        goBackClick = fragmentView.findViewById(R.id.goBackClick);
        recyclerView = fragmentView.findViewById(R.id.quizRecyclerView);

        userId = UserManager.getInstance().getCurrentUser().getUser_id();
        userLevel = UserManager.getInstance().getCurrentUser().getLevel();

        if (getArguments() != null) {
            subjectId = getArguments().getInt("subjectId", 0);
            subjectName = getArguments().getString("subjectName", "");
        }

        loadQuiz();
        setupRecyclerView();

        goBackClick.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );

            transaction.replace(R.id.fragmentContainerView, new SubjectListFragment());
            transaction.addToBackStack(null);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshQuiz();
    }

    private void loadQuiz() {
        List<QuizProgress> filtered =
                QuizManager.getInstance().getQuizListBySubId(subjectId);

        quizItems = convertToQuizItems(filtered);
    }

    private void refreshQuiz() {

        QuizDAO quizDAO = new QuizDAO(requireContext());

        List<QuizProgress> updated =
                quizDAO.getQuizProgressListByUser(userId);

        QuizManager.getInstance().setQuizList(updated);

        List<QuizProgress> filtered =
                QuizManager.getInstance().getQuizListBySubId(subjectId);

        quizItems = convertToQuizItems(filtered);

        if (adapter != null) {
            adapter.updateList(quizItems);
        }

        Log.d("QUIZ_REFRESH", "Quiz list refreshed from DB");
    }

    private List<QuizItem> convertToQuizItems(List<QuizProgress> list) {

        List<QuizItem> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            QuizProgress q = list.get(i);

            items.add(new QuizItem(
                    q.getQuizId(),
                    i + 1,
                    q.getQuizQuestion(),
                    subjectName,
                    q.getQuizLevelReq(),
                    q.isQuizAnswered()
            ));
        }

        return items;
    }

    private void setupRecyclerView() {

        adapter = new QuizAdapter(
                requireContext(),
                quizItems,
                userLevel,
                (item, position) -> {

                    Log.d("QUIZ_PASS",
                            "Clicked -> ID: " + item.getItemId() +
                                    ", Level: " + item.getItemNumber() +
                                    ", Subject: " + subjectName);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                    transaction.setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );

                    QuizAttemptFragment nextFragment = new QuizAttemptFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("subjectTitle", subjectName);
                    bundle.putInt("currentLevel", item.getItemNumber());
                    bundle.putInt("totalQuiz", quizItems.size());
                    bundle.putInt("quizId", item.getItemId());

                    nextFragment.setArguments(bundle);

                    transaction.replace(R.id.fragmentContainerView, nextFragment);
                    transaction.addToBackStack(null);
                    transaction.setReorderingAllowed(true);
                    transaction.commit();
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }
}