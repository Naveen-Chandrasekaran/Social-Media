package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText loginName, loginPass;
    Button login;
    TextView loginRegister;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginName = findViewById(R.id.username);
        loginPass = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);
        loginRegister = findViewById(R.id.registerPrompt);

        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        login.setOnClickListener(view -> {
            String name1 = loginName.getText().toString();
            String pass1 = loginPass.getText().toString();

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = userDao.getUser(name1, pass1);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(MainActivity.this, "Hi " + name1 + " Logged In Successfully!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                        intent.putExtra("username", user.getUsername());
                        startActivity(intent);
                    } else {
                        loginPass.setError("Invalid Password");
                    }
                });
            });
        });

        loginRegister.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(in);
        });
    }
}
