package com.example.finalprojectjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.User;

public class QuizFragment extends Fragment {

    TextView display_username_txt;
    LinearLayout view_all_click;
    View fragmentQuizView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentQuizView = inflater.inflate(R.layout.fragment_quiz, container, false);

        User user = UserManager.getInstance().getCurrentUser();

        display_username_txt = fragmentQuizView.findViewById(R.id.displayUsernameTxt);
        view_all_click = fragmentQuizView.findViewById(R.id.viewAllLY);

        display_username_txt.setText(user.getUsername());

        view_all_click.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );

            transaction.replace(R.id.fragmentContainerView, new SubjectListFragment());
            transaction.addToBackStack(null);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        });

        return fragmentQuizView;
    }
}