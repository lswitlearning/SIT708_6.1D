package com.example.pleapp;

import java.io.Serializable;
import java.util.List;

// API返回的顶级类
public class QuizResponse implements Serializable {
    private List<QuizQuestion> quiz;  // 问题列表

    public List<QuizQuestion> getQuiz() {
        return quiz;
    }
}