package com.example.new_scs.Principal;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.new_scs.Principal_Teacher_Fragment.PrincipalTeacher4;
import com.example.new_scs.Principal_Teacher_Fragment.PrincipalTeacher5;
import com.example.new_scs.Principal_Teacher_Fragment.PrincipalTeacher6;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrincipalTeacherDetails extends AppCompatActivity {

    TextView textViewClass, textViewSection;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Switch aSwitch;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_teacher_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait. It's still loading.");
        progressDialog.show();
        textViewClass = findViewById(R.id.textView60);
        textViewSection = findViewById(R.id.textView61);
        aSwitch = findViewById(R.id.switch1);
        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);
        String class_value = sp.getString("class", null);
        String section_value = sp.getString("section", null);

        textViewClass.setText("Class - " + class_value);
        textViewSection.setText("Section - " + section_value);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    db.collection("Principal").document(id).collection("class").document(class_value).collection("section")
                            .document(section_value).update("add_student", true);
                } else {
                    db.collection("Principal").document(id).collection("class").document(class_value).collection("section")
                            .document(section_value).update("add_student", false);
                }
            }
        });

        db.collection("Principal").document(id).collection("class").document(class_value).collection("section")
                .document(section_value).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    String sub6 = documentSnapshot.getString("subject_teacher.6Subject");
                    Boolean addStudent = documentSnapshot.getBoolean("add_student");
                    if (addStudent) {
                        aSwitch.setChecked(true);
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_linear_layout, new PrincipalTeacher6());
                    transaction.commit();
                    Log.e("PrincipalTeacherDetails", sub6);
                    Toast.makeText(PrincipalTeacherDetails.this, "Six-Subject", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    try {
                        String sub5 = documentSnapshot.getString("subject_teacher.5Subject");
                        Log.e("PrincipalTeacherDetails", sub5);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_linear_layout, new PrincipalTeacher5());
                        transaction.commit();
                        Toast.makeText(PrincipalTeacherDetails.this, "Five-Subject", Toast.LENGTH_SHORT).show();
                    } catch (Exception ee) {
                        Toast.makeText(PrincipalTeacherDetails.this, "Four-Subject", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_linear_layout, new PrincipalTeacher4());
                        transaction.commit();
                        Log.e("PrincipalTeacherDetails-sub5", ee.getMessage());
                    }
                    Log.e("PrincipalTeacherDetails-sub6", e.getMessage());
                }
            }
        });
        progressDialog.dismiss();
    }
}