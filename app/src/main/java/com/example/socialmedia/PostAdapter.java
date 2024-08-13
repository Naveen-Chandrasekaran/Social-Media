package com.example.socialmedia;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        super(context, 0, posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        }

        Post post = posts.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.post_username);
        TextView contentTextView = convertView.findViewById(R.id.post_content);
        ImageView postImageView = convertView.findViewById(R.id.post_image);

        usernameTextView.setText(post.getUsername());
        contentTextView.setText(post.getContent());

        String postImageUri = post.getImageUri();
        if (postImageUri != null) {
            postImageView.setVisibility(View.VISIBLE);
            Log.d("PostAdapter", "Loading image: " + postImageUri);
            Glide.with(context)
                    .load(Uri.parse(postImageUri))
                    .error(R.drawable.ic_launcher_background)  // Fallback image for errors
                    .into(postImageView);
        } else {
            postImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
