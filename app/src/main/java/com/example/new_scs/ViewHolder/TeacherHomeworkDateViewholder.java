package com.example.new_scs.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

public class TeacherHomeworkDateViewholder extends RecyclerView.ViewHolder {

    public TextView textView;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager manager;

    public TeacherHomeworkDateViewholder(@NonNull View itemView) {
        super(itemView);

        manager = new LinearLayoutManager(itemView.getContext());
        textView = itemView.findViewById(R.id.textView75);
        recyclerView = itemView.findViewById(R.id.recycleview_homework);
        recyclerView.setLayoutManager(manager);
    }
}
