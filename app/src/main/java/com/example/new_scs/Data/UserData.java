package com.example.new_scs.Data;

import com.google.firebase.firestore.Exclude;

public class UserData {
    private String name, email, password, documentID;
    private Boolean verify;
    private long mobile_no;

    public UserData() {
    }

    public UserData(String name, String email, String password, Boolean verify, long mobile_no) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verify = verify;
        this.mobile_no = mobile_no;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean varify) {
        this.verify = varify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(long mobile_no) {
        this.mobile_no = mobile_no;
    }
}
