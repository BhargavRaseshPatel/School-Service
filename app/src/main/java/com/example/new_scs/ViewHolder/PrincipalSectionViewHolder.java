package com.example.new_scs.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

public class PrincipalSectionViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewSection;
    public PrincipalSectionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewSection = itemView.findViewById(R.id.textView22);
    }
}
