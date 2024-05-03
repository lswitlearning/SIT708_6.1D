package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);  // 设定对应的布局文件

        // 获取布局中的EditText控件
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        phoneEditText = findViewById(R.id.editTextPhone);

        dbHelper = new DatabaseHelper(this);  // 创建数据库帮助类
    }

    public void onRegisterButtonClick(View view) {  // 处理注册按钮的点击事件
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // 验证用户输入
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {  // 检查密码和确认密码是否匹配
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加用户到数据库
        boolean isAdded = dbHelper.addUser(username, email, password, phone);  // 传入所有参数
        if (isAdded) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            // 导航到登录页面
            Intent intent = new Intent(this, InterestActivity.class);
            startActivity(intent);
            finish();  // 结束当前Activity
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }


    }
}