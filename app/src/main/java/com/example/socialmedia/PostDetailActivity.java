package com.example.socialmedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        AppDatabase db = AppDatabase.getDatabase(this);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        TextView postDetails = findViewById(R.id.post_details);
        TextView postUserName = findViewById(R.id.post_username);
        ImageView postImage = findViewById(R.id.post_image_detail);

        int postId = getIntent().getIntExtra("post_id", -1);

        if (postId != -1) {
            LiveData<Post> postLiveData = db.postDao().getPostById(postId);
            postLiveData.observe(this, post -> {
                if (post != null) {
                    postUserName.setText(post.getUsername() != null ? post.getUsername() : "Username not available");
                    postDetails.setText(post.getContent() != null ? post.getContent() : "Content not available");
                    String postImageUri = post.getImageUri(); // Use post's image URI

                    if (postImageUri != null) {
                        Glide.with(PostDetailActivity.this)
                                .load(Uri.parse(postImageUri))
                                .error(R.drawable.ic_default_profile_image)  // Add a fallback image for error cases
                                .into(postImage);
                    } else {
                        postImage.setVisibility(View.GONE);
                    }
                } else {
                    Log.d("PostDetailActivity", "Post is null");
                }
            });
        } else {
            Log.d("PostDetailActivity", "Invalid postId: " + postId);
        }

        postUserName.setOnClickListener(view -> {
            Intent in = new Intent(PostDetailActivity.this, UserProfileActivity.class);
            in.putExtra("user_name",postUserName.getText().toString());
            in.putExtra("post_id",postId);
            in.putExtra("profile","user_profile");
            startActivity(in);
        });
    }
}
