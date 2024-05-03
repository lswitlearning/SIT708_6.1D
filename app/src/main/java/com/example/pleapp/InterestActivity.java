package com.example.pleapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.OkHttpClient;

public class InterestActivity extends AppCompatActivity {

    private List<String> selectedInterests = new ArrayList<>();
    private static final int MAX_INTERESTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_page);

        // 为每个兴趣主题按钮设置点击事件
        setInterestClickListener(R.id.button, "Maths");
        setInterestClickListener(R.id.button1, "Solar Planet");
        setInterestClickListener(R.id.button2, "City of Country");
        setInterestClickListener(R.id.button3, "History");
        setInterestClickListener(R.id.button4, "Technology");
        setInterestClickListener(R.id.button5, "Art and Culture");
        setInterestClickListener(R.id.button6, "Movies");
        setInterestClickListener(R.id.button7, "Sports");
        setInterestClickListener(R.id.button8, "Geography");

        //完成按钮
        Button doneButton = findViewById(R.id.registerButton);
        doneButton.setOnClickListener(view -> {
            if (selectedInterests.size() > MAX_INTERESTS) {
                Toast.makeText(this, "You can select up to 3 topics.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveInterests();
            finish();
        });
    }

    private void setInterestClickListener(int buttonId, String interest) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(view -> toggleInterest(button, interest));
    }

    private void toggleInterest(Button button, String interest) {
        if (selectedInterests.contains(interest)) {
            selectedInterests.remove(interest);
            button.setBackgroundResource(R.drawable.buttonbg);
        } else {
            if (selectedInterests.size() >= MAX_INTERESTS) {
                Toast.makeText(this, "You can select up to 3 topics.", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedInterests.add(interest);
            int color = ContextCompat.getColor(this, R.color.pressBtn_color);
        }
    }
    private void saveInterests() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("selectedInterests", new HashSet<>(selectedInterests));  // 保存兴趣集合
        editor.apply();
    }
}