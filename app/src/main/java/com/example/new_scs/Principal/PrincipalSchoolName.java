package com.example.new_scs.Principal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.Data.School;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PrincipalSchoolName extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sp;
    String ID;
    EditText name, address, pincode;
    Button addSchoolData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_school_name);

        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        ID = sp.getString("documentID", null);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        addSchoolData = findViewById(R.id.btn_school_add);

        addSchoolData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData(ID);
            }
        });
    }

    private void addData(String id) {
        String name_v = name.getText().toString();
        String address_v = address.getText().toString();
        String pincode_v = (pincode.getText().toString());

        if (name_v.equals(null) || address_v.equals(null) || pincode_v.equals(null)) {
            Toast.makeText(PrincipalSchoolName.this, "Please enter every parameter.", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("name", name_v);
            map.put("address", address_v);
            map.put("pincode", (pincode_v));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("SchoolName", name_v);
            editor.commit();

            School userData = new School(map);

            db.collection("Principal").document(id).set(userData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PrincipalSchoolName.this, "Successful Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), PrincipalAddClass.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(PrincipalSchoolName.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}