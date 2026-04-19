package com.example.finalprojectjava.styles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

import java.util.List;

public class OverviewProgressAdapter extends RecyclerView.Adapter<OverviewProgressHolder> {

    Context context;
    List<OverviewProgressItem> overviewProgressItems;

    public OverviewProgressAdapter(Context context, List<OverviewProgressItem> overviewProgressItems) {
        this.context = context;
        this.overviewProgressItems = overviewProgressItems;
    }

    @NonNull
    @Override
    public OverviewProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OverviewProgressHolder(LayoutInflater.from(context).inflate(R.layout.view_overview_progress_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewProgressHolder holder, int position) {
        holder.itemTitle.setText(overviewProgressItems.get(position).getItemTitle());
        holder.itemProgress.setText(overviewProgressItems.get(position).getItemProgress());
        holder.progressValue.setProgress(overviewProgressItems.get(position).getProgressValue());
        holder.itemImage.setImageResource(overviewProgressItems.get(position).getItemImage());
    }

    @Override
    public int getItemCount() {
        return overviewProgressItems.size();
    }
}
