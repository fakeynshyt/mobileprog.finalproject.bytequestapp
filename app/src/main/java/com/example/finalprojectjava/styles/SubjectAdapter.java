package com.example.finalprojectjava.styles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {

    Context context;
    List<SubjectItem> subjectItems;
    private OnSubjectItemClickListener onSubjectItemClickListener;

    public SubjectAdapter(Context context, List<SubjectItem> subjectItems, OnSubjectItemClickListener onSubjectItemClickListener) {
        this.context = context;
        this.subjectItems = subjectItems;
        this.onSubjectItemClickListener = onSubjectItemClickListener;
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectHolder(LayoutInflater.from(context).inflate(R.layout.view_subject_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        holder.itemTitle.setText(subjectItems.get(position).getSubjectTitle());
        holder.itemDesc.setText(subjectItems.get(position).getSubjectDesc());
        holder.itemImage.setImageResource(subjectItems.get(position).getSubjectImage());

        holder.itemView.setOnClickListener(v -> {
            onSubjectItemClickListener.subjectItemOnClick(subjectItems.get(position), position);
        });

        holder.itemView.setAlpha(0f);
        holder.itemView.setTranslationY(20f);

        holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(180)
                .setStartDelay(Math.min(position * 20L, 120L))
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();

    }

    @Override
    public int getItemCount() { return subjectItems.size(); }
}
