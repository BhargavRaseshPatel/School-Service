package com.example.new_scs.Parents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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


public class ChildrenTimetable extends Fragment {


    public ChildrenTimetable() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    List<UploadImage> uploadImages;
    DatabaseReference databaseReference;
    SharedPreferences sp;
    String classValue, sectionValue, schoolName;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_children_timetable, container, false);
        linearLayout = view.findViewById(R.id.ll_no_timetable);
        sp = getActivity().getSharedPreferences("SCS", Context.MODE_PRIVATE);
        classValue = sp.getString("class",null);
        sectionValue = sp.getString("section",null);
        schoolName = sp.getString("SchoolName",null);
        databaseReference = FirebaseDatabase.getInstance().getReference(schoolName).child("class" + classValue + sectionValue + "/timetable");
        recyclerView = view.findViewById(R.id.recycle_view_timetable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uploadImages = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UploadImage uploadImage = dataSnapshot.getValue(UploadImage.class);
                    uploadImage.setKey(uploadImage.getKey());
                    uploadImages.add(uploadImage);
                    linearLayout.setVisibility(View.INVISIBLE);
                }
                imageAdapter = new ImageAdapter(getContext(),uploadImages,schoolName+"/class" + classValue + sectionValue + "/timetable");
                recyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}