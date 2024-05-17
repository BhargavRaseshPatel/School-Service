package com.example.new_scs.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Data.DataClassSection;
import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class FirebaseSection extends FirestoreRecyclerAdapter<DataClassSection, FirebaseSection.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseSection(@NonNull   FirestoreRecyclerOptions<DataClassSection> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FirebaseSection.ViewHolder holder, int position, @NonNull @NotNull DataClassSection model) {
        holder.textView.setText("Section : "+model.getStd_section());
        Log.e("section",model.getStd_section());
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_principal_class_section, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView22);
        }
    }
}
