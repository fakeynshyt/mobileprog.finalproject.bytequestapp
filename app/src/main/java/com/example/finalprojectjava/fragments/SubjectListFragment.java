package com.example.finalprojectjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.SubjectDAO;
import com.example.finalprojectjava.models.Subject;
import com.example.finalprojectjava.styles.SubjectAdapter;
import com.example.finalprojectjava.styles.SubjectItem;

import java.util.ArrayList;
import java.util.List;

public class SubjectListFragment extends Fragment {

    View fragmentSubjectListView;
    LinearLayout goBackClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSubjectListView = inflater.inflate(R.layout.fragment_subject_list, container, false);

        goBackClick = fragmentSubjectListView.findViewById(R.id.goBackClick);

        SubjectDAO subjectDAO = new SubjectDAO(fragmentSubjectListView.getContext());
        List<Subject> subjects = subjectDAO.getAllSubjects();

        int iconSubjects[] = {
              R.drawable.ic_computing,
              R.drawable.ic_java,
              R.drawable.ic_csharp,
              R.drawable.ic_mobile_phone,
              R.drawable.ic_web,
              R.drawable.ic_database,
              R.drawable.ic_mathematics
        };
        List<SubjectItem> subjectItems = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            Subject s = subjects.get(i);

            int imageResId = iconSubjects[i % iconSubjects.length];

            subjectItems.add(new SubjectItem(
                    s.getSubject_name(),
                    s.getSubject_description(),
                    imageResId
            ));
        }
        RecyclerView subjectRecyclerView = fragmentSubjectListView.findViewById(R.id.subjectRecyclerView);

        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        SubjectAdapter adapter = new SubjectAdapter(fragmentSubjectListView.getContext(), subjectItems, (subjectItem, position) -> {
            Toast.makeText(fragmentSubjectListView.getContext(), "Clicked: " + subjectItem.getSubjectTitle(), Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putInt("subjectId", position + 1);
            bundle.putString("subjectName", subjectItem.getSubjectTitle());

            Fragment nextFragment = new QuizListFragment();
            nextFragment.setArguments(bundle);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );

            transaction.replace(R.id.fragmentContainerView, nextFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        subjectRecyclerView.setAdapter(adapter);

        goBackClick.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );

            transaction.replace(R.id.fragmentContainerView, new QuizFragment());
            transaction.addToBackStack(null);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        });

        return fragmentSubjectListView;
    }
}