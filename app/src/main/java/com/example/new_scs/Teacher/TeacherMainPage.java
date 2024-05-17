package com.example.new_scs.Teacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.new_scs.Adapter.ChildrenDataAdapter;
import com.example.new_scs.Adapter.TeacherAttendanceAdapter;
import com.example.new_scs.Data.ChildrenDataModel;
import com.example.new_scs.Profile;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColorAuto;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeacherMainPage extends AppCompatActivity {

    ImageView imageView, imageViewProfile;
    Dialog dialog;
    TextView textViewClass, textViewSection, textViewTodayAttendance;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String classValue, sectionValue;
    String schoolName;
    RecyclerView recyclerView, recyclerViewName;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<ChildrenDataModel> childrenDatas;
    TeacherAttendanceAdapter childrenAttendanceAdapter;
    Button buttonAddAttendance;
    EditText editTextName;
    ChildrenDataAdapter childrenDataAdapter;
    Boolean bool;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_page);
        imageView = findViewById(R.id.imageView8);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        dialog = new Dialog(this);
        recyclerView = findViewById(R.id.recycleview_student_attendance);
        buttonAddAttendance = findViewById(R.id.button22);
        editTextName = findViewById(R.id.search_student_name);
        recyclerViewName = findViewById(R.id.recycleview_student_name);
        textViewTodayAttendance = findViewById(R.id.textView66);
        childrenDatas = new ArrayList<ChildrenDataModel>();
        linearLayout = findViewById(R.id.ll_no_student);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);

        db.collection("Teacher").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bool = documentSnapshot.getBoolean("classteacher.add_student");
                displayClass(id,currentDate,"",bool);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewAdd(bool);
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainPage.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
        textViewClass = findViewById(R.id.textView47);
        textViewSection = findViewById(R.id.textView49);

        buttonAddAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance(currentDate);
                Intent intent = new Intent(TeacherMainPage.this,TeacherMainPage.class);
                startActivity(intent);
                finish();
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    displayClass(id,currentDate,s.toString(),bool);
                }
                else displayClass(id, currentDate,"",bool);
            }
        });
    }

    private void displayClass(String id, String currentDate,String data,Boolean bool) {
        db.collection("Teacher").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                classValue = documentSnapshot.getString("classteacher.class");
                sectionValue = documentSnapshot.getString("classteacher.section");
                schoolName = documentSnapshot.getString("SchoolName");
                textViewClass.setText("Class-" + classValue);
                textViewSection.setText("Section-" + sectionValue);
                SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("class", classValue);
                editor.putString("section", sectionValue);
                editor.putString("SchoolName", schoolName);
                editor.apply();
                Log.e("Class",classValue+sectionValue);

                db.collection("Attendance").document(currentDate).collection(schoolName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                            String id = qds.getId();
                            if(id.equals(classValue+sectionValue)){
                                linearLayout.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                recyclerViewName.setVisibility(View.VISIBLE);
                                textViewTodayAttendance.setVisibility(View.INVISIBLE);
                                editTextName.setVisibility(View.VISIBLE);
                                buttonAddAttendance.setEnabled(false);
                                buttonAddAttendance.setBackgroundColor(Color.rgb(234,234,234));
                                buttonAddAttendance.setTextColor(Color.rgb(10,16,69));
                                buttonAddAttendance.setText("Today Attendance has taken");
                                break;
                            }
                        }
                    }
                });

                db.collection("Student").whereEqualTo("SchoolName", schoolName).whereEqualTo("class", classValue).
                        whereEqualTo("section", sectionValue).orderBy("name").startAt(data).endAt(data+"\uf8ff").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(data!="") {
                            childrenDatas.clear();
                            name.clear();
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED ){
                                name.add(documentChange.getDocument().getString("name"));
                                Log.e("name",documentChange.getDocument().getString("name"));
                                ChildrenDataModel cdm = documentChange.getDocument().toObject(ChildrenDataModel.class);
                                cdm.setKey(documentChange.getDocument().getId());
                                childrenDatas.add(cdm);
                            }
                            else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                                ChildrenDataModel cdm = documentChange.getDocument().toObject(ChildrenDataModel.class);
                                cdm.setKey(documentChange.getDocument().getId());
                                childrenDatas.add(cdm);
                            }
                        }
                        if(name.isEmpty()){
                            buttonAddAttendance.setEnabled(false);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        childrenDataAdapter.notifyDataSetChanged();
                        childrenAttendanceAdapter.notifyDataSetChanged();
                    }
                });

                childrenAttendanceAdapter = new TeacherAttendanceAdapter(TeacherMainPage.this, name);
                recyclerView.setLayoutManager(new LinearLayoutManager(TeacherMainPage.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(childrenAttendanceAdapter);
//                recyclerView.setVisibility(View.INVISIBLE);

                childrenDataAdapter = new ChildrenDataAdapter(TeacherMainPage.this,childrenDatas,bool);
                recyclerViewName.setLayoutManager(new LinearLayoutManager(TeacherMainPage.this));
                recyclerViewName.setHasFixedSize(true);
                recyclerViewName.setAdapter(childrenDataAdapter);
            }
        });
    }

    public void saveAttendance(String currentDate) {
        Map<String, Object> map = new HashMap<>();

        map.put("std_class",classValue+sectionValue);
        for (int i = 0; i < name.size(); i++) {
            map.put(name.get(i), 1);
        }
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("date", currentDate);
        db.collection("Attendance").document(currentDate).set(dateMap);
        db.collection("Attendance").document(currentDate).collection(schoolName).document(classValue + sectionValue).set(map);

        if (childrenAttendanceAdapter.getAllAbsent().size() > 0) {

            for (int i = 0; i < childrenAttendanceAdapter.getAllAbsent().size(); i++) {
                db.collection("Attendance").document(currentDate).collection(schoolName).document(classValue + sectionValue).update(childrenAttendanceAdapter.getAllAbsent().get(i), 0);
            }
        }

    }

    private void imageViewAdd(Boolean bool) {
        dialog.setContentView(R.layout.teacher_dialog);
        Button addHomework, addTimetable, addStudent, cancel;
        addHomework = dialog.findViewById(R.id.button16);
        addTimetable = dialog.findViewById(R.id.button17);
        addStudent = dialog.findViewById(R.id.button18);
        cancel = dialog.findViewById(R.id.button19);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (bool) {
            addStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TeacherMainPage.this, TeacherAddStudent.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
        } else {
            addStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TeacherMainPage.this, "New Student is not allowed to add.", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        addStudent.setEnabled(false);

        addTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMainPage.this, TeacherTimetable.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherMainPage.this, TeacherNewHomework.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}