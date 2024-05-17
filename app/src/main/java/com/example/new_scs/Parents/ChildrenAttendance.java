package com.example.new_scs.Parents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Adapter.ChildrenAttendanceAdapter;
import com.example.new_scs.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import java.util.ArrayList;

public class ChildrenAttendance extends Fragment {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    SharedPreferences sp;
    ArrayList<String> dates;
    TextView textViewPresent, textViewAbsent;
    ChildrenAttendanceAdapter attendanceAdapter;
    static int absent=0,present=0,total;

    public ChildrenAttendance() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_children_attendance, container, false);
        dates = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleview_children_attendance);
        textViewPresent = view.findViewById(R.id.textView88);
        textViewAbsent = view.findViewById(R.id.textView89);

        db = FirebaseFirestore.getInstance();
        sp = getContext().getSharedPreferences("SCS", Context.MODE_PRIVATE);
        final String schoolName = sp.getString("SchoolName", null);
        String name = sp.getString("name", null);
        final String classValue = sp.getString("class", null);
        final String sectionValue = sp.getString("section", null);

        db.collection("Attendance").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                    String date = qds.getId();
                    db.collection("Attendance").document(date).collection(schoolName).
                            whereEqualTo("std_class",classValue+sectionValue).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @SuppressLint("SuspiciousIndentation")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    Log.e("date -- ",qds.getId());

                                    if (dc.getDocument().getLong(name) == null) {
                                            dates.add("0" + date);
//                                            Log.e("ID", value.getId());
                                            absent += 1;
                                        } else if (dc.getDocument().getLong(name) != null ) {
                                            dates.add("" + dc.getDocument().getLong(name) + date);
                                            if(dc.getDocument().getLong(name)==1){
                                                present += 1;
                                            } else {
                                                absent += 1;
                                            }
                                        }
                                        attendanceAdapter.notifyDataSetChanged();
                                        total = present + absent ;
                                            Log.e("present",""+(present*100)/total);
                                            int presentPercentage = (present*100)/total;
                                            int absentPercentage = 100 - presentPercentage;
                                            textViewPresent.setText("Present : "+presentPercentage+"%");
                                            textViewAbsent.setText("Absent : "+absentPercentage+"%");
                                }
                            }
                        }
                    });
                }
            }
        });


        Log.e("school",schoolName);
        attendanceAdapter = new ChildrenAttendanceAdapter(getContext(), dates);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(attendanceAdapter);
        return view;
    }
}