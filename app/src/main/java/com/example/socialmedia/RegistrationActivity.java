package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {
    EditText name, pass, email, number;
    Button register;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.userName);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        number = findViewById(R.id.num);
        register = findViewById(R.id.btnRegister);

        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        register.setOnClickListener(view -> {
            String name1 = name.getText().toString();
            String pass1 = pass.getText().toString();
            String email1 = email.getText().toString();
            String number1 = number.getText().toString();

            if (name1.isEmpty() || pass1.isEmpty() || email1.isEmpty() || number1.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "PLEASE FILL ALL THE DETAILS", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                email.setError("Invalid Email Address");
            } else if (number1.length() != 10) {
                number.setError("Please Enter 10 Digit Mobile Number");
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    User existingUser = userDao.getUserByUsername(name1);
                    if (existingUser == null) {
                        User user = new User();
                        user.setUsername(name1);
                        user.setPassword(pass1);
                        user.setEmail(email1);
                        user.setNumber(number1);

                        userDao.insert(user);
                        runOnUiThread(() -> {
                            Toast.makeText(RegistrationActivity.this, "Hi " + name1 + " Registered Successfully!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }
}
