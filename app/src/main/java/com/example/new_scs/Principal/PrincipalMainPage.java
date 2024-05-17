package com.example.new_scs.Principal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.new_scs.Model.ClassModel;
import com.example.new_scs.Model.SectionModel;
import com.example.new_scs.Profile;
import com.example.new_scs.R;
import com.example.new_scs.ViewHolder.PrincipalClassViewholder;
import com.example.new_scs.ViewHolder.PrincipalSectionViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PrincipalMainPage extends AppCompatActivity {

    RecyclerView.LayoutManager manager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    FirestoreRecyclerAdapter<ClassModel, PrincipalClassViewholder> adapter1;
    FirestoreRecyclerAdapter<SectionModel, PrincipalSectionViewHolder> adapter2;
    SharedPreferences sp;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_main_page);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait. It's still loading.");
        progressDialog.show();
        FloatingActionButton fab = findViewById(R.id.floating_add);
        recyclerView = findViewById(R.id.principal_recycle_class);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);

        Query query = db.collection("Principal").document(id).collection("class");

        FirestoreRecyclerOptions<ClassModel> options1 = new FirestoreRecyclerOptions.Builder<ClassModel>().setQuery(query, ClassModel.class).build();

        adapter1 = new FirestoreRecyclerAdapter<ClassModel, PrincipalClassViewholder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull PrincipalClassViewholder holder, int position, @NonNull ClassModel modelClass) {
                String classDisplay = "Class - " + modelClass.getStd_class();
                holder.textViewClass.setText(classDisplay);

                Query query2 = db.collection("Principal").document(id).collection("class").document(modelClass.getStd_class()).collection("section");
                FirestoreRecyclerOptions<SectionModel> options2 = new FirestoreRecyclerOptions.Builder<SectionModel>().setQuery(query2, SectionModel.class).build();
                adapter2 = new FirestoreRecyclerAdapter<SectionModel, PrincipalSectionViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull PrincipalSectionViewHolder holder, int position, @NonNull SectionModel modelSection) {
                        String sectionDisplay = "Section-" + modelSection.getStd_section();
                        holder.textViewSection.setText(sectionDisplay);
                        holder.textViewSection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PrincipalMainPage.this, PrincipalTeacherDetails.class);
                                intent.putExtra("section", modelSection.getStd_section());
                                intent.putExtra("class", modelClass.getStd_class());
                                SharedPreferences sp = getSharedPreferences("SCS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("class", modelClass.getStd_class());
                                editor.putString("section", modelSection.getStd_section());
                                editor.commit();
                                startActivity(intent);
                            }
                        });
                        holder.textViewSection.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                new AlertDialog.Builder(PrincipalMainPage.this).setTitle("Delete Section").setMessage("Are you sure to remove section from class?").
                                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Principal").document(id).collection("class").document(modelClass.getStd_class()).
                                                        collection("section").document(modelSection.getStd_section()).delete();
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                return false;
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PrincipalSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.sample_principal_class_section, parent, false);
                        return new PrincipalSectionViewHolder(view);
                    }
                };
                adapter2.startListening();
                adapter2.notifyDataSetChanged();
                holder.recyclerViewSection.setAdapter(adapter2);
            }

            @NonNull
            @Override
            public PrincipalClassViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.sample_principal_class, parent, false);
                return new PrincipalClassViewholder(view);
            }
        };
        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        recyclerView.setAdapter(adapter1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        progressDialog.dismiss();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout_principal);

        LinearLayout calendarLayout = dialog.findViewById(R.id.layout_calendar);
        LinearLayout classLayout = dialog.findViewById(R.id.layout_class);
        LinearLayout profileLayout = dialog.findViewById(R.id.layout_profile);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalMainPage.this, Profile.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalMainPage.this, Calendar.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        classLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalMainPage.this, PrincipalAddClass.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
        adapter2.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}