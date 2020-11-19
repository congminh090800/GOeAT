package com.example.goeat;

import androidx.annotation.NonNull;

import com.example.goeat.auth.Auth;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class User {
    String uid;
    String username;
    String email;
    String avatarURL;
    String gender;
    String hometown;
    long dateCreated;
    long birthdate;

    public User() {
        this.uid = "";
        this.username = "";
        this.email = "";
        this.avatarURL = "";
        this.gender = "";
        this.hometown = "";
        this.dateCreated = (new Date()).getTime();
        this.birthdate = dateCreated;
    }

    private User(String username, String email, String avatarURL, String gender, String hometown, long dateCreated, long birthdate) {
        this.uid = "";
        this.username = username;
        this.email = email;
        this.avatarURL = avatarURL;
        this.gender = gender;
        this.hometown = hometown;
        this.dateCreated = dateCreated;
        this.birthdate = birthdate;
    }

    public void setUid(String uid, Auth.UserUIDSetter setter) {
        if (setter != null) {
            this.uid = uid;
        }
    }

    @Exclude
    public String getUid() {
        return uid;
    }

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

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", gender='" + gender + '\'' +
                ", hometown='" + hometown + '\'' +
                ", dateCreated=" + dateCreated +
                ", birthdate=" + birthdate +
                '}';
    }
    public User copy(){
        return new User(username, email, avatarURL, gender, hometown, dateCreated, birthdate);
    }
}