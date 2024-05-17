package com.example.new_scs.Data;

import java.util.Map;

public class Add_Class_Data {
    String class_teacher;
    boolean add_student;
    Map<String, String> subject_teacher ;
    String std_section;

    public Add_Class_Data(String class_teacher, boolean add_student, String std_section, Map<String, String> subject_teacher) {
        this.class_teacher = class_teacher;
        this.add_student = add_student;
        this.subject_teacher = subject_teacher;
        this.std_section = std_section;
    }

    public String getStd_section() {
        return std_section;
    }

    public void setStd_section(String std_section) {
        this.std_section = std_section;
    }

    public String getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(String class_teacher) {
        this.class_teacher = class_teacher;
    }

    public boolean isAdd_student() {
        return add_student;
    }

    public void setAdd_student(boolean add_student) {
        this.add_student = add_student;
    }

    public Map<String, String> getSubject_teacher() {
        return subject_teacher;
    }

    public void setSubject_teacher(Map<String, String> subject_teacher) {
        this.subject_teacher = subject_teacher;
    }
}
