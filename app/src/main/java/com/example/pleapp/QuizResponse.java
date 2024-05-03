package com.example.pleapp;

import java.io.Serializable;
import java.util.List;

// Top-level class representing the API response
public class QuizResponse implements Serializable {
    // A list of QuizQuestion objects representing the questions in the response
    private List<QuizQuestion> quiz;

    // Getter for the list of quiz questions
    public List<QuizQuestion> getQuiz() {
        return quiz;  // Return the list of questions
    }
}
