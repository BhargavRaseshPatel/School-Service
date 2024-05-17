package com.example.new_scs.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;

import java.util.ArrayList;

public class ChildrenAttendanceAdapter extends RecyclerView.Adapter<ChildrenAttendanceAdapter.ViewHolder> {
    Context context;
    ArrayList<String> date ;

    public ChildrenAttendanceAdapter(Context context, ArrayList<String> date) {
        this.context = context;
        this.date = date;
    }

    @NonNull
    @Override
    public ChildrenAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_attendance, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull ChildrenAttendanceAdapter.ViewHolder holder, int position) {
        holder.textViewDate.setText(date.get(position).substring(1));
        if(date.get(position).charAt(0)=='1'){
            holder.radioButtonPresent.setChecked(true);
            holder.radioButtonPresent.setTextColor(Color.rgb(22,185,27));
//            holder.radioButtonPresent.setOutlineAmbientShadowColor(Color.rgb(22,185,27));
        }else {
            holder.radioButtonAbsent.setChecked(true);
            holder.radioButtonAbsent.setTextColor(Color.rgb(238,16,16));
//            holder.radioButtonAbsent.setOutlineSpotShadowColor(Color.rgb(238,16,16));
        }
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        RadioButton radioButtonPresent, radioButtonAbsent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.name);
            radioButtonPresent = itemView.findViewById(R.id.radioButton);
            radioButtonAbsent = itemView.findViewById(R.id.radioButton2);
            radioButtonAbsent.setEnabled(false);
            radioButtonPresent.setEnabled(false);
        }
    }
}
