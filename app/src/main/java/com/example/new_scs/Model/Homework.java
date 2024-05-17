package com.example.new_scs.Model;

import androidx.annotation.NonNull;

public class Homework {
    String std_class,section,date,description,subject;

    public Homework( String std_class,String section, String date, String description, String subject) {
        this.std_class = std_class;
        this.section = section;
        this.date = date;
        this.description = description;
        this.subject = subject;
    }

    public Homework() {
    }

    @NonNull
    public String getStd_class() {
        return std_class;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }
}
