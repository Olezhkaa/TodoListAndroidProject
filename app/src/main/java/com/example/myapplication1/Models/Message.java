package com.example.myapplication1.Models;


import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.TimeZone;

public class Message {
    public String userName;
    public String textMessage;
    private String timeMessage;

    public Message() {}
    public Message(String userName, String textMessage) {
        this.userName = userName;
        this.textMessage = textMessage;
        this.timeMessage = nowTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String nowTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        String date = (String) DateFormat.format("dd.MM.yyyy HH:mm", calendar);
        return date;
    }
}
