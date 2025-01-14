package com.example.androidfacebook.entities;
import java.io.Serializable;
import java.util.*;
/*
  This class is used to store the user information
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String displayName;
    private byte[] photo;

    public User(String username, String password,String displayName, byte[] photo){
        this.username=username;
        this.password=password;
        this.displayName=displayName;
        this.photo=photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
