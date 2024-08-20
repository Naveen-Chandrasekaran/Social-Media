package com.example.socialmedia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface FollowerDao {
    @Insert
    void followUser(Follower follower);



    @Query("DELETE FROM followers WHERE followerId = :followerId AND followingId = :followingId")
    void unfollowUser(int followerId, int followingId);

    @Query("SELECT COUNT(*) FROM followers WHERE followerId = :followerId AND followingId = :followingId")
    int isFollowing(int followerId, int followingId);

    @Query("SELECT followingId FROM followers WHERE followerId = :userId")
    List<Integer> getFollowingIds(int userId);

    @Query("SELECT followerId FROM followers WHERE followingId = :userId")
    List<Integer> getFollowerIds(int userId);

    @Query("SELECT U.username FROM Users U INNER JOIN Followers F ON U.id = F.followerId WHERE F.followingId = :userId")
    List<String> getFollowersNames(int userId);
}
