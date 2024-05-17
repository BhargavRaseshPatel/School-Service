package com.example.new_scs.Principal;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_scs.Data.Add_Class_Data;
import com.example.new_scs.Data.ItemViewModel;
import com.example.new_scs.R;
import com.example.new_scs.Subject_Fragment.Sub_4;
import com.example.new_scs.Subject_Fragment.Sub_5;
import com.example.new_scs.Subject_Fragment.Sub_6;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PrincipalAddClass extends AppCompatActivity {

    Spinner spin_class, spin_sec;
    int std_class;
    LinearLayout linearLayout;
    char section;
    SharedPreferences sp;
    FirebaseFirestore db;
    RadioButton fourSubject, fiveSubject, sixSubject;
    private ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_add_class);

        spin_class = findViewById(R.id.spinner2);
        spin_sec = findViewById(R.id.spinner3);
        linearLayout = findViewById(R.id.linearLayout_Subject);
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);
        db = FirebaseFirestore.getInstance();
        fourSubject = findViewById(R.id.radioButton);
        fiveSubject = findViewById(R.id.radioButton2);
        sixSubject = findViewById(R.id.radioButton3);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayout_Subject, new Sub_4());
        transaction.commit();

        fourSubject.setChecked(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_no, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_class.setAdapter(adapter);
        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spin_class.setSelection(i);
                std_class = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sec.setAdapter(adapter1);
        spin_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spin_sec.setSelection(i);
                section = (adapterView.getItemAtPosition(i).toString()).charAt(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fourSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayout_Subject, new Sub_4());
                transaction.commit();
            }
        });

        fiveSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayout_Subject, new Sub_5());
                transaction.commit();
            }
        });

        sixSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayout_Subject, new Sub_6());
                transaction.commit();
            }
        });

        viewModel = new ViewModelProvider(PrincipalAddClass.this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(PrincipalAddClass.this, item -> {

            int i, n = 0;
            String[] str = new String[6];
            String value = "";
            for (i = 0; i < item.length(); ++i) {

                if (item.charAt(i) == ',') {
                    str[n++] = value;
//                    Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
                    value = "";
                } else {
                    value = value + item.charAt(i);
                }
            }

            Map<String, String> subject = new HashMap<>();

            for (i = 0; i < 6; ++i) {
                if (str[i] != null) {
                    subject.put(i + 1 + "Subject", str[i]);
                    subject.put(i + 1 + "Teacher", null);
                    Log.e("Subject", "" + i + " " + str[i]);
                }
            }

            Map<String, String> std_class_map = new HashMap<>();
            std_class_map.put("std_class", String.valueOf(std_class));
            try {
                int j = 0;
                for (j = section; j > 64; --j) {
                    Add_Class_Data add_class_data = new Add_Class_Data(null, false, String.valueOf((char) j), subject);
                    db.collection("Principal").document(id).collection("class").document("" + std_class).set(std_class_map);
                    db.collection("Principal").document(id).collection("class").document("" + std_class)
                            .collection("section").document("" + (char) j).set(add_class_data);
                }
            } catch (Exception e) {
                Log.e("PrincipalAddClass", e.getMessage());
            }
            Intent intent = new Intent(getApplicationContext(), PrincipalMainPage.class);
            startActivity(intent);
            finish();
        });
    }
}