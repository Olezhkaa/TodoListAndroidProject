package com.example.myapplication1.Models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Note {

    private String id, title, date, time;

    public Note(String title, String date, String time) {
        id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public Note(String id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Note() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
