package com.example.new_scs.Data;

import com.google.firebase.database.Exclude;

public class UploadImage {
    String name , imageUrl;
    private String mKey;

    public UploadImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public UploadImage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }
}
