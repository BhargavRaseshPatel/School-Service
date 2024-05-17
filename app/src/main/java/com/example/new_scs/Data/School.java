package com.example.new_scs.Data;

import java.util.Map;

public class School {
    Map<String, String > schooldata;

    public School(Map<String, String> schooldata) {
        this.schooldata = schooldata;
    }

    public Map<String, String> getSchooldata() {
        return schooldata;
    }

    public void setSchooldata(Map<String, String> schooldata) {
        this.schooldata = schooldata;
    }
}
