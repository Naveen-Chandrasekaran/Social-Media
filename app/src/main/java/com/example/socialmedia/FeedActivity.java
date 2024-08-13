package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FeedActivity extends AppCompatActivity {
    private AppDatabase db;
    private PostAdapter adapter;
    private ActivityResultLauncher<Intent> postActivityLauncher;
    private LiveData<List<Post>> postsLiveData;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Button newPostButton = findViewById(R.id.new_post_button);
        ListView feedListView = findViewById(R.id.feed_list);

        db = AppDatabase.getDatabase(this);
        postsLiveData = db.postDao().getAllPosts();  // Obtain LiveData
        username = getIntent().getStringExtra("username");

        adapter = new PostAdapter(this, new ArrayList<>());
        feedListView.setAdapter(adapter);

        // Observe the LiveData to update the UI
        postsLiveData.observe(this, posts -> {
            adapter.clear();
            adapter.addAll(posts);
            adapter.notifyDataSetChanged();
        });

        // Register the ActivityResultLauncher
        postActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    // Correctly retrieve the Post object
                    Post newPost = (Post) data.getSerializableExtra("new_post");
                    if (newPost != null) {
                        Executors.newSingleThreadExecutor().execute(() -> db.postDao().insert(newPost));
                    } else {
                        Log.e("FeedActivity", "No Post object returned");
                    }
                }
            }
        });

        newPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, PostActivity.class);
            intent.putExtra("username", username);
            postActivityLauncher.launch(intent);
        });

        // Handle item clicks
        feedListView.setOnItemClickListener((parent, view, position, id) -> {
            Post selectedPost = (Post) parent.getItemAtPosition(position);

            Intent intent = new Intent(FeedActivity.this, PostDetailActivity.class);
            intent.putExtra("post_id", selectedPost.getId()); // Pass post ID to detail activity
            startActivity(intent);
        });
    }
}
