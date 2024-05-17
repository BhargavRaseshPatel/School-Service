package com.example.new_scs.Teacher;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.Data.UploadImage;
import com.example.new_scs.Principal.Add_Calendar;
import com.example.new_scs.Principal.Calendar;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TeacherAddTimeTable extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    TextView textViewClass, textViewSection;
    Button buttonChooseImage, btnUploadImage;
    EditText editTextTimetable;
    String std_class, std_section, schoolName;
    SharedPreferences sp;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_time_table);
        imageView = findViewById(R.id.imageView9);
        progressBar = findViewById(R.id.progressBar);
        editTextTimetable = findViewById(R.id.editText_teacher_timetable);
        buttonChooseImage = findViewById(R.id.button8);
        btnUploadImage = findViewById(R.id.button20);
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        std_class = sp.getString("class", null);
        std_section = sp.getString("section", null);
        schoolName = sp.getString("SchoolName", null);
        storageReference = FirebaseStorage.getInstance().getReference(schoolName);
        databaseReference = FirebaseDatabase.getInstance().getReference(schoolName);

        textViewClass = findViewById(R.id.textView56);
        textViewSection = findViewById(R.id.textView57);
        textViewClass.setText("CLass - " + std_class);
        textViewSection.setText("Section - " + std_section);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoosee();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUploadImage.setClickable(false);
                String imageName = editTextTimetable.getText().toString();
                uploadFile(imageName);
            }
        });
    }

    private String getFileExtensionUri(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(String imageName) {
        if (imageUri != null && imageName != null) {
            StorageReference fileReference = storageReference.child("class" + std_class + std_section + "/timetable").
                    child(imageName + "." + getFileExtensionUri(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 200);
                    Toast.makeText(TeacherAddTimeTable.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    UploadImage uploadImage = new UploadImage(imageName, taskSnapshot.getUploadSessionUri().toString());
                    String uploadID = databaseReference.push().getKey();
                    databaseReference.child("class" + std_class + std_section).child("timetable").child(uploadID).setValue(uploadImage);
                    Intent intent = new Intent(TeacherAddTimeTable.this, TeacherTimetable.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TeacherAddTimeTable.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No File is Selected or \n Write the Time-Table name", Toast.LENGTH_SHORT).show();
            btnUploadImage.setClickable(true);
        }
    }

    private void openFileChoosee() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TeacherAddTimeTable.this, TeacherTimetable.class);
        startActivity(intent);
        finish();
    }
}