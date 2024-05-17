package com.example.new_scs.Principal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity {

    SharedPreferences sp;
    LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    private Button uploadCalendar;
    private List<UploadImage> uploadImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        linearLayout = findViewById(R.id.ll_no_calendar);
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String schoolName = sp.getString("SchoolName", null);
        databaseReference = FirebaseDatabase.getInstance().getReference(schoolName).child("calendar");
        uploadImages = new ArrayList<>();
        uploadCalendar = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recycle_calendar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(Calendar.this, uploadImages, schoolName + "/calendar");
        recyclerView.setAdapter(imageAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                uploadImages.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadImage uploadImage = postSnapshot.getValue(UploadImage.class);
                    uploadImage.setKey(postSnapshot.getKey());
                    uploadImages.add(uploadImage);
                    linearLayout.setVisibility(View.INVISIBLE);
                }
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(Calendar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        uploadCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Calendar.this, Add_Calendar.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Calendar.this,PrincipalMainPage.class);
        startActivity(intent);
        finish();
    }
}