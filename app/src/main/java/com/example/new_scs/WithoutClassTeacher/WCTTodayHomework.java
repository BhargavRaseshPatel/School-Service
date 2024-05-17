package com.example.new_scs.WithoutClassTeacher;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Adapter.TeacherTodayHomework;
import com.example.new_scs.Model.Homework;
import com.example.new_scs.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
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
import java.util.Map;

public class WCTTodayHomework extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listTeacherSubject = new ArrayList<>();
    Spinner spinnerSubject;
    Button btnAdd, btnCancel;
    char subjectSection;
    String subject;
    EditText editTextHomework;
    RecyclerView recyclerView;
    String id, schoolName, currentDate, subjectClass;
    TeacherTodayHomework adapter;
    LinearLayout linearLayout;
    public WCTTodayHomework() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w_c_t_today_homework, container, false);
        spinnerSubject = view.findViewById(R.id.spinner5);
        btnAdd = view.findViewById(R.id.button6);
        btnCancel = view.findViewById(R.id.button4);
        editTextHomework = view.findViewById(R.id.homework_details);
        recyclerView = view.findViewById(R.id.recycleview_teacher_homework);
        linearLayout = view.findViewById(R.id.layout_lottie_todayHomework);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        SharedPreferences sp = getActivity().getSharedPreferences("SCS", MODE_PRIVATE);
        id = sp.getString("documentID", null);
        schoolName = sp.getString("SchoolName", null);
        addTodayHomework();

        db.collection("Teacher").document(id).collection("subject").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    listTeacherSubject.add("CLass-" + queryDocumentSnapshot.getString("class") + ", Section-" +
                            queryDocumentSnapshot.getString("section") + ", " + queryDocumentSnapshot.getString("SubjectName"));
                }
            }
        });

        listTeacherSubject.add("Choose class and subject");
        ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listTeacherSubject);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapterClass);
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSubject.setSelection(position);
                subjectClass = String.valueOf(parent.getItemAtPosition(position).toString().charAt(6));
                if (subjectClass == "1") {
                    if (parent.getItemAtPosition(position).toString().charAt(7) != ',') {
                        subjectSection = parent.getItemAtPosition(position).toString().charAt(18);
                        subject = parent.getItemAtPosition(position).toString().substring(20);
                        subjectClass = "1" + parent.getItemAtPosition(position).toString().charAt(7);
                    } else {
                        subjectSection = parent.getItemAtPosition(position).toString().charAt(17);
                        subject = parent.getItemAtPosition(position).toString().substring(19);
                    }
                } else {
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
                if (subjectClass.equals(" ")) {
                    Toast.makeText(getActivity(), "Please select the class and subject", Toast.LENGTH_SHORT).show();
                } else {
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
        return view;
    }

    private void addHomework() {

        String homeworkDescription = editTextHomework.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("teacherID", id);
        map.put("description", homeworkDescription);
        map.put("subject", subject);
        map.put("date", currentDate);
        map.put("std_class", subjectClass);
        map.put("section", "" + subjectSection);
        Map<String, String> map3 = new HashMap<>();
        map3.put("date", currentDate);

        db.collection("Homework").document(currentDate).set(map3, SetOptions.merge());

        db.collection("Homework").document(currentDate).collection(schoolName).
                document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Homework added Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Homework is not added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTodayHomework() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(schoolName!=null) {
            db.collection("Homework").document(currentDate).collection(schoolName).whereEqualTo("teacherID", id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            linearLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });

            Query query = db.collection("Homework").document(currentDate).collection(schoolName).whereEqualTo("teacherID", id);
            FirestoreRecyclerOptions<Homework> options = new FirestoreRecyclerOptions.Builder<Homework>().setQuery(query, Homework.class).build();

            adapter = new TeacherTodayHomework(options);
            recyclerView.setAdapter(adapter);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}