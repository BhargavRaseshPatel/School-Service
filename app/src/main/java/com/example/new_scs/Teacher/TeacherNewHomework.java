package com.example.new_scs.Teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.new_scs.Adapter.TeacherTodayHomework;
import com.example.new_scs.Model.Homework;
import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherNewHomework extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listTeacherSubject = new ArrayList<>();
    Spinner spinnerSubject;
    Button btnAdd, btnCancel, btnAllHomework;
    char subjectSection;
    String subject;
    EditText editTextHomework;
    RecyclerView recyclerView;
    String id,schoolName,currentDate,subjectClass;
    TeacherTodayHomework adapter;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_new_homework);
        spinnerSubject = findViewById(R.id.spinner5);
        btnAdd = findViewById(R.id.button6);
        btnCancel = findViewById(R.id.button4);
        editTextHomework = findViewById(R.id.homework_details);
        recyclerView = findViewById(R.id.recycleview_teacher_homework);
        btnAllHomework = findViewById(R.id.button21);
        linearLayout = findViewById(R.id.layout_lottie_todayHomework);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        id = sp.getString("documentID", null);
        schoolName = sp.getString("SchoolName",null);
        addTodayHomework();

        db.collection("Teacher").document(id).collection("subject").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                    listTeacherSubject.add("CLass-"+qds.getString("class")+", Section-"+ qds.getString("section")+", "+qds.getString("SubjectName"));
                }
            }
        });

        listTeacherSubject.add("Choose class and subject");
            ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listTeacherSubject);
            adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubject.setAdapter(adapterClass);
            spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSubject.setSelection(position);
                    subjectClass = String.valueOf(parent.getItemAtPosition(position).toString().charAt(6));
                    if(subjectClass == "1"){
                        if(parent.getItemAtPosition(position).toString().charAt(7) != ','){
                            subjectSection = parent.getItemAtPosition(position).toString().charAt(18);
                            subject = parent.getItemAtPosition(position).toString().substring(20);
                            subjectClass ="1"+parent.getItemAtPosition(position).toString().charAt(7);
                        }
                        else {
                            subjectSection = parent.getItemAtPosition(position).toString().charAt(17);
                            subject = parent.getItemAtPosition(position).toString().substring(19);
                        }
                    }else {
                        subjectSection = parent.getItemAtPosition(position).toString().charAt(17);
                        subject = parent.getItemAtPosition(position).toString().substring(19);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subjectClass.equals(" ")){
                    Toast.makeText(TeacherNewHomework.this, "Please select the class and subject", Toast.LENGTH_SHORT).show();
                }else {
                    addHomework();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextHomework.setText("");
            }
        });

        btnAllHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherNewHomework.this, TeacherAllHomework.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addHomework() {

        String homeworkDescription = editTextHomework.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("teacherID",id);
        map.put("description",homeworkDescription);
        map.put("subject",subject);
        map.put("date",currentDate);
        map.put("std_class",subjectClass);
        map.put("section",""+subjectSection);
        Map<String,String> map3 = new HashMap<>();
        map3.put("date",currentDate);

        db.collection("Homework").document(currentDate).set(map3,SetOptions.merge());

        db.collection("Homework").document(currentDate).collection(schoolName).
                document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(TeacherNewHomework.this, "Homework added Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherNewHomework.this, "Homework is not added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTodayHomework() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherNewHomework.this));

                    Query query = db.collection("Homework").document(currentDate).collection(schoolName).whereEqualTo("teacherID",id);
        db.collection("Homework").document(currentDate).collection(schoolName).whereEqualTo("teacherID",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
                    FirestoreRecyclerOptions<Homework> options = new FirestoreRecyclerOptions.Builder<Homework>().setQuery(query,Homework.class).build();

//                    if(options != null){
//                        linearLayout.setVisibility(View.INVISIBLE);
//                    }
                    adapter = new TeacherTodayHomework(options);
                    recyclerView.setAdapter(adapter);

                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                            new AlertDialog.Builder(TeacherNewHomework.this).setTitle("Remove Homework").setMessage("Are you sure to remove Homework?").
//                                    setPositiveButton("Remove", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            adapter.deleteItem(viewHolder.getAdapterPosition());
//                                        }
//                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();
                            adapter.deleteItem(viewHolder.getAdapterPosition());
                        }
                    }).attachToRecyclerView(recyclerView);
            }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}