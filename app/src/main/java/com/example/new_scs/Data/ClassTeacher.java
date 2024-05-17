package com.example.new_scs.Data;

import java.util.Map;

public class ClassTeacher {
    Map<String, String > classteacher;

    public Map<String, String> getClassteacher() {
        return classteacher;
    }

    public void setClassteacher(Map<String, String> classteacher) {
        this.classteacher = classteacher;
    }

    public ClassTeacher(Map<String, String> classteacher) {
        this.classteacher = classteacher;
    }
}
