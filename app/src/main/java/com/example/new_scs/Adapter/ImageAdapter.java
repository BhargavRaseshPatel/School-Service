package com.example.new_scs.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.new_scs.Data.UploadImage;
import com.example.new_scs.FullScreenImage;
import com.example.new_scs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    StorageReference reference;
    String schoolname;
    private Context context;
    private List<UploadImage> list;

    public ImageAdapter(Context context, List<UploadImage> list, String name) {
        this.context = context;
        this.list = list;
        reference = FirebaseStorage.getInstance().getReference(name);
        schoolname = name;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageAdapter.ViewHolder holder, int position) {
        SharedPreferences sp = context.getSharedPreferences("SCS", Context.MODE_PRIVATE);
        String cate = sp.getString("Category", null);

        UploadImage uploadImage = list.get(position);
        holder.textView.setText(uploadImage.getName());
        try {
            reference.child(uploadImage.getName() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (uri != null) {
                        String url = uri.toString();
                        Glide.with(context).load(url).into(holder.imageView);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.e("Image Adapter Download Url", e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Image Adapter", e.getMessage());
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullScreenImage.class);
                intent.putExtra("path",schoolname);
                intent.putExtra("name",uploadImage.getName());
                context.startActivity(intent);
            }
        });

        if (cate.equals("Principal") || cate.equals("Teacher")) {
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context).setTitle("Remove Image").setMessage("Are you sure to delete image ?").
                            setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String imageKey = uploadImage.getKey();
                                    Log.e("Image Key", imageKey + uploadImage.getImageUrl());
                                    StorageReference imageRef = FirebaseStorage.getInstance().getReference(schoolname).child(uploadImage.getName() + ".jpg");
                                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(schoolname);
                                            databaseReference.child(imageKey).removeValue();
                                            Toast.makeText(context, "Remove Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.month_name);
            imageView = itemView.findViewById(R.id.image_calendar);
        }
    }
}
