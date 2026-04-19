package com.example.finalprojectjava.fragments;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.UserDAO;
import com.example.finalprojectjava.helpers.SnackBarHelperActivity;
import com.example.finalprojectjava.helpers.SnackBarHelperFragment;
import com.example.finalprojectjava.managers.SessionManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.User;
import com.example.finalprojectjava.styles.OverviewProgressAdapter;
import com.example.finalprojectjava.styles.OverviewProgressItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    TextView display_username_txt, display_user_lvl;
    ProgressBar user_byte_points_progress;
    View fragmentDashboardView;
    int exp = UserManager.getInstance().getCurrentUser().getByte_point();
    User currentUser = UserManager.getInstance().getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        display_username_txt = fragmentDashboardView.findViewById(R.id.displayUsernameTxt);
        display_user_lvl = fragmentDashboardView.findViewById(R.id.displayUserLvlTxt);
        user_byte_points_progress = fragmentDashboardView.findViewById(R.id.userExpProgress);

        user_byte_points_progress.setMax(100);

        String username = currentUser.getUsername();
        String level = "lvl." + currentUser.getLevel();


        Log.e(TAG, "User: " + currentUser.toString());


        display_username_txt.setText(username);
        display_user_lvl.setText(level);

        ObjectAnimator animator = ObjectAnimator.ofInt(user_byte_points_progress, "progress", 0, exp);
        animator.setDuration(1500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.start();


        RecyclerView progressRecyclerView = fragmentDashboardView.findViewById(R.id.progressRecyclerView);

        List<OverviewProgressItem> overviewProgressItems = new ArrayList<>();

        int subjectProgress = 13;
        int moduleProgress = 37;
        int quizProgress = 41;

        // default progress overviews
        overviewProgressItems.add(new OverviewProgressItem("Subjects", subjectProgress + "%", subjectProgress, R.drawable.img_image1));
        overviewProgressItems.add(new OverviewProgressItem("Modules", moduleProgress + "%", moduleProgress, R.drawable.img_image3));
        overviewProgressItems.add(new OverviewProgressItem("Quiz", quizProgress + "%", quizProgress, R.drawable.img_image2));

        progressRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        progressRecyclerView.setAdapter(new OverviewProgressAdapter(this.getContext(), overviewProgressItems));

        SessionManager session = new SessionManager(this.getContext(), currentUser.getUser_email());
        boolean bonusClaimed = session.getBonusClaimed();

        if(!bonusClaimed) {
            showCustomDialog(currentUser);
        }

        return fragmentDashboardView;
    }

    private void showCustomDialog(User user) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_acknoledgement, null);

        TextView rewardText = dialogView.findViewById(R.id.displayRewardTxt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton claimButton = dialogView.findViewById(R.id.claimRewardBtn);

        rewardText.setText("You’ve received 75 Byte points\nfor joining ByteQuest!");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Animation;

        new Handler().postDelayed(() -> {
            alertDialog.show();
        }, 500);

        claimButton.setOnClickListener(v -> {
            int newUserBonus = 150;
            ObjectAnimator animator = ObjectAnimator.ofInt(user_byte_points_progress, "progress", exp, newUserBonus);

            SnackBarHelperFragment.showSuccessSnackBar(getActivity().findViewById(android.R.id.content), "You’ve successfully received the award!");

            SessionManager session = new SessionManager(this.getContext(), UserManager.getInstance().getCurrentUser().getUser_email());
            session.setBonusClaimed(true);

            animator.setDuration(1500);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            user.setByte_point(newUserBonus);

            UserDAO userDAO = new UserDAO(this.getContext());
            userDAO.updateUserProfile(user);

            animator.start();
            alertDialog.dismiss();
        });
    }

}