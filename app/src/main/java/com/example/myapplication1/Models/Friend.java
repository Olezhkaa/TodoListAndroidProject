package com.example.myapplication1.Models;

import com.google.firebase.auth.FirebaseAuth;

public class Friend {

    private String userUID, friendUID;

    public Friend() {}

    public Friend(String friendUID) {
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.friendUID = friendUID;
    }

    public Friend(String userUID, String friendUID) {
        this.userUID = userUID;
        this.friendUID = friendUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFriendUID() {
        return friendUID;
    }

    public void setFriendUID(String friendUID) {
        this.friendUID = friendUID;
    }
}
