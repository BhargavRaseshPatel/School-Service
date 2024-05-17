package com.example.new_scs.Data;

public class ChildrenHomeworkData {
    String date, subject, description;

    public ChildrenHomeworkData() {
    }

    public ChildrenHomeworkData(String date, String subject, String description) {
        this.date = date;
        this.subject = subject;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
