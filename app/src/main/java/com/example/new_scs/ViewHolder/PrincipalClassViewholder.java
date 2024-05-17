package com.example.new_scs.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

public class PrincipalClassViewholder extends RecyclerView.ViewHolder{

    public TextView textViewClass;
    public RecyclerView recyclerViewSection;
    public RecyclerView.LayoutManager manager;

    public PrincipalClassViewholder(@NonNull View itemView) {
        super(itemView);

        manager = new GridLayoutManager(itemView.getContext(),2);
        textViewClass = itemView.findViewById(R.id.textView21);
        recyclerViewSection = itemView.findViewById(R.id.listview_section);
        recyclerViewSection.setLayoutManager(manager);
    }
}
