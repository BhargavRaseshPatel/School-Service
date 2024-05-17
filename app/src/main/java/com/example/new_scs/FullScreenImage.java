package com.example.new_scs;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;

public class FullScreenImage extends AppCompatActivity {

    TouchImageView imageView;
    TextView textView;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.textView91);

        storageReference = FirebaseStorage.getInstance().getReference(getIntent().getStringExtra("path"));
        textView.setText(getIntent().getStringExtra("name"));
        storageReference.child(getIntent().getStringExtra("name")+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri != null){
                    String url =uri.toString();
                    Glide.with(FullScreenImage.this).load(url).into(imageView);
                }
            }
        });
        Log.e("name",getIntent().getStringExtra("name"));
    }
}