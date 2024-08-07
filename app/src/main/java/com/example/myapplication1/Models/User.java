package com.example.myapplication1.Models;

public class User {
    private String username, email, password, aboutMe, accessRights;

    public User(String username, String email, String password, String aboutMe, String accessRights) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.aboutMe = aboutMe;
        this.accessRights = accessRights;
    }

    public User(String username, String email, String password, String aboutMe) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.aboutMe = aboutMe;
        this.accessRights = "User";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.aboutMe = "";
        this.accessRights = "User";
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";
        this.aboutMe = "";
        this.accessRights = "User";
    }

    public User() {}



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }
}
