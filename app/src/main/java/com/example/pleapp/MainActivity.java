package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable edge-to-edge mode for full-screen layout
        setContentView(R.layout.activity_main);  // Set the layout for this activity

        // Handle window insets for better layout adaptation to system UI changes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());  // Get system bar insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);  // Apply padding for system bars
            return insets;  // Return the insets
        });

        // Initialize the EditText fields
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.editTextTextPassword);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);
    }

    // Handle login button click event
    public void onLoginButtonClick(View view) {
        String username = usernameEditText.getText().toString().trim();  // Get the username
        String password = passwordEditText.getText().toString().trim();  // Get the password

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();  // Show error if fields are empty
            return;  // Exit early if validation fails
        }

        // Check if the user exists and the password matches
        if (dbHelper.checkUser(username, password)) {  // Check against the database
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();  // Notify successful login
            // Navigate to Quiz Start Page
            Intent intent = new Intent(this, QuizStartActivity.class);  // Set the target activity
            startActivity(intent);  // Start the new activity
        } else {  // User does not exist or password is incorrect
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();  // Notify invalid login
        }
    }

    // Handle "Need an Account?" text view click event
    public void onNeedAccTextViewClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);  // Set the target activity
        startActivity(intent);  // Navigate to the registration page
    }
}
