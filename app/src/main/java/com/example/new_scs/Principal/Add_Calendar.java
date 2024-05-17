package com.example.new_scs.Principal;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.Data.UploadImage;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Add_Calendar extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    String month;
    SharedPreferences sp;
    String schoolName;
    ArrayList<String> arrayList = new ArrayList<>();
    private Button buttonChooseImage;
    private Button buttonUpload;
    private Spinner spinnerMonth;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageURI;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar);

        arrayList.add("January");
        arrayList.add("February");
        arrayList.add("March");
        arrayList.add("April");
        arrayList.add("May");
        arrayList.add("June");
        arrayList.add("July");
        arrayList.add("August");
        arrayList.add("September");
        arrayList.add("October");
        arrayList.add("November");
        arrayList.add("December");

        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        String id = sp.getString("documentID", null);
        schoolName = sp.getString("SchoolName", null);

        buttonChooseImage = findViewById(R.id.button_choose_image);
        buttonUpload = findViewById(R.id.button_upload);
        spinnerMonth = findViewById(R.id.spinner_month);
        imageView = findViewById(R.id.show_image);
        progressBar = findViewById(R.id.progressBar2);
        storageReference = FirebaseStorage.getInstance().getReference(schoolName);
        spinnerMonth = findViewById(R.id.spinner_month);
        databaseReference = FirebaseDatabase.getInstance().getReference(schoolName);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UploadImage uploadImage = (ds.getValue(UploadImage.class));
                    Log.e("calendar", ds.child("name").toString());
                    arrayList.remove(uploadImage.getName());
                }
                displayMonthSpinner(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonUpload.setClickable(false);
                buttonUpload.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                uploadFile(month);
//                Log.e("month name",month);
            }
        });
    }

    private void displayMonthSpinner(ArrayList<String> arrayList) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter1);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerMonth.setSelection(i);
                month = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void uploadFile(String month) {
        if (imageURI != null && month != null) {
            StorageReference fileReference = storageReference.child("calendar").child(month + ".jpg");

            fileReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 200);
                    Toast.makeText(Add_Calendar.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    UploadImage uploadImage = new UploadImage(month, taskSnapshot.getUploadSessionUri().toString());
                    String uploadID = databaseReference.push().getKey();
                    databaseReference.child("calendar").child(uploadID).setValue(uploadImage);
                    Intent intent = new Intent(Add_Calendar.this, Calendar.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(Add_Calendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No File is Selected or \n Select the month", Toast.LENGTH_SHORT).show();
            buttonUpload.setClickable(true);
        }
        progressBar.setVisibility(View.GONE);
        buttonUpload.setVisibility(View.VISIBLE);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Add_Calendar.this, Calendar.class);
        startActivity(intent);
        finish();
    }
}