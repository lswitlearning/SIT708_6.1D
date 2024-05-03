package com.example.pleapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// 修改返回类型
public interface QuizService {
    @GET("/getQuiz")
    Call<QuizResponse> getQuiz(@Query("topic") String topic);
}



