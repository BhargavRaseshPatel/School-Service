package com.example.new_scs;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.Data.ClassTeacher;
import com.example.new_scs.Parents.Parents;
import com.example.new_scs.Principal.PrincipalMainPage;
import com.example.new_scs.Principal.PrincipalSchoolName;
import com.example.new_scs.Teacher.TeacherMainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTP_Verification extends AppCompatActivity {

    TextView phone;
    EditText no1, no2, no3, no4, no5, no6;
    String no1s, no2s, no3s, no4s, no5s, no6s, otp, category;
    ProgressBar progressBar;
    Button otp_verify;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SharedPreferences sp;
    String id;
    String changePasswordOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        phone = findViewById(R.id.phone);
        no1 = findViewById(R.id.textView9);
        no2 = findViewById(R.id.textView10);
        no3 = findViewById(R.id.textView11);
        no4 = findViewById(R.id.textView12);
        no5 = findViewById(R.id.textView13);
        no6 = findViewById(R.id.textView14);
        progressBar = findViewById(R.id.progressbar_verifying_otp);
        otp_verify = findViewById(R.id.btn_otp_verify);

        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        category = sp.getString("Category", null);
        id = sp.getString("documentID", null);
        long num = sp.getLong("number", 0);
        changePasswordOTP = getIntent().getStringExtra("ResendOTP");

        if (changePasswordOTP.equals("Yes")) {
            resendOTP(num);
            Log.e("resend_otp", "" + num + " " + changePasswordOTP);
        }

        String no_display = "+91" + num;
        phone.setText(no_display);
        otp = getIntent().getStringExtra("otp");
        numberTopMove();

        otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify(category, id);
            }
        });

        findViewById(R.id.layout_resend_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP(num);
            }
        });
    }

    private void resendOTP(long number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + number, 60, TimeUnit.SECONDS, OTP_Verification.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Toast.makeText(OTP_Verification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String newotp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otp = newotp;
                    }
                });
    }

    private void verify(String category, String id) {
        no1s = no1.getText().toString().trim();
        no2s = no2.getText().toString().trim();
        no3s = no3.getText().toString().trim();
        no4s = no4.getText().toString().trim();
        no5s = no5.getText().toString().trim();
        no6s = no6.getText().toString().trim();

        if (no1s.isEmpty() || no2s.isEmpty() || no3s.isEmpty() || no4s.isEmpty() || no5s.isEmpty() || no6s.isEmpty()) {
            Toast.makeText(OTP_Verification.this, "please enter all number", Toast.LENGTH_SHORT).show();
        } else {
            String enterOTP = "" + no1s + no2s + no3s + no4s + no5s + no6s;
            if (otp != null) {
                progressBar.setVisibility(View.VISIBLE);
                otp_verify.setVisibility(View.INVISIBLE);

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otp, enterOTP);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        otp_verify.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            verifyTrue(id, category);
                            Toast.makeText(OTP_Verification.this, "Verified Successful", Toast.LENGTH_SHORT).show();

                            if (changePasswordOTP.equals("Yes")) {
                                forgetpassword(category);
                            }

                            else if (category.equals("Principal") && !changePasswordOTP.equals("Yes")) {
                                Intent intent = new Intent(getApplicationContext(), PrincipalSchoolName.class);
                                startActivity(intent);
                                finish();
                            } else if (category.equals("Teacher") && !changePasswordOTP.equals("Yes")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("class", null);
                                map.put("section", null);

                                ClassTeacher classTeacher = new ClassTeacher(map);
                                db.collection(category).document(id).set(classTeacher, SetOptions.merge());
                                Intent intent = new Intent(getApplicationContext(), TeacherWithoutAdded.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (category.equals("Student")){

                            }
                        } else {
                            Toast.makeText(OTP_Verification.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(OTP_Verification.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forgetpassword(String category) {

        Dialog dialog = new Dialog(OTP_Verification.this);
        dialog.setContentView(R.layout.forget_password_dialog);
        EditText newPassword, confirmPassword;
        Button cancel, confirm;
        newPassword = dialog.findViewById(R.id.new_pass_forget_password);
        confirmPassword = dialog.findViewById(R.id.con_pass_forget_password);
        cancel = dialog.findViewById(R.id.cancel);
        confirm = dialog.findViewById(R.id.confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pass = newPassword.getText().toString().trim();
                String con_pass = confirmPassword.getText().toString().trim();

                if (new_pass.equals(con_pass) && new_pass != null && con_pass != null) {
                    if(category.equals("Principal") || category.equals("Teacher")) {
                        db.collection(category).document(id).update("password", new_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OTP_Verification.this, "Password changed successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (category.equals("Principal")) {
                                    Intent intent = new Intent(OTP_Verification.this, PrincipalMainPage.class);
                                    startActivity(intent);
                                    finish();
                                } else if (category.equals("Teacher")) {
                                    Intent intent = new Intent(OTP_Verification.this, TeacherMainPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else {
                        db.collection("Student").document(id).update("dateofbirth",new_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OTP_Verification.this, "Password changed successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent = new Intent(OTP_Verification.this, Parents.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } else {
                    Toast.makeText(OTP_Verification.this, "Enter the correct confirm password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void verifyTrue(String id, String category) {
        try {
            if(category != "Student") {
                db.collection(category).document(id).update("verify", true).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e("Failed to true", e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e("OTP_verify_true", e.getMessage());
        }
    }

    private void numberTopMove() {
        no1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    no2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        no2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    no3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        no3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    no4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        no4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    no5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        no5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    no6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}