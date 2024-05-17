package com.example.new_scs.Principal_Teacher_Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.new_scs.Principal.PrincipalMainPage;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PrincipalTeacher6 extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sp;
    RadioButton ct1, ct2, ct3, ct4, ct5, ct6;
    TextView textViewSub1, textViewSub2, textViewSub3, textViewSub4, textViewSub5, textViewSub6;
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    String teacherEmail1, teacherEmail2, teacherEmail3, teacherEmail4, teacherEmail5, teacherEmail6;
    String id1 = null, id2 = null, id3 = null, id4 = null, id5 = null, id6 = null;
    String changeID1, changeID2, changeID3, changeID4, changeID5, changeID6;
    Button btnSaveChanges;
    Boolean v1 = false, v2 = false, v3 = false, v4 = false, v5 = false, v6 = false;
    Switch aSwitch;
    String addClassTeacher, classTeacherID, removeClassTeacherID;
    Boolean addStudent;

    public PrincipalTeacher6() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_teacher6, container, false);
        sp = getContext().getSharedPreferences("SCS", Context.MODE_PRIVATE);
        String classValue = sp.getString("class", null);
        String sectionValue = sp.getString("section", null);
        String schoolName = sp.getString("SchoolName", null);
        addClassTeacher = "ABC";
//        Toast.makeText(getActivity(), "SchoolName"+schoolName, Toast.LENGTH_SHORT).show();
        aSwitch = view.findViewById(R.id.switch2);
        ct1 = view.findViewById(R.id.classTeaccher1);
        ct2 = view.findViewById(R.id.classTeaccher2);
        ct3 = view.findViewById(R.id.classTeaccher3);
        ct4 = view.findViewById(R.id.classTeaccher4);
        ct5 = view.findViewById(R.id.classTeaccher5);
        ct6 = view.findViewById(R.id.classTeaccher6);
        textViewSub1 = view.findViewById(R.id.subject1);
        textViewSub2 = view.findViewById(R.id.subject2);
        textViewSub3 = view.findViewById(R.id.subject3);
        textViewSub4 = view.findViewById(R.id.subject4);
        textViewSub5 = view.findViewById(R.id.subject5);
        textViewSub6 = view.findViewById(R.id.subject6);
        imageButton1 = view.findViewById(R.id.verify1);
        imageButton2 = view.findViewById(R.id.verify2);
        imageButton3 = view.findViewById(R.id.verify3);
        imageButton4 = view.findViewById(R.id.verify4);
        imageButton5 = view.findViewById(R.id.verify5);
        imageButton6 = view.findViewById(R.id.verify6);
        editText1 = view.findViewById(R.id.teacherEmail1);
        editText2 = view.findViewById(R.id.teacherEmail2);
        editText3 = view.findViewById(R.id.teacherEmail3);
        editText4 = view.findViewById(R.id.teacherEmail4);
        editText5 = view.findViewById(R.id.teacherEmail5);
        editText6 = view.findViewById(R.id.teacherEmail6);
        btnSaveChanges = view.findViewById(R.id.save_changes);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aSwitch.isChecked()) {
                    ct1.setVisibility(View.INVISIBLE);
                    ct2.setVisibility(View.INVISIBLE);
                    ct3.setVisibility(View.INVISIBLE);
                    ct4.setVisibility(View.INVISIBLE);
                    ct5.setVisibility(View.INVISIBLE);
                    ct6.setVisibility(View.INVISIBLE);
                    addClassTeacher = "NO";
                } else {
                    ct1.setVisibility(View.VISIBLE);
                    ct2.setVisibility(View.VISIBLE);
                    ct3.setVisibility(View.VISIBLE);
                    ct4.setVisibility(View.VISIBLE);
                    ct5.setVisibility(View.VISIBLE);
                    ct6.setVisibility(View.VISIBLE);
                    addClassTeacher = "YES";
                }
            }
        });

        String id = sp.getString("documentID", null);
        db.collection("Principal").document(id).collection("class").document(classValue).collection("section")
                .document(sectionValue).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewSub1.setText(documentSnapshot.getString("subject_teacher.1Subject"));
                textViewSub2.setText(documentSnapshot.getString("subject_teacher.2Subject"));
                textViewSub3.setText(documentSnapshot.getString("subject_teacher.3Subject"));
                textViewSub4.setText(documentSnapshot.getString("subject_teacher.4Subject"));
                textViewSub5.setText(documentSnapshot.getString("subject_teacher.5Subject"));
                textViewSub6.setText(documentSnapshot.getString("subject_teacher.6Subject"));

                String emailTeacher1 = documentSnapshot.getString("subject_teacher.1Teacher");
                String emailTeacher2 = documentSnapshot.getString("subject_teacher.2Teacher");
                String emailTeacher3 = documentSnapshot.getString("subject_teacher.3Teacher");
                String emailTeacher4 = documentSnapshot.getString("subject_teacher.4Teacher");
                String emailTeacher5 = documentSnapshot.getString("subject_teacher.5Teacher");
                String emailTeacher6 = documentSnapshot.getString("subject_teacher.6Teacher");

                final String classTeacher = documentSnapshot.getString("class_teacher");

                if (!(emailTeacher1 == null || emailTeacher1.equals(""))) {
                    editText1.setText(emailTeacher1);
                    imageButton1.setImageResource(R.drawable.ic_baseline_check_24);
                    imageButton1.setClickable(false);
                    ct1.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher1)) {
                                    id1 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct1.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct1.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (!(emailTeacher2 == null || emailTeacher2.equals(""))) {
                    editText2.setText(emailTeacher2);
                    imageButton2.setImageResource(R.drawable.ic_baseline_check_24);
                    imageButton2.setClickable(false);
                    ct2.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher2)) {
                                    id2 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct2.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct2.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (!(emailTeacher3 == null || emailTeacher3.equals(""))) {
                    editText3.setText(emailTeacher3);
                    imageButton3.setImageResource(R.drawable.ic_baseline_check_24);
                    imageButton3.setClickable(false);
                    ct3.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher3)) {
                                    id3 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct3.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct3.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (!(emailTeacher4 == null || emailTeacher4.equals(""))) {
                    editText4.setText(emailTeacher4);
                    imageButton4.setImageResource(R.drawable.ic_baseline_check_24);
                    imageButton4.setClickable(false);
                    ct4.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher4)) {
                                    id4 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct4.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct4.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (!(emailTeacher5 == null || emailTeacher5.equals(""))) {
                    editText5.setText(emailTeacher5);
                    imageButton5.setClickable(false);
                    imageButton5.setImageResource(R.drawable.ic_baseline_check_24);
                    ct5.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher5)) {
                                    id5 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct5.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct5.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                if (!(emailTeacher6 == null || emailTeacher6.equals(""))) {
                    editText6.setText(emailTeacher6);
                    imageButton6.setClickable(false);
                    imageButton6.setImageResource(R.drawable.ic_baseline_check_24);
                    ct6.setEnabled(true);
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                if (qds.getString("email").equals(emailTeacher6)) {
                                    id6 = qds.getId();
                                    if (qds.getString("classteacher.class") != null) {
                                        ct6.setEnabled(false);
                                        if (qds.getString("classteacher.class").equals(classValue) && qds.getString("classteacher.section").equals(sectionValue)) {
                                            removeClassTeacherID = qds.getId();
                                            ct6.setEnabled(true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

                db.collection("Principal").document(id).collection("class").document(classValue).
                        collection("section").document(sectionValue).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        addStudent = value.getBoolean("add_student");
                    }
                });

                if (classTeacher != null) {
                    aSwitch.setChecked(true);
                    ct1.setVisibility(View.VISIBLE);
                    ct2.setVisibility(View.VISIBLE);
                    ct3.setVisibility(View.VISIBLE);
                    ct4.setVisibility(View.VISIBLE);
                    ct5.setVisibility(View.VISIBLE);
                    ct6.setVisibility(View.VISIBLE);
                    addClassTeacher = "YES";

                    if (classTeacher.equals(emailTeacher1)) {
                        ct1.setChecked(true);
                    } else if (classTeacher.equals(emailTeacher2)) {
                        ct2.setChecked(true);
                    } else if (classTeacher.equals(emailTeacher3)) {
                        ct3.setChecked(true);
                    } else if (classTeacher.equals(emailTeacher4)) {
                        ct4.setChecked(true);
                    } else if (classTeacher.equals(emailTeacher5)) {
                        ct5.setChecked(true);
                    } else if (classTeacher.equals(emailTeacher6)) {
                        ct6.setChecked(true);
                    }
                }
            }
        });

        imageButton();
        radioButton();
        emailAddressChange();

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail1 = editText1.getText().toString();
                teacherEmail2 = editText2.getText().toString();
                teacherEmail3 = editText3.getText().toString();
                teacherEmail4 = editText4.getText().toString();
                teacherEmail5 = editText5.getText().toString();
                teacherEmail6 = editText6.getText().toString();

                if (addClassTeacher.equals("NO")) {
                    if (removeClassTeacherID != null) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                        db.collection("Principal").document(id).collection("class").document(classValue).
                                collection("section").document(sectionValue).update("class_teacher", null);
                        Log.e("Class Teacher", "NO");
                    }
                }

                if (ct1.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id1;
                    if (v1) {
                        classTeacherID = changeID1;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail1);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                } else if (ct2.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id2;
                    if (v2) {
                        classTeacherID = changeID2;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail2);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                } else if (ct3.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id3;
                    if (v3) {
                        classTeacherID = changeID3;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail3);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                } else if (ct4.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id4;
                    if (v4) {
                        classTeacherID = changeID4;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail4);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                } else if (ct5.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id5;
                    if (v5) {
                        classTeacherID = changeID5;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail5);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                } else if (ct6.isChecked() && addClassTeacher.equals("YES")) {
                    classTeacherID = id6;
                    if (v6) {
                        classTeacherID = changeID6;
                    }
                    db.collection("Teacher").document(classTeacherID).update("classteacher.section", sectionValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.class", classValue);
                    db.collection("Teacher").document(classTeacherID).update("classteacher.add_student", addStudent);
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("class_teacher", teacherEmail6);
                    if (removeClassTeacherID != null && !removeClassTeacherID.equals(classTeacherID)) {
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.section", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.class", null);
                        db.collection("Teacher").document(removeClassTeacherID).update("classteacher.add_student", null);
                    }
                }

                Map<String, String> addSujectData = new HashMap<>();
                addSujectData.put("class", classValue);
                addSujectData.put("section", sectionValue);

                Map<String, String> addSchoolName = new HashMap<>();
                addSchoolName.put("SchoolName", schoolName);

                if (v1 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.1Teacher", teacherEmail1);
                    addSujectData.put("SubjectName", textViewSub1.getText().toString());
                    if (teacherEmail1.equals("") || (id1 != null) && !(id1.equals(changeID1))) {
                        db.collection("Teacher").document(id1).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub1.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id1).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id1).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id1).update("classteacher.class", null);
                                    db.collection("Teacher").document(id1).update("classteacher.section", null);
                                    db.collection("Teacher").document(id1).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail1.equals("")) {
                        db.collection("Teacher").document(changeID1).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID1).set(addSchoolName, SetOptions.merge());
                    }
                }
                if (v2 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.2Teacher", teacherEmail2);
                    addSujectData.put("SubjectName", textViewSub2.getText().toString());
                    if (teacherEmail2.equals("") || (id2 != null) && !(id2.equals(changeID2))) {
                        db.collection("Teacher").document(id2).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub2.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id2).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id2).update("classteacher.class", null);
                                    db.collection("Teacher").document(id2).update("classteacher.section", null);
                                    db.collection("Teacher").document(id2).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail2.equals("")) {
                        db.collection("Teacher").document(changeID2).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID2).set(addSchoolName, SetOptions.merge());
                    }
                }
                if (v3 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.3Teacher", teacherEmail3);
                    addSujectData.put("SubjectName", textViewSub3.getText().toString());
                    if (teacherEmail3.equals("") || (id3 != null) && !(id3.equals(changeID3))) {
                        db.collection("Teacher").document(id3).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub3.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id3).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id3).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id3).update("classteacher.class", null);
                                    db.collection("Teacher").document(id3).update("classteacher.section", null);
                                    db.collection("Teacher").document(id3).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail3.equals("")) {
                        db.collection("Teacher").document(changeID3).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID3).set(addSchoolName, SetOptions.merge());
                    }
                }
                if (v4 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.4Teacher", teacherEmail4);
                    addSujectData.put("SubjectName", textViewSub4.getText().toString());
                    if (teacherEmail4.equals("") || (id4 != null) && !(id4.equals(changeID4))) {
                        db.collection("Teacher").document(id4).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub4.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id4).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id4).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id4).update("classteacher.class", null);
                                    db.collection("Teacher").document(id4).update("classteacher.section", null);
                                    db.collection("Teacher").document(id4).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail4.equals("")) {
                        db.collection("Teacher").document(changeID4).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID4).set(addSchoolName, SetOptions.merge());
                    }
                }
                if (v5 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.5Teacher", teacherEmail5);
                    addSujectData.put("SubjectName", textViewSub5.getText().toString());
                    if (teacherEmail5.equals("") || (id5 != null) && !(id5.equals(changeID5))) {
                        db.collection("Teacher").document(id5).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub5.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id5).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id5).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id5).update("classteacher.class", null);
                                    db.collection("Teacher").document(id5).update("classteacher.section", null);
                                    db.collection("Teacher").document(id5).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail5.equals("")) {
                        db.collection("Teacher").document(changeID5).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID5).set(addSchoolName, SetOptions.merge());
                    }
                }
                if (v6 == true) {
                    db.collection("Principal").document(id).collection("class").document(classValue).
                            collection("section").document(sectionValue).update("subject_teacher.5Teacher", teacherEmail6);
                    addSujectData.put("SubjectName", textViewSub6.getText().toString());
                    if (teacherEmail6.equals("") || (id6 != null) && !(id6.equals(changeID6))) {
                        db.collection("Teacher").document(id6).collection("subject").
                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    if (qds.getString("SubjectName").equals(textViewSub6.getText().toString()) &&
                                            qds.getString("class").equals(classValue) && qds.getString("section").equals(sectionValue)) {
                                        String id = qds.getId();
                                        db.collection("Teacher").document(id6).collection("subject").document(id).delete();
                                    }
                                }
                            }
                        });
                        db.collection("Teacher").document(id6).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                if (ds.getString("classteacher.class").equals(classValue) && ds.getString("classteacher.section").equals(sectionValue)) {
                                    db.collection("Teacher").document(id6).update("classteacher.class", null);
                                    db.collection("Teacher").document(id6).update("classteacher.section", null);
                                    db.collection("Teacher").document(id6).update("classteacher.add_student", null);
                                    db.collection("Principal").document(id).collection("class").document(classValue).
                                            collection("section").document(sectionValue).update("class_teacher", null);
                                }
                            }
                        });
                    }
                    if (!teacherEmail6.equals("")) {
                        db.collection("Teacher").document(changeID6).collection("subject").add(addSujectData);
                        db.collection("Teacher").document(changeID6).set(addSchoolName, SetOptions.merge());
                    }
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("class", null);
                editor.putString("section", null);
                editor.commit();

                Intent intent = new Intent(getContext(), PrincipalMainPage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void imageButton() {

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail1 = editText1.getText().toString();
                verifyTeacherEmail1();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail2 = editText2.getText().toString();
                verifyTeacherEmail2();
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail3 = editText3.getText().toString();
                verifyTeacherEmail3();
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail4 = editText4.getText().toString();
                verifyTeacherEmail4();
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail5 = editText5.getText().toString();
                verifyTeacherEmail5();
            }
        });
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherEmail6 = editText6.getText().toString();
                verifyTeacherEmail6();
            }
        });
    }

    private void verifyTeacherEmail1() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail1) || id1 != null && teacherEmail1.equals("")) {
                        imageButton1.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton1.setClickable(false);
                        ct1.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail1.equals("")) {
                            ct1.setChecked(false);
                            ct1.setEnabled(false);
                        } else {
                            changeID1 = queryDocumentSnapshot.getId();
                        }
                        v1 = true;
                        break;
                    }
                }
            }
        });
    }

    private void verifyTeacherEmail2() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail2) || id2 != null && teacherEmail2.equals("")) {
                        imageButton2.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton2.setClickable(false);
                        ct2.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail2.equals("")) {
                            ct2.setChecked(false);
                            ct2.setEnabled(false);
                        } else {
                            changeID2 = queryDocumentSnapshot.getId();
                        }
                        v2 = true;
                        break;
                    }
                }
            }
        });
    }

    private void verifyTeacherEmail3() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail3) || id3 != null && teacherEmail3.equals("")) {
                        imageButton3.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton3.setClickable(false);
                        ct3.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail3.equals("")) {
                            ct3.setChecked(false);
                            ct3.setEnabled(false);
                        } else {
                            changeID3 = queryDocumentSnapshot.getId();
                        }
                        v3 = true;
                        break;
                    }
                }
            }
        });
    }

    private void verifyTeacherEmail4() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail4) || id4 != null && teacherEmail4.equals("")) {
                        imageButton4.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton4.setClickable(false);
                        ct4.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail4.equals("")) {
                            ct4.setChecked(false);
                            ct4.setEnabled(false);
                        } else {
                            changeID4 = queryDocumentSnapshot.getId();
                        }
                        v4 = true;
                        break;
                    }
                }
            }
        });
    }

    private void verifyTeacherEmail5() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail5) || id5 != null && teacherEmail5.equals("")) {
                        imageButton5.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton5.setClickable(false);
                        ct5.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail5.equals("")) {
                            ct5.setChecked(false);
                            ct5.setEnabled(false);
                        } else {
                            changeID5 = queryDocumentSnapshot.getId();
                        }
                        v5 = true;
                        break;
                    }
                }
            }
        });
    }

    private void verifyTeacherEmail6() {
        db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String email = queryDocumentSnapshot.getString("email");
                    if (email.equals(teacherEmail6) || id6 != null && teacherEmail6.equals("")) {
                        imageButton6.setImageResource(R.drawable.ic_baseline_check_24);
                        imageButton6.setClickable(false);
                        ct6.setEnabled(queryDocumentSnapshot.getString("classteacher.class") == null);
                        if (teacherEmail6.equals("")) {
                            ct6.setChecked(false);
                            ct6.setEnabled(false);
                        } else {
                            changeID6 = queryDocumentSnapshot.getId();
                        }
                        v6 = true;
                        break;
                    }
                }
            }
        });
    }

    private void emailAddressChange() {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton1.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton1.setClickable(true);
                ct1.setChecked(false);
                ct1.setEnabled(false);
                v1 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton2.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton2.setClickable(true);
                ct2.setChecked(false);
                ct2.setEnabled(false);
                v2 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton3.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton3.setClickable(true);
                ct3.setEnabled(false);
                ct3.setChecked(false);
                v3 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton4.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton4.setClickable(true);
                ct4.setEnabled(false);
                ct4.setChecked(false);
                v4 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton5.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton5.setClickable(true);
                ct5.setEnabled(false);
                ct5.setChecked(false);
                v5 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageButton6.setImageResource(R.drawable.ic_baseline_close_24);
                imageButton6.setClickable(true);
                ct6.setEnabled(false);
                ct6.setChecked(false);
                v6 = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void radioButton() {
        ct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct2.setChecked(false);
                ct3.setChecked(false);
                ct4.setChecked(false);
                ct5.setChecked(false);
                ct6.setChecked(false);
            }
        });
        ct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct1.setChecked(false);
                ct3.setChecked(false);
                ct4.setChecked(false);
                ct5.setChecked(false);
                ct6.setChecked(false);
            }
        });
        ct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct2.setChecked(false);
                ct1.setChecked(false);
                ct4.setChecked(false);
                ct5.setChecked(false);
                ct6.setChecked(false);
            }
        });
        ct4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct2.setChecked(false);
                ct3.setChecked(false);
                ct1.setChecked(false);
                ct5.setChecked(false);
                ct6.setChecked(false);
            }
        });
        ct5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct2.setChecked(false);
                ct3.setChecked(false);
                ct4.setChecked(false);
                ct1.setChecked(false);
                ct6.setChecked(false);
            }
        });
        ct6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct2.setChecked(false);
                ct3.setChecked(false);
                ct4.setChecked(false);
                ct1.setChecked(false);
                ct5.setChecked(false);
            }
        });
    }
}