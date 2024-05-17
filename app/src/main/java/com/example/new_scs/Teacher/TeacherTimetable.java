package com.example.new_scs.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Adapter.ImageAdapter;
import com.example.new_scs.Data.UploadImage;
import com.example.new_scs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherTimetable extends AppCompatActivity {

    Button uploadImage;
    TextView textViewClass, textViewSection;
    String schoolName;
    RecyclerView recyclerView;
    List<UploadImage> uploadImages;
    DatabaseReference databaseReference;
    SharedPreferences sp;
    LinearLayout linearLayout;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_timetable);
        uploadImage = findViewById(R.id.button7);
        textViewClass = findViewById(R.id.textView53);
        textViewSection = findViewById(R.id.textView54);
        recyclerView = findViewById(R.id.recycleview_teacher_timetable);
        linearLayout = findViewById(R.id.ll_no_timetable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploadImages = new ArrayList<>();
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        schoolName = sp.getString("SchoolName", null);
        String std_class = sp.getString("class", null);
        String std_section = sp.getString("section", null);

        databaseReference = FirebaseDatabase.getInstance().getReference(schoolName).child("class" + std_class + std_section + "/timetable");
        textViewClass.setText("CLass - " + std_class);
        textViewSection.setText("Section - " + std_section);

        imageAdapter = new ImageAdapter(TeacherTimetable.this, uploadImages, schoolName + "/class" + std_class + std_section + "/timetable");
        recyclerView.setAdapter(imageAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadImages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UploadImage uploadImage = dataSnapshot.getValue(UploadImage.class);
                    uploadImage.setKey(dataSnapshot.getKey());
                    uploadImages.add(uploadImage);
                    linearLayout.setVisibility(View.INVISIBLE);
                }
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TeacherTimetable.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherTimetable.this, TeacherAddTimeTable.class);
                startActivity(intent);
                finish();
            }
        });
    }
}