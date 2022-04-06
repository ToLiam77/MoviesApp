package com.example.movies;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultHolder extends RecyclerView.ViewHolder {
    TextView name;
    CheckBox cb;

    public ResultHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewName);
        cb = itemView.findViewById(R.id.checkBox);
    }
}
