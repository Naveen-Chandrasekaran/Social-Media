package com.example.socialmedia;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "followers",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "followerId"),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "followingId")
        })
public class Follower {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int followerId;
    private int followingId;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFollowerId() { return followerId; }
    public void setFollowerId(int followerId) { this.followerId = followerId; }
    public int getFollowingId() { return followingId; }
    public void setFollowingId(int followingId) { this.followingId = followingId; }
}