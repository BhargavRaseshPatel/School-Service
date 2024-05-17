package com.example.new_scs.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class FirestoreAttendanceAdapter extends FirestoreRecyclerAdapter<String,FirestoreAttendanceAdapter.ViewHolder> {

    ArrayList<String> absentList = new ArrayList<>();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirestoreAttendanceAdapter(@NonNull FirestoreRecyclerOptions<String> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_attendance , parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull String model) {
        String value = model;
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

}
