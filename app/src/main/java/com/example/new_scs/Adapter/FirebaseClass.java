package com.example.new_scs.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Data.DataClassSection;
import com.example.new_scs.Data.SaveClass;
import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class FirebaseClass extends FirestoreRecyclerAdapter<SaveClass, FirebaseClass.ViewHolder> {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseSection firebaseSection;
    Activity activity;

    public FirebaseClass(@NonNull @NotNull FirestoreRecyclerOptions<SaveClass> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FirebaseClass.ViewHolder holder, int position, @NonNull @NotNull SaveClass model) {
        holder.textView.setText("Class : "+model.getStd_class());
        firestoreSection((model.getStd_class()));

        Log.e("class",model.getStd_class());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        holder.recyclerView.setAdapter(firebaseSection);

    }

    private void firestoreSection(String value) {
        try {
            Query query = db.collection("Principal").document("t88JRSYFluoYCnlKUF4f").collection("class").
                    document(value).collection("section");
            FirestoreRecyclerOptions<DataClassSection> options = new FirestoreRecyclerOptions.Builder<DataClassSection>().setQuery(query, DataClassSection.class).build();
            firebaseSection.startListening();
            firebaseSection = new FirebaseSection(options);
//            Thread.sleep(2000);
            firebaseSection.stopListening();
        }
        catch (Exception e){
            Log.e("firebase_class",e.getMessage());
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_principal_class, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView21);
            recyclerView = itemView.findViewById(R.id.listview_section);
        }
    }
}
