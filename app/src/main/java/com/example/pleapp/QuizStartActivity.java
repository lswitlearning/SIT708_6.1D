package com.example.pleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class QuizStartActivity extends AppCompatActivity {

    private TextView smallTaskTextView;
    private TextView loadingText;  // 用于显示 "GENERATING QUIZ"
    private ProgressBar loadingIndicator;  // 加载指示器
    private Retrofit retrofit;
    private QuizService quizService;
    private List<String> selectedInterests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start_page);

        // 初始化 UI 元素
        smallTaskTextView = findViewById(R.id.smallTask);
        Button startButton = findViewById(R.id.startButton);
        Button refreshButton = findViewById(R.id.refreshButton);
        loadingText = findViewById(R.id.loadingText);  // 获取 "GENERATING QUIZ" 的 TextView
        loadingIndicator = findViewById(R.id.loadingIndicator);  // 获取 ProgressBar

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")  // API 基础 URL
                .addConverterFactory(GsonConverterFactory.create())  // 使用 Gson 作为转换器
                .client(new OkHttpClient.Builder().readTimeout(60, java.util.concurrent.TimeUnit.SECONDS).build())  // OkHttp 客户端
                .build();

        quizService = retrofit.create(QuizService.class);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        Set<String> selectedInterestsSet = prefs.getStringSet("selectedInterests", new HashSet<>());
        selectedInterests = new ArrayList<>(selectedInterestsSet);

        if (selectedInterests == null || selectedInterests.isEmpty()) {
            smallTaskTextView.setText("No topics selected.");
            return;
        }

        randomizeTopic();  // 随机选择一个主题

        // 设置 "START" 按钮的点击事件
        startButton.setOnClickListener(view -> {
            loadingText.setVisibility(View.VISIBLE);  // 显示 "GENERATING QUIZ"
            loadingIndicator.setVisibility(View.VISIBLE);  // 显示加载指示器
            fetchQuizDataAndNavigate(getCurrentTopic());  // 获取当前主题，并发起 API 请求
        });

        // 设置 "REFRESH TOPIC" 按钮的点击事件
        refreshButton.setOnClickListener(view -> {
            randomizeTopic();  // 刷新主题
        });
    }

    private void randomizeTopic() {
        if (selectedInterests == null || selectedInterests.isEmpty()) {
            smallTaskTextView.setText("No topics available.");
            return;
        }

        Random random = new Random();
        String selectedTopic = selectedInterests.get(random.nextInt(selectedInterests.size()));
        smallTaskTextView.setText("The topic is " + selectedTopic);
    }

    private void fetchQuizDataAndNavigate(String topic) {
        Call<QuizResponse> call = quizService.getQuiz(topic);

        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                loadingText.setVisibility(View.GONE);  // 隐藏加载文本
                loadingIndicator.setVisibility(View.GONE);  // 隐藏加载指示器

                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String quizDataJson = gson.toJson(response.body());

                    Intent intent = new Intent(QuizStartActivity.this, QuizPageActivity.class);
                    intent.putExtra("quiz_data", quizDataJson);
                    startActivity(intent);
                } else {
                    Toast.makeText(QuizStartActivity.this, "Error fetching quiz.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                loadingText.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(QuizStartActivity.this, "Failed to fetch quiz.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentTopic() {
        return smallTaskTextView.getText().toString().replace("The topic is ", "");
    }
}
