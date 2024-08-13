package com.example.socialmedia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);

    @Query("SELECT * FROM posts")
    LiveData<List<Post>> getAllPosts();  // Return LiveData

    @Query("SELECT * FROM posts WHERE id = :postId")
    LiveData<Post> getPostById(int postId);  // Return LiveData
}
