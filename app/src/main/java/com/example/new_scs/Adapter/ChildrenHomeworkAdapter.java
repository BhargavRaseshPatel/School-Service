package com.example.new_scs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Data.ChildrenHomeworkData;
import com.example.new_scs.R;

import java.util.ArrayList;

public class ChildrenHomeworkAdapter extends RecyclerView.Adapter<ChildrenHomeworkAdapter.viewHolder> {
    ArrayList<ChildrenHomeworkData> list;
    Context context;

    public ChildrenHomeworkAdapter(ArrayList<ChildrenHomeworkData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_children_homework, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ChildrenHomeworkData model = list.get(position);
        holder.tvSubject.setText(model.getSubject());
        holder.tvDate.setText(model.getDate());
        holder.tvDescription.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView tvSubject, tvDate, tvDescription;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.textView71);
            tvSubject = itemView.findViewById(R.id.textView69);
            tvDescription = itemView.findViewById(R.id.textView72);
        }
    }

}
