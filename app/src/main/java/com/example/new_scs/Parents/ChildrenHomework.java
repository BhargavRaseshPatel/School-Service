package com.example.new_scs.Parents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Adapter.ChildrenHomeworkAdapter;
import com.example.new_scs.Data.ChildrenHomeworkData;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ChildrenHomework extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sp;
    RecyclerView recyclerViewHomework;
    ArrayList<ChildrenHomeworkData> list = new ArrayList<>();
    ChildrenHomeworkAdapter childrenHomeworkAdapter;
    LinearLayout linearLayout;
    public ChildrenHomework() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_children_homework, container, false);
        recyclerViewHomework = view.findViewById(R.id.recycleview_children_homework);
        linearLayout = view.findViewById(R.id.ll_no_homework);
        recyclerViewHomework.setHasFixedSize(true);
        recyclerViewHomework.setLayoutManager(new LinearLayoutManager(getContext()));

        sp = getContext().getSharedPreferences("SCS", Context.MODE_PRIVATE);
        String classValue = sp.getString("class", null);
        String sectionValue = sp.getString("section", null);
        String schoolName = sp.getString("SchoolName", null);

        fetchData(classValue, sectionValue, schoolName);
        childrenHomeworkAdapter = new ChildrenHomeworkAdapter(list, getContext());
        recyclerViewHomework.setAdapter(childrenHomeworkAdapter);
        return view;
    }

    private void fetchData(String classValue, String sectionValue, String schoolName) {

        db.collection("Homework").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String date = queryDocumentSnapshot.getId();

                    db.collection("Homework").document(date).collection(schoolName).whereEqualTo("std_class", classValue).
                            whereEqualTo("section", sectionValue).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    String subject = documentChange.getDocument().get("subject").toString();
                                    String description = documentChange.getDocument().get("description").toString();

                                    list.add(new ChildrenHomeworkData(date, subject, description));
                                    linearLayout.setVisibility(View.INVISIBLE);
                                }
                                childrenHomeworkAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }
}