package com.example.new_scs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView schoolName, name, mobile_no, category;
    Button changePassword, signOut;
    SharedPreferences sp;
    Dialog dialog;
    String oldpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dialog = new Dialog(this);

        schoolName = findViewById(R.id.textView29);
        name = findViewById(R.id.textView36);
        mobile_no = findViewById(R.id.textView38);
        category = findViewById(R.id.textView34);
        changePassword = findViewById(R.id.button14);
        signOut = findViewById(R.id.button15);

        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String cate = sp.getString("Category", null);
        String id = sp.getString("documentID", null);
        category.setText(cate);
        schoolName.setText(sp.getString("SchoolName", null));

        fetchData(id, cate);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog(cate, id);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Profile.this).setTitle("Sign Out").setMessage("Are you sure to Sign Out from App ?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Category", null);
                                editor.putString("documentID", null);
                                editor.putLong("number", 0);
                                editor.putString("SchoolName", null);
                                editor.putString("name", null);
                                editor.apply();

                                Intent intent = new Intent(Profile.this, LogIn.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void changePasswordDialog(String cate, String id) {
        dialog.setContentView(R.layout.password_dialog);
        EditText oldPassword, newPassword, confirmPassword;
        oldPassword = dialog.findViewById(R.id.old_password);
        newPassword = dialog.findViewById(R.id.new_Password);
        confirmPassword = dialog.findViewById(R.id.confirm_pass);
        Button dialogCancel, dialogConfirm, dialogForgetPassword;
        dialogConfirm = dialog.findViewById(R.id.button3);
        dialogCancel = dialog.findViewById(R.id.button2);
        dialogForgetPassword = dialog.findViewById(R.id.button5);

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_pass = newPassword.getText().toString().trim();
                String old_pass = oldPassword.getText().toString().trim();
                String con_pass = confirmPassword.getText().toString().trim();
                if (new_pass.equals("") || old_pass.equals("") || con_pass.equals("")) {
                    Toast.makeText(Profile.this, "Enter every Text Field.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_pass.equals(con_pass)) {

                        if (oldpassword.equals(old_pass)) {
                            db.collection(cate).document(id).update("password", new_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Profile.this, "Password changed successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(Profile.this, "Old password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Profile.this, "New Password and Confirm Password are not same.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, OTP_Verification.class);
                intent.putExtra("ResendOTP", "Yes");
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void fetchData(String id, String cate) {
        db.collection(cate).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name_value = documentSnapshot.getString("name");
                long number_value = documentSnapshot.getLong("mobile_no");
                oldpassword = documentSnapshot.getString("password");

                name.setText(name_value);
                mobile_no.setText(String.valueOf(number_value));
            }
        });
    }
}