package com.example.pleapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class ResultPageActivity extends AppCompatActivity {

    private LinearLayout innerResultLayout;  // 用于显示问题和答案的内层布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);  // 设置布局

        innerResultLayout = findViewById(R.id.innerResultLayout);  // 内层容器
        Button finishButton = findViewById(R.id.finishButton);  // "Finish" 按钮

        // 当点击 "Finish" 时，返回到 `QuizStartActivity`
        finishButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(ResultPageActivity.this, QuizStartActivity.class);
            startActivity(backIntent);  // 启动 `QuizStartActivity`
        });


        Intent intent = getIntent();
        ArrayList<QuizQuestion> answeredQuestions = intent.getParcelableArrayListExtra("all_questions");

        if (answeredQuestions == null || answeredQuestions.isEmpty()) {
            Toast.makeText(this, "No results available.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 清空内层容器
        innerResultLayout.removeAllViews();

        for (int i = 0; i < answeredQuestions.size(); i++) {
            QuizQuestion quizQuestion = answeredQuestions.get(i);

            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            questionLayout.setPadding(15, 15, 15, 15);
            questionLayout.setBackgroundResource(R.drawable.edit_text_border);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.bottomMargin = 15;  // 设定每个卡片的底部间距
            questionLayout.setLayoutParams(layoutParams);

            // 题号
            TextView questionNumber = new TextView(this);
            questionNumber.setText("Question #" + (i + 1));
            questionNumber.setTextColor(ContextCompat.getColor(this, R.color.black));
            questionNumber.setTextSize(16f);

            // 设置上部 padding 为 13dp
            int paddingInDp = 13;  // padding 大小
            float density = getResources().getDisplayMetrics().density;  // 获取屏幕密度
            int paddingInPx = (int) (paddingInDp * density);  // 转换为 px

            questionNumber.setPadding(0, paddingInPx, 0, 0);  // 设置 padding


            // 题目
            TextView questionText = new TextView(this);
            questionText.setText("Question: " + quizQuestion.getQuestion());
            questionText.setTextColor(ContextCompat.getColor(this, R.color.black));
            questionText.setTextSize(16f);

            // 用户答案
            TextView userAnswerText = new TextView(this);
            String userAnswer = quizQuestion.getUserAnswer() != null ? quizQuestion.getUserAnswer() : "No answer selected";
            userAnswerText.setText("Your Answer: " + userAnswer);
            userAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));
            userAnswerText.setTextSize(16f);

            // 正确答案
            TextView correctAnswerText = new TextView(this);
            correctAnswerText.setText("Correct Answer: " + quizQuestion.getCorrectAnswer());
            correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));
            correctAnswerText.setTextSize(16f);
            questionNumber.setPadding(0, 0, 0, 0);

            // 判断用户答案是否正确
            if (quizQuestion.getUserAnswer() != null && quizQuestion.getUserAnswer().equals(quizQuestion.getCorrectAnswer())) {
                correctAnswerText.append(" (You are correct)");
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.green));
            } else {
                correctAnswerText.append(" (You are not correct)");
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.red));
            }

            questionLayout.addView(questionNumber);
            questionLayout.addView(questionText);
            questionLayout.addView(userAnswerText);
            questionLayout.addView(correctAnswerText);

            innerResultLayout.addView(questionLayout);  // 添加到内层容器
        }
    }
}
