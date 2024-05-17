package com.example.new_scs.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Class_child extends RecyclerView.Adapter<Class_child.viewHolder> {
    ArrayList<String> arrayList;

    public Class_child(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_principal_class_section, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Class_child.viewHolder holder, int position) {

        holder.textView.setText(String.valueOf(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView22);
        }
    }
}
