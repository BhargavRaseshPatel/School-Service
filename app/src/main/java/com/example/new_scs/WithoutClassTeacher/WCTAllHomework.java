package com.example.new_scs.WithoutClassTeacher;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Model.DateModel;
import com.example.new_scs.Model.Homework;
import com.example.new_scs.R;
import com.example.new_scs.ViewHolder.TeacherHomeworkDateViewholder;
import com.example.new_scs.ViewHolder.TeacherHomeworkDescriptionViewholder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WCTAllHomework extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<DateModel, TeacherHomeworkDateViewholder> adapter1;
    FirestoreRecyclerAdapter<Homework, TeacherHomeworkDescriptionViewholder> adapter2;
    RecyclerView.LayoutManager manager;

    public WCTAllHomework() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w_c_t_all_homework, container, false);
        recyclerView = view.findViewById(R.id.recycleview_wct_allhomework);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        SharedPreferences sp = getActivity().getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);
        String schoolName = sp.getString("SchoolName", null);
        db = FirebaseFirestore.getInstance();

        Query query1 = db.collection("Homework");

        FirestoreRecyclerOptions<DateModel> options = new FirestoreRecyclerOptions.Builder<DateModel>().setQuery(query1, DateModel.class).build();

        adapter1 = new FirestoreRecyclerAdapter<DateModel, TeacherHomeworkDateViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TeacherHomeworkDateViewholder holder, int position, @NonNull DateModel model) {
                holder.textView.setText(model.getDate());

                Query query2 = db.collection("Homework").document(model.getDate()).collection(schoolName).whereEqualTo("teacherID", id);

                FirestoreRecyclerOptions<Homework> options2 = new FirestoreRecyclerOptions.Builder<Homework>().setQuery(query2, Homework.class).build();

                adapter2 = new FirestoreRecyclerAdapter<Homework, TeacherHomeworkDescriptionViewholder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull TeacherHomeworkDescriptionViewholder holder, int position, @NonNull Homework model) {
                        holder.textViewSubject.setText(model.getSubject());
                        holder.textViewDescription.setText(model.getDescription());
                        holder.textViewClass.setText("Class-" + model.getStd_class() + ", Section-" + model.getSection());
                    }

                    @NonNull
                    @Override
                    public TeacherHomeworkDescriptionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_children_homework, parent, false);
                        return new TeacherHomeworkDescriptionViewholder(view);
                    }
                };
                adapter2.startListening();
                adapter2.notifyDataSetChanged();
                holder.recyclerView.setAdapter(adapter2);
            }

            @NonNull
            @Override
            public TeacherHomeworkDateViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_teacher_homework, parent, false);
                return new TeacherHomeworkDateViewholder(view);
            }
        };
        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        recyclerView.setAdapter(adapter1);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter1.stopListening();
        adapter2.stopListening();
    }
}