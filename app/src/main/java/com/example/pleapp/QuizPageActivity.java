package com.example.pleapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class QuizPageActivity extends AppCompatActivity {

    private List<QuizQuestion> quizQuestions;  // 问题列表
    private int currentQuestionIndex = 0;  // 当前问题索引
    private LinearLayout parentLayout;  // 容纳所有卡片的布局
    private LinearLayout questionLayout;  // 原来的布局
    private Button nextButton;  // "NEXT" 按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_page);  // 设置布局

        parentLayout = findViewById(R.id.parentLayout);  // 获取父布局
        questionLayout = findViewById(R.id.question);  // 获取原来的布局
        nextButton = findViewById(R.id.nextButton);  // 获取 "NEXT" 按钮

        // 从 Intent 中获取 JSON 数据
        Intent intent = getIntent();
        String quizData = intent.getStringExtra("quiz_data");

        if (quizData == null) {
            Toast.makeText(this, "No quiz data available.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 解析 JSON 数据
        Gson gson = new Gson();
        QuizResponse quizResponse = gson.fromJson(quizData, QuizResponse.class);

        if (quizResponse == null || quizResponse.getQuiz().isEmpty()) {
            Toast.makeText(this, "Invalid quiz data.", Toast.LENGTH_SHORT).show();
            return;
        }

        quizQuestions = quizResponse.getQuiz();  // 获取问题列表
//        String s = quizQuestions.get(0).getQuestion();
//        Log.d("question",s);

        // 隐藏原来的布局
        questionLayout.setVisibility(View.GONE);

        // 显示第一道题
        addQuestionCard(currentQuestionIndex);  // 添加第一题

        nextButton.setOnClickListener(view -> {
            currentQuestionIndex++;  // 增加索引

            if (currentQuestionIndex < quizQuestions.size()) {
                // 添加新卡片
                addQuestionCard(currentQuestionIndex);
            }

            if (currentQuestionIndex == quizQuestions.size() - 1) {
                // 如果是最后一个问题，将按钮文本改为 "Submit"
                nextButton.setText("Submit");

                // 更改按钮逻辑，例如提交结果
                nextButton.setOnClickListener(submitView -> {
                    // 执行提交操作
                    submitQuizResults();
                });
            }
        });
    }

    private void addQuestionCard(int questionIndex) {
        LinearLayout cardView = new LinearLayout(this);
        cardView.setOrientation(LinearLayout.VERTICAL);
        cardView.setPadding(16, 16, 16, 16);
        cardView.setBackgroundResource(R.drawable.questionbg);  // 使用背景图

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // 宽度
                LinearLayout.LayoutParams.WRAP_CONTENT   // 高度
        );
        params.bottomMargin = 16;  // 设置底部边界

        cardView.setLayoutParams(params);

        // 获取当前问题
        QuizQuestion currentQuestion = quizQuestions.get(questionIndex);

        // 设置问题编号
        TextView questionNumber = new TextView(this);
        questionNumber.setText("Question #" + (questionIndex + 1));
        questionNumber.setTextColor(ContextCompat.getColor(this, R.color.black));
        questionNumber.setTextSize(18f);

        // 设置问题文本
        TextView questionText = new TextView(this);
        questionText.setText(currentQuestion.getQuestion());
        questionText.setTextColor(ContextCompat.getColor(this, R.color.black));
        questionText.setTextSize(18f);

        // 添加 RadioGroup 及其选项
        RadioGroup radioGroupOptions = new RadioGroup(this);
        // new
        radioGroupOptions.setId(questionIndex + 1001);

        radioGroupOptions.setOrientation(LinearLayout.VERTICAL);

        for (String option : currentQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroupOptions.addView(radioButton);  // 将 RadioButton 添加到选项组
        }

        cardView.addView(questionNumber);
        cardView.addView(questionText);
        cardView.addView(radioGroupOptions);

        // 将 cardView 添加到 parentLayout 中 "NEXT" 按钮的上方
        parentLayout.addView(cardView, parentLayout.getChildCount() - 1);  // 添加到 "NEXT" 按钮的上方
    }

    private void submitQuizResults() {
        ArrayList<QuizQuestion> allQuestions = new ArrayList<>();  // 所有问题
        int i = 1001;
        for (QuizQuestion question : quizQuestions) {
            RadioGroup radioGroup = parentLayout.findViewById(i);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            Log.d("answer index", String.valueOf(selectedId));
            Log.d("correct answer", question.getCorrectAnswer());
            Log.d("question", question.getQuestion());

            if (selectedId != -1) {
                RadioButton selectedButton = parentLayout.findViewById(selectedId);
                question.setUserAnswer(selectedButton.getText().toString());
                Log.d("answer index", selectedButton.getText().toString());

            } else {
                question.setUserAnswer(null);  // 如果没有选择答案，设置为 null
            }

            allQuestions.add(question);  // 添加到所有问题列表
            i++;
        }

        // 使用 `Intent` 传递数据
        Intent intent = new Intent(this, ResultPageActivity.class);
        intent.putParcelableArrayListExtra("all_questions", allQuestions);  // 传递数据

        startActivity(intent);  // 启动 `ResultPageActivity`
    }
}