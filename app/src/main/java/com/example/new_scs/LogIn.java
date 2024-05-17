package com.example.new_scs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ApplicationExitInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.new_scs.Parents.Parents;
import com.example.new_scs.Principal.PrincipalMainPage;
import com.example.new_scs.Teacher.TeacherMainPage;
import com.example.new_scs.WithoutClassTeacher.WCTMainPage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogIn extends AppCompatActivity {

    TextInputLayout textInputLayoutemail, textInputLayoutpassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email, password;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        forgetPassword = findViewById(R.id.textViewForgetPassword);
        textInputLayoutemail = findViewById(R.id.email_tf_login);
        textInputLayoutpassword = findViewById(R.id.password_tf_login);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }

    private void forgetPassword() {
        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        email = textInputLayoutemail.getEditText().getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(LogIn.this, "Please enter the Email-ID", Toast.LENGTH_SHORT).show();
        } else {
            String[] emailIDGot = {"NO"};
            if (email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                db.collection("Principal").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String emailID = qds.getString("email");
                            if (emailID.equals(email) ) {
                                emailIDGot[0] = "YES";

                                editor.putString("documentID", qds.getId());
                                editor.putString("Category", "Principal");
                                editor.putLong("number", qds.getLong("mobile_no"));
                                editor.putString("SchoolName",qds.getString("schooldata.name"));
                                editor.commit();
                                Intent intent = new Intent(LogIn.this, OTP_Verification.class);
                                intent.putExtra("ResendOTP", "Yes");
                                startActivity(intent);
                            }
                        }
                    }
                });

                if(emailIDGot[0]=="NO"){
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                String emailID = qds.getString("email");
                                if (emailID.equals(email)) {
                                    emailIDGot[0]="YES";

                                    editor.putString("documentID", qds.getId());
                                    editor.putString("Category", "Teacher");
                                    editor.putLong("number", qds.getLong("mobile_no"));
                                    editor.putString("SchoolName",qds.getString("SchoolName"));
                                    editor.commit();

                                    Intent intent = new Intent(LogIn.this, OTP_Verification.class);
                                    intent.putExtra("ResendOTP", "Yes");
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
            }
            else {
                Toast.makeText(LogIn.this, "Enter the correct email", Toast.LENGTH_SHORT).show();
            }
        }

//            if(emailIDGot[0].equals("NO")){
//                Toast.makeText(LogIn.this, "Please enter valid email-ID", Toast.LENGTH_SHORT).show();
//            }
        }

    public void login(View view) {
        email = textInputLayoutemail.getEditText().getText().toString();
        password = textInputLayoutpassword.getEditText().getText().toString();

        SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String classValue = sp.getString("class", null);
        String exist_category = sp.getString("Category", null);

        if (exist_category != null) {
            if (exist_category.equals("Principal")) {
                Intent intent = new Intent(this, PrincipalMainPage.class);
                startActivity(intent);
                finish();
            } else if (exist_category.equals("Teacher")) {
                if (classValue != null) {
                    Intent intent = new Intent(this, TeacherMainPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, WCTMainPage.class);
                    startActivity(intent);
                }
                finish();
            } else if (exist_category.equals("Student")) {
                Intent intent = new Intent(this, Parents.class);
                startActivity(intent);
                finish();
            }
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LogIn.this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        } else {
            String[] emailIDGot = {"NO"};
            if (email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                db.collection("Principal").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            String emailID = queryDocumentSnapshot.getString("email");
                            String pass = queryDocumentSnapshot.getString("password");
                            if (emailID.equals(email) && pass.equals(password)) {
                                emailIDGot[0] = "YES";
                                editor.putString("documentID", queryDocumentSnapshot.getId());
                                editor.putString("Category", "Principal");
                                editor.putLong("number", queryDocumentSnapshot.getLong("mobile_no"));
                                editor.putString("SchoolName",queryDocumentSnapshot.getString("schooldata.name"));
                                editor.commit();
                                Intent intent = new Intent(LogIn.this, PrincipalMainPage.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
                if(emailIDGot[0]=="NO"){
                    db.collection("Teacher").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                String emailID = qds.getString("email");
                                String pass = qds.getString("password");
                                if (emailID.equals(email) && pass.equals(password)) {
                                    emailIDGot[0]="YES";
                                    editor.putString("documentID", qds.getId());
                                    editor.putString("Category", "Teacher");
                                    editor.putLong("number", qds.getLong("mobile_no"));
                                    editor.putString("SchoolName",qds.getString("SchoolName"));
                                    editor.commit();
//                                    Log.e("schoolname", qds.getString("SchoolName"));
                                    if(qds.getString("classteacher.class") != null){
                                        Intent intent = new Intent(LogIn.this, TeacherMainPage.class);
                                        startActivity(intent);
                                    }
                                    else if(qds.getString("SchoolName").equals(null)){
                                        Intent intent = new Intent(LogIn.this, TeacherWithoutAdded.class);
                                        startActivity(intent);
                                        Log.e("scholl ","YES");
                                    }
                                    else if(qds.getString("classteacher.class") == null){
                                        Intent intent = new Intent(LogIn.this, WCTMainPage.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            }
                        }
                    });
            }
            }  else if(email.length()==10){
                db.collection("Student").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            String number = queryDocumentSnapshot.getString("mobile_no");
                            String pass = queryDocumentSnapshot.getString("dateofbirth");
                            if(email.equals(number) && password.equals(pass)){
                                editor.putString("documentID",queryDocumentSnapshot.getId());
                                editor.putString("name", queryDocumentSnapshot.getString("name"));
                                editor.putString("Category","Student");
                                editor.putLong("number",Long.parseLong(queryDocumentSnapshot.getString("mobile_no")));
                                editor.putString("class",queryDocumentSnapshot.getString("class"));
                                editor.putString("section",queryDocumentSnapshot.getString("section"));
                                editor.putString("SchoolName",queryDocumentSnapshot.getString("SchoolName"));
                                editor.commit();
                                Intent intent = new Intent(LogIn.this, Parents.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
            else {
                Toast.makeText(LogIn.this, "Enter the correct email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void login_signup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogIn.this.finish();
        System.exit(0);
    }
}