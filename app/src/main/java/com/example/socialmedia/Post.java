package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "posts")
public class Post implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String content;
    private String imageUri; // Field for storing the image URI
    private String profilePhotoUri;  // Added profile photo URI


    // Constructor
    public Post(String username, String content, String imageUri) {
        this.username = username;
        this.content = content;
        this.imageUri = imageUri;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getProfilePhotoUri() {
        return profilePhotoUri;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        this.profilePhotoUri = profilePhotoUri;
    }

    @NonNull
    @Override
    public String toString() {
        return content;
    }
}
