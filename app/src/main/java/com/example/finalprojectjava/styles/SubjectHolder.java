package com.example.finalprojectjava.styles;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

public class SubjectHolder extends RecyclerView.ViewHolder {

    ImageView itemImage;
    TextView itemTitle, itemDesc;

    public SubjectHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.imageItem);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        itemDesc = itemView.findViewById(R.id.itemDesc);
    }
}
