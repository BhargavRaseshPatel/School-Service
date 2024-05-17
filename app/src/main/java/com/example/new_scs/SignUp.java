package com.example.new_scs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.Data.UserData;
import com.example.new_scs.Parents.Parents;
import com.example.new_scs.Principal.PrincipalMainPage;
import com.example.new_scs.Teacher.TeacherMainPage;
import com.example.new_scs.WithoutClassTeacher.WCTMainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    TextInputLayout name, phone, email, pass, con_password;
    String name_i, email_i, pass_i, con_pass_i;
    Long phone_i;
    Button signup;
    ProgressBar progressBar;
    FirebaseFirestore db;
    RadioButton rbPrincipal, rbTeacher;
    String category;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        name = findViewById(R.id.person_name);
        phone = findViewById(R.id.phone_no);
        email = findViewById(R.id.email_address);
        pass = findViewById(R.id.log_in_password);
        con_password = findViewById(R.id.confirm_password);
        signup = findViewById(R.id.signup);
        rbPrincipal = findViewById(R.id.radioButton_principal);
        rbTeacher = findViewById(R.id.radioButton_teacher);

        String exist_category = sp.getString("Category", null);
        String classValue = sp.getString("class", null);

        if (exist_category != null) {
            if (exist_category.equals("Principal")) {
                Intent intent = new Intent(SignUp.this, PrincipalMainPage.class);
                startActivity(intent);
                finish();
            } else if (exist_category.equals("Teacher")) {
                if (classValue != null) {
                    Intent intent = new Intent(SignUp.this, TeacherMainPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SignUp.this, WCTMainPage.class);
                    startActivity(intent);
                }
                finish();
            } else if (exist_category.equals("Student")) {
                Intent intent = new Intent(this, Parents.class);
                startActivity(intent);
                finish();
            }
        }

        rbPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Principal";
                rbTeacher.setChecked(false);
            }
        });

        rbTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Teacher";
                rbPrincipal.setChecked(false);
            }
        });

        progressBar = findViewById(R.id.progressbar_sending_otp);

        db = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name_i = name.getEditText().getText().toString();
                phone_i = Long.parseLong(phone.getEditText().getText().toString());
                email_i = email.getEditText().getText().toString();
                pass_i = pass.getEditText().getText().toString();
                con_pass_i = con_password.getEditText().getText().toString();

                if (category != null) {
                    if (name_i.isEmpty() || email_i.isEmpty() || pass_i.isEmpty() || con_pass_i.isEmpty()) {
                        Toast.makeText(SignUp.this, "Enter every parameter", Toast.LENGTH_SHORT).show();
                    } else {
                        if (phone_i.toString().length() == 10) {
                            phone.setError(null);
                            phone.setErrorEnabled(false);

                            if (pass_i.equals(con_pass_i)) {
                                con_password.setError(null);
                                con_password.setErrorEnabled(false);

                                if (email_i.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z]+.[a-zA-Z.]+$")) {
                                    email.setError(null);
                                    email.setErrorEnabled(false);


                                    db.collection(category).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            String[] exist = {"no"};

                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    UserData userData = queryDocumentSnapshot.toObject(UserData.class);

                                                    if (phone_i == (userData.getMobile_no())) {
                                                        Toast.makeText(SignUp.this, "Account is already created from this no. \nPlease enter different mobile no", Toast.LENGTH_LONG).show();
                                                        exist[0] = "yes";
                                                        break;
                                                    }
                                                }
                                            }
                                            if (exist[0] == "no") {
                                                mobile_no_exist(category);

                                            }
//                                        Toast.makeText(Sign_Up.this,exist[0],Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    email.setError("Enter valid Email-ID");
                                }

                            } else {
                                con_password.setError("Enter correct confirm password");
                            }
                        } else {
                            phone.setError("Enter 10 digits mobile no.");
                        }
                    }
                } else {
                    Toast.makeText(SignUp.this, "Select the Category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mobile_no_exist(String cate) {
        progressBar.setVisibility(View.VISIBLE);
        signup.setVisibility(View.INVISIBLE);
        firebasesignup(cate);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone_i, 60, TimeUnit.SECONDS, SignUp.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        signup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        signup.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String otp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        signup.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(getApplicationContext(), OTP_Verification.class);
                        intent.putExtra("otp", otp);
                        intent.putExtra("ResendOTP", "No");
                        startActivity(intent);
                        finish();
                    }
                });
    }

    public void firebasesignup(String cate) {
        UserData data = new UserData();
        data.setName(name_i);
        data.setMobile_no(phone_i);
        data.setEmail(email_i);
        data.setPassword(pass_i);
        data.setVerify(false);

        db.collection(cate).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SignUp.this, "Account is created", Toast.LENGTH_SHORT).show();
                Log.e("Create account ID", documentReference.getId());
                SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Category", category);
                editor.putString("documentID", documentReference.getId());
                editor.putLong("number", phone_i);
                editor.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Creating account is unsuccessful" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Sign Up Exception", e.getMessage());
            }
        });
    }

    public void signup_login(View view) {
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
        finish();
    }
}