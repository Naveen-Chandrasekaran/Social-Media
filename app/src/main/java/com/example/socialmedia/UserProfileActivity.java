package com.example.socialmedia;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserProfileActivity extends AppCompatActivity {
    private Button followButton;
    private UserDao userDao;
    private FollowerDao followerDao;
    private User currentUser;  // Logged-in user
    private User displayedUser;  // User profile being displayed
    private String userName;
    private TextView profile_textview;
    SharedPreferences prefs;
    String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followButton = findViewById(R.id.follow_button);
        TextView profileUsername = findViewById(R.id.profile_username);

        Button your_details = findViewById(R.id.your_details);
        Button your_followers = findViewById(R.id.your_followers);
        profile_textview = findViewById(R.id.profile_textview);
        RelativeLayout your_profile_layout = findViewById(R.id.your_profile_layout);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("currentUsername", null);

        String profile = getIntent().getStringExtra("profile");

        // Initialize DAOs
        userDao = AppDatabase.getDatabase(this).userDao();
        followerDao = AppDatabase.getDatabase(this).followerDao();


        assert profile != null;
        if (profile.equals("user_profile")) {


            userName = getIntent().getStringExtra("user_name");
            profileUsername.setText(userName);
            your_profile_layout.setVisibility(View.GONE);

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(() -> {
                currentUser = getCurrentUser();
                displayedUser = getDisplayedUser();

                runOnUiThread(() -> {
                    if (currentUser == null || displayedUser == null) {
                        Toast.makeText(UserProfileActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                        followButton.setText("Error");
                        return;
                    }

                    // Check if the current user is already following the displayed user
                    executorService.execute(() -> {
                        boolean isFollowing = followerDao.isFollowing(currentUser.getId(), displayedUser.getId()) > 0;

                        runOnUiThread(() -> {
                            followButton.setText(isFollowing ? "Unfollow" : "Follow");

                            followButton.setOnClickListener(v -> executorService.execute(() -> {
                                if (followerDao.isFollowing(currentUser.getId(), displayedUser.getId()) > 0) {
                                    // Unfollow
                                    followerDao.unfollowUser(currentUser.getId(), displayedUser.getId());
                                    runOnUiThread(() -> {
                                        followButton.setText("Follow");
                                        Toast.makeText(UserProfileActivity.this, "Unfollowed " + displayedUser.getUsername(), Toast.LENGTH_SHORT).show();
                                    });
                                } else {
                                    // Follow
                                    Follower follower = new Follower();
                                    follower.setFollowerId(currentUser.getId());
                                    follower.setFollowingId(displayedUser.getId());
                                    followerDao.followUser(follower);
                                    runOnUiThread(() -> {
                                        followButton.setText("Unfollow");
                                        Toast.makeText(UserProfileActivity.this, "Following " + displayedUser.getUsername(), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }));
                        });
                    });
                });
            });
        } else if (profile.equals("your_profile")) {
            profileUsername.setText("\t\t\tYour Profile:\t "+currentUserName);
            followButton.setVisibility(View.GONE);

            your_details.setOnClickListener(view -> {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    currentUser = getCurrentUser();
                    if (currentUser != null) {
                        String details = "Email: " + currentUser.getEmail() + "\nPhone: " + currentUser.getNumber();
                        runOnUiThread(() -> profile_textview.setText(details));
                    }
                });
            });

            your_followers.setOnClickListener(view -> {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    currentUser = getCurrentUser();
                    List<String> followers = followerDao.getFollowersNames(currentUser.getId());
                    StringBuilder followersList = new StringBuilder();
                    for (String follower : followers) {
                        followersList.append(follower).append("\n");
                    }
                    runOnUiThread(() -> profile_textview.setText(followersList.length()!=0 ? followersList.toString() : "None"));
                });
            });
        }
    }

    private User getCurrentUser() {

        if (currentUserName != null) {
            return userDao.getUserByUsername(currentUserName); // Ideally, make this call asynchronous
        } else {
            return null;
        }
    }

    private User getDisplayedUser() {
        if (userName != null) {
            return userDao.getUserByUsername(userName); // Ideally, make this call asynchronous
        } else {
            return null;
        }
    }
}
