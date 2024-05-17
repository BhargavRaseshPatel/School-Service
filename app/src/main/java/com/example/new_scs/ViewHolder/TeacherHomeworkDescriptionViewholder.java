package com.example.new_scs.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

public class TeacherHomeworkDescriptionViewholder extends RecyclerView.ViewHolder {

    public TextView textViewSubject, textViewDescription, textViewClass;

    public TeacherHomeworkDescriptionViewholder(@NonNull View itemView) {
        super(itemView);
        textViewSubject = itemView.findViewById(R.id.textView69);
        textViewDescription = itemView.findViewById(R.id.textView72);
        textViewClass = itemView.findViewById(R.id.textView71);
    }
}
