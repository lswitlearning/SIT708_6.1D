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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        dbHelper = new DatabaseHelper(this);
    }
        public void onLoginButtonClick (View view){
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 检查用户名和密码
            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                // 导航到Quiz Start Page
                Intent intent = new Intent(this, QuizStartActivity.class); // 指定目标Activity
                startActivity(intent); // 开始新的Activity
            } else { // 用户名和密码不匹配
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
        }
    public void onNeedAccTextViewClick(View view) { // 处理"Need an Account?"的点击事件
        Intent intent = new Intent(this, RegisterActivity.class); // 指定目标Activity
        startActivity(intent); // 导航到注册页面
    }
}