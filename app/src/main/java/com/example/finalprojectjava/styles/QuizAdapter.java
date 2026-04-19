package com.example.finalprojectjava.styles;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizHolder> {

    Context context;
    List<QuizItem> quizItems;
    int userLevel;
    private OnQuizItemClickListener onQuizItemClickListener;

    public QuizAdapter(Context context, List<QuizItem> quizItems, int userLevel, OnQuizItemClickListener listener) {
        this.context = context;
        this.quizItems = quizItems;
        this.userLevel = userLevel;
        this.onQuizItemClickListener = listener;
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_quiz_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {

        QuizItem item = quizItems.get(position);

        holder.itemNumber.setText(String.valueOf(position + 1));
        holder.itemQuestion.setText(item.getItemQuestion());
        holder.itemTitle.setText(item.getItemTitle());
        holder.itemReqLevel.setText(getDifficultyName(item.getItemReqLevel()));

        if (item.isItemAnswered()) {
            holder.itemContainer.setBackgroundColor(Color.parseColor("#a9e9ca"));
            holder.itemNumberContainer.setBackgroundColor(Color.parseColor("#25c7a5"));
        } else if (userLevel < item.getItemReqLevel()) {
            holder.itemContainer.setBackgroundColor(Color.parseColor("#ccc5c5"));
            holder.itemNumberContainer.setBackgroundColor(Color.parseColor("#dacece"));
        } else {
            holder.itemContainer.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.itemNumberContainer.setBackgroundColor(Color.parseColor("#6725C7"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (userLevel < item.getItemReqLevel()) return;

            if (onQuizItemClickListener != null) {
                onQuizItemClickListener.quizItemOnClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizItems.size();
    }

    private String getDifficultyName(int level) {
        if (level == 1) return "Beginner";
        if (level == 5) return "Easy";
        if (level == 10) return "Intermediate";
        if (level == 15) return "Advanced";
        if (level == 20) return "Expert";
        if (level == 25) return "Master";
        return "Unknown";
    }

    public void updateList(List<QuizItem> newList) {
        this.quizItems = newList;
        notifyDataSetChanged();
    }
}
