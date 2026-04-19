package com.example.finalprojectjava.styles;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

public class OverviewProgressHolder extends RecyclerView.ViewHolder {

    ImageView itemImage;
    TextView itemTitle, itemProgress;
    ProgressBar progressValue;

    public OverviewProgressHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        itemProgress = itemView.findViewById(R.id.itemProgress);
        progressValue = itemView.findViewById(R.id.progressValue);

        String input = itemProgress.getText().toString();
        String numberOnly = input.replaceAll("[^0-9]", "");
        int value = Integer.parseInt(numberOnly);

        progressValue.setProgress(value);
        progressValue.setMax(100);

    }
}
