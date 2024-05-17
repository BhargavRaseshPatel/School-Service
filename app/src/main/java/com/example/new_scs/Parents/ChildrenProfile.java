package com.example.new_scs.Parents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.new_scs.LogIn;
import com.example.new_scs.OTP_Verification;
import com.example.new_scs.Profile;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChildrenProfile extends Fragment {

    SharedPreferences sp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewName, textViewNumber, textViewClass, textViewSection, textViewSchool;
    Button signOut, changePassword;
    Dialog dialog;
    String oldpassword;
    public ChildrenProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_children_profile, container, false);
        textViewName = view.findViewById(R.id.textView31);
        textViewNumber = view.findViewById(R.id.textView33);
        textViewClass = view.findViewById(R.id.textView28);
        textViewSection = view.findViewById(R.id.textView68);
        textViewSchool = view.findViewById(R.id.textView27);
        signOut = view.findViewById(R.id.button13);
//        changePassword = view.findViewById(R.id.button9);
        dialog = new Dialog(getContext());

        sp = getContext().getSharedPreferences("SCS", Context.MODE_PRIVATE);
        String id = sp.getString("documentID", null);

        db.collection("Student").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewName.setText(documentSnapshot.getString("name"));
                textViewNumber.setText(documentSnapshot.getString("mobile_no"));
                oldpassword = documentSnapshot.getString("dateofbirth");
                Log.e("password",oldpassword);
            }
        });
        textViewClass.setText("Class - " + sp.getString("class", null));
        textViewSection.setText("Section - " + sp.getString("section", null));
        textViewSchool.setText(sp.getString("SchoolName", null));

//        changePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changePasswordDialog( id);
//            }
//        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Sign Out").setMessage("Are you sure to Sign Out from App?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Category", null);
                                editor.putString("documentID", null);
                                editor.putLong("number", 0);
                                editor.putString("class", null);
                                editor.putString("section", null);
                                editor.apply();
                                Intent intent = new Intent(getActivity(), LogIn.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        return view;
    }
    private void changePasswordDialog( String id) {
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
                    Toast.makeText(getContext(), "Enter every Text Field.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_pass.equals(con_pass)) {

                        if (oldpassword.equals(old_pass)) {
                            db.collection("Student").document(id).update("password", new_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Password changed successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Old password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "New Password and Confirm Password are not same.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getContext(), OTP_Verification.class);
                intent.putExtra("ResendOTP", "Yes");
                startActivity(intent);
            }
        });

        dialog.show();
    }
}