package com.example.new_scs.Data;

import com.google.firebase.database.Exclude;

public class ChildrenDataModel {
    String name,dateofbirth,mobile_no,key;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public ChildrenDataModel() {
    }

    public ChildrenDataModel(String name, String date, String mobile_no) {
        this.name = name;
        this.dateofbirth = date;
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
