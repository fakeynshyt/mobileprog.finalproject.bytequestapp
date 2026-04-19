package com.example.finalprojectjava.styles;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectjava.R;

public class QuizHolder extends RecyclerView.ViewHolder {
    TextView itemNumber, itemQuestion, itemTitle, itemReqLevel;
    LinearLayout itemContainer, itemNumberContainer;
    public QuizHolder(@NonNull View itemView) {
        super(itemView);

        itemNumber = itemView.findViewById(R.id.itemNumber);
        itemQuestion = itemView.findViewById(R.id.itemQuestion);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        itemReqLevel = itemView.findViewById(R.id.itemReqLevel);

        itemContainer = itemView.findViewById(R.id.itemContainer);
        itemNumberContainer = itemView.findViewById(R.id.itemNumberContainer);
    }
}
