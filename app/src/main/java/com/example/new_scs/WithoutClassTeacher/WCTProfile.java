package com.example.new_scs.WithoutClassTeacher;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WCTProfile extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView schoolName, name, mobile_no, category;
    Button changePassword, signOut;
    SharedPreferences sp;
    Dialog dialog;
    String oldpassword;
    public WCTProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w_c_t_profile, container, false);
        dialog = new Dialog(getContext());

        schoolName = view.findViewById(R.id.textView29);
        name = view.findViewById(R.id.textView36);
        mobile_no = view.findViewById(R.id.textView38);
        category = view.findViewById(R.id.textView34);
        changePassword = view.findViewById(R.id.button14);
        signOut = view.findViewById(R.id.button15);

        sp = getActivity().getSharedPreferences("SCS", MODE_PRIVATE);
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
                new AlertDialog.Builder(getActivity()).setTitle("Sign Out").setMessage("Are you sure to Sign Out from App ?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sp = getActivity().getSharedPreferences("SCS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Category", null);
                                editor.putString("documentID", null);
                                editor.putLong("number", 0);
                                editor.putString("SchoolName", null);
                                editor.putString("name", null);
                                editor.apply();

                                Intent intent = new Intent(getActivity(), LogIn.class);
                                startActivity(intent);
                                getActivity().finish();
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
        return view;
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
                    Toast.makeText(getActivity(), "Enter every Text Field.", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_pass.equals(con_pass)) {

                        if (oldpassword.equals(old_pass)) {
                            db.collection(cate).document(id).update("password", new_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Password changed successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Old password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "New Password and Confirm Password are not same.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getActivity(), OTP_Verification.class);
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