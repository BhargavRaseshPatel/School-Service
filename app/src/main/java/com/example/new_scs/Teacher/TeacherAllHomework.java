package com.example.new_scs.Teacher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Model.DateModel;
import com.example.new_scs.Model.Homework;
import com.example.new_scs.R;
import com.example.new_scs.ViewHolder.TeacherHomeworkDateViewholder;
import com.example.new_scs.ViewHolder.TeacherHomeworkDescriptionViewholder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TeacherAllHomework extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<DateModel, TeacherHomeworkDateViewholder> adapter1;
    FirestoreRecyclerAdapter<Homework, TeacherHomeworkDescriptionViewholder> adapter2;
    RecyclerView.LayoutManager manager;
    ArrayList<String> arrayListDate = new ArrayList<>();
    ArrayList<String> homeworkDate = new ArrayList<>();
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_all_homework);
        recyclerView = findViewById(R.id.recycleview_teacher_all_homework);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        btn = findViewById(R.id.button25);
        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);
        String schoolName = sp.getString("SchoolName", null);
        db = FirebaseFirestore.getInstance();

        db.collection("Homework").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                    arrayListDate.add(qds.getId());
                }
                for (String str : arrayListDate) {
                    Log.e("all date", str);
                    db.collection("Homework").document(str).collection(schoolName).whereEqualTo("teacherID", id)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                homeworkDate.add(qds.getString("date") + " - " + qds.getId());
                                break;
                            }
                            for (String str : homeworkDate) {
                                Log.e("homework Date", str);
                            }
                        }
                    });
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String str : homeworkDate) {
                    Log.e("homework Date", str);
//                    showHomework(str, schoolName, id);
                }
            }
        });
        showHomework(homeworkDate,schoolName,id);

    }

    private void showHomework(ArrayList<String> dates, String schoolName, String id) {

//        for (String date : dates) {

            Query query1 = db.collection("Homework");
            FirestoreRecyclerOptions<DateModel> options = new FirestoreRecyclerOptions.Builder<DateModel>().setQuery(query1, DateModel.class).build();

            adapter1 = new FirestoreRecyclerAdapter<DateModel, TeacherHomeworkDateViewholder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull TeacherHomeworkDateViewholder holder, int position, @NonNull DateModel model) {
                    holder.textView.setText(model.getDate());

                    Query query2 = db.collection("Homework").document(model.getDate()).collection(schoolName).whereEqualTo("teacherID", id);
                    FirestoreRecyclerOptions<Homework> options2 = new FirestoreRecyclerOptions.Builder<Homework>().setQuery(query2, Homework.class).build();

                    adapter2 = new FirestoreRecyclerAdapter<Homework, TeacherHomeworkDescriptionViewholder>(options2) {
                        @Override
                        protected void onBindViewHolder(@NonNull TeacherHomeworkDescriptionViewholder holder, int position, @NonNull Homework model) {
                            holder.textViewSubject.setText(model.getSubject());
                            holder.textViewDescription.setText(model.getDescription());
                            holder.textViewClass.setText("Class-" + model.getStd_class() + ", Section-" + model.getSection());
                        }

                        @NonNull
                        @Override
                        public TeacherHomeworkDescriptionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_children_homework, parent, false);
                            return new TeacherHomeworkDescriptionViewholder(view);
                        }
                    };
                    adapter2.startListening();
                    adapter2.notifyDataSetChanged();
                    holder.recyclerView.setAdapter(adapter2);
                }

                @NonNull
                @Override
                public TeacherHomeworkDateViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_teacher_homework, parent, false);
                    return new TeacherHomeworkDateViewholder(view);
                }
            };
            adapter1.startListening();
            adapter1.notifyDataSetChanged();
            recyclerView.setAdapter(adapter1);
        }
//    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
        adapter2.stopListening();
    }
}