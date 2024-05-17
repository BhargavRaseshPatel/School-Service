package com.example.new_scs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    SectionAdapter sectionAdapter;
    ArrayList<String> arrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> arrayList1 = new ArrayList<>();
    String id;
    private final Activity activity;

    public ClassAdapter(Activity activity, ArrayList<String> arrayList, String id) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.id = id;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_principal_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassAdapter.ViewHolder holder, int position) {
//        save_class saveClass = arrayList.get(position);
        holder.textView.setText("Class : " + arrayList.get(position));

//        arrayList1.add("ABC");
//        arrayList1.add("XYZ");
        Log.e("Class ", arrayList.get(position));

        try {
            addSection(arrayList.get(position));

//            db.collection("Principal").document("t88JRSYFluoYCnlKUF4f").collection("class").
//                    document(arrayList.get(position)).collection("section").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                    for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//                        String a = queryDocumentSnapshot.getId();
//                        arrayList1.add(a);
//                        Log.e("Section ",a);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull @NotNull Exception e) {
//                    Log.e("Class_Adapter",e.getMessage());
//                }
//            });
//            Thread.sleep(1000);


//            Log.e("Class Adapter",arrayList.get(position));
////            Thread.sleep(1000);
//            Log.e("Section ",arrayList1.toString());
//              SectionAdapterListView sectionAdapter = new SectionAdapterListView(activity,arrayList1);
//              holder.recyclerView.setAdapter(sectionAdapter);
            sectionAdapter = new SectionAdapter(arrayList1);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
            holder.recyclerView.setAdapter(sectionAdapter);

        } catch (Exception e) {
            Log.e("Main_Page", e.getMessage());
        }
    }

    private void addSection(String s) {
        db.collection("Principal").document(id).collection("class").
                document(s).collection("section").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("ClassAdapter", error.getMessage());
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        arrayList1.add(dc.getDocument().getId());
                        Log.e("Section ", dc.getDocument().getId());
                    }
                    sectionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SectionAdapterListView extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> section;

        public SectionAdapterListView(@NonNull Context context, ArrayList<String> section) {
            super(context, R.layout.sample_principal_class_section, section);
            this.context = context;
            this.section = section;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_principal_class_section, parent, false);
            TextView section = view.findViewById(R.id.textView22);
            section.setText((getItem(position)));
            return view;
        }
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
