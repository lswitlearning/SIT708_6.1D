package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;  // Input for the username
    private EditText emailEditText;  // Input for the email
    private EditText passwordEditText;  // Input for the password
    private EditText confirmPasswordEditText;  // Input for confirming the password
    private EditText phoneEditText;  // Input for the phone number
    private DatabaseHelper dbHelper;  // Helper for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);  // Set the corresponding layout file for this activity

        // Get references to the EditText fields from the layout
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        phoneEditText = findViewById(R.id.editTextPhone);

        dbHelper = new DatabaseHelper(this);  // Initialize the database helper
    }

    public void onRegisterButtonClick(View view) {  // Handle the register button click event
        String username = usernameEditText.getText().toString().trim();  // Get the username
        String email = emailEditText.getText().toString().trim();  // Get the email
        String password = passwordEditText.getText().toString().trim();  // Get the password
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();  // Get the confirmation password
        String phone = phoneEditText.getText().toString().trim();  // Get the phone number

        // Validate user input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();  // Show an error message if any field is empty
            return;  // Exit early if validation fails
        }

        if (!password.equals(confirmPassword)) {  // Check if the password matches the confirmation password
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();  // Show an error message if passwords do not match
            return;  // Exit early if validation fails
        }

        // Add the user to the database
        boolean isAdded = dbHelper.addUser(username, email, password, phone);  // Attempt to add the user to the database
        if (isAdded) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();  // Show a success message if registration is successful
            // Navigate to the InterestActivity
            Intent intent = new Intent(this, InterestActivity.class);  // Create an intent to navigate to the InterestActivity
            startActivity(intent);  // Start the new activity
            finish();  // Finish the current activity
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();  // Show an error message if registration fails
        }
    }
}
