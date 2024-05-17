package com.example.new_scs.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Model.Homework;
import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TeacherTodayHomework extends FirestoreRecyclerAdapter<Homework, TeacherTodayHomework.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeacherTodayHomework(@NonNull FirestoreRecyclerOptions<Homework> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Homework model) {
        holder.textViewSubject.setText(model.getSubject());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewClass.setText("Class-"+model.getStd_class()+", Section-"+model.getSection());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_children_homework,parent,false);
        return new ViewHolder(view);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClass, textViewSubject, textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClass = itemView.findViewById(R.id.textView71);
            textViewSubject = itemView.findViewById(R.id.textView69);
            textViewDescription = itemView.findViewById(R.id.textView72);
        }
    }
}
