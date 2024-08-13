package com.example.socialmedia;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class PostActivity extends AppCompatActivity {
    private Uri imageUri;
    private EditText postContentEditText;
    private ImageView postImageView;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String POST_IMAGE_KEY = "post_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postContentEditText = findViewById(R.id.post_content);
        postImageView = findViewById(R.id.post_image);
        Button postButton = findViewById(R.id.post_button);
        Button selectImageButton = findViewById(R.id.select_image_button);

        // Check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Load saved post image if available
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String postImageUri = prefs.getString(POST_IMAGE_KEY, null);
        if (postImageUri != null) {
            Glide.with(this).load(Uri.parse(postImageUri)).into(postImageView);
        }

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                imageUri = uri;
                postImageView.setImageURI(uri);
                postImageView.setVisibility(View.VISIBLE);
            }
        });

        selectImageButton.setOnClickListener(v -> openImagePicker());

        postButton.setOnClickListener(v -> {
            String content = postContentEditText.getText().toString();
            String photoUri = imageUri != null ? imageUri.toString() : null;

            if (content.isEmpty()) {
                Toast.makeText(PostActivity.this, "Please Post Something", Toast.LENGTH_SHORT).show();
                return;
            }

            Post newPost = new Post(getIntent().getStringExtra("username"), content, photoUri);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(POST_IMAGE_KEY, photoUri);
            editor.apply();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_post", newPost);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(PostActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
