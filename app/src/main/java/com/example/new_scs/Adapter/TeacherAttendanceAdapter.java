package com.example.new_scs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

import java.util.ArrayList;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder> {
    Context context;
    ArrayList<String> name;
    ArrayList<String> absentList = new ArrayList<>();

    public TeacherAttendanceAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public TeacherAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_attendance , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAttendanceAdapter.ViewHolder holder, int position) {
        String value = name.get(position);
        holder.textView.setText(value);
        holder.radioButtonAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.radioButtonAbsent.isChecked()){
                    absentList.add(value);
                }
            }
        });
        holder.radioButtonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.radioButtonPresent.isChecked()){
                    absentList.remove(value);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RadioButton radioButtonPresent, radioButtonAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            radioButtonAbsent = itemView.findViewById(R.id.radioButton2);
            radioButtonPresent = itemView.findViewById(R.id.radioButton);
        }
    }
    public ArrayList<String> getAllAbsent(){
        return absentList;
    }

    public void setName(ArrayList<String> name){
        this.name = new ArrayList<>();
        this.name = name;
        notifyDataSetChanged();
    }
}
