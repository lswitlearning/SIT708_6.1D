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

    private LinearLayout innerResultLayout;  // Container for displaying the question and answer cards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);  // Set the content view to the layout for the results page

        // Initialize the UI components
        innerResultLayout = findViewById(R.id.innerResultLayout);  // Reference to the inner container
        Button finishButton = findViewById(R.id.finishButton);  // Reference to the "Finish" button

        // When the "Finish" button is clicked, navigate back to the QuizStartActivity
        finishButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(ResultPageActivity.this, QuizStartActivity.class);
            startActivity(backIntent);  // Start the QuizStartActivity
        });

        // Retrieve the list of questions from the Intent
        Intent intent = getIntent();
        ArrayList<QuizQuestion> answeredQuestions =
                intent.getParcelableArrayListExtra("all_questions");  // Retrieve the list of questions

        if (answeredQuestions == null || answeredQuestions.isEmpty()) {  // Check if the list is empty
            Toast.makeText(this, "No results available.", Toast.LENGTH_SHORT).show();
            return;  // Exit the method if no results are available
        }

        // Clear any existing views in the inner container
        innerResultLayout.removeAllViews();  // Clear the inner layout to avoid duplicates

        // Loop through the list of questions to create individual cards for each
        for (int i = 0; i < answeredQuestions.size(); i++) {
            QuizQuestion quizQuestion = answeredQuestions.get(i);  // Get the current question

            // Create a new LinearLayout to represent the question card
            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setOrientation(LinearLayout.VERTICAL);  // Set to vertical layout
            questionLayout.setPadding(15, 15, 15, 15);  // Add padding around the card
            questionLayout.setBackgroundResource(R.drawable.edit_text_border);  // Set a border background

            // Create layout parameters for the card with a bottom margin
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.bottomMargin = 15;  // Set the bottom margin for the card
            questionLayout.setLayoutParams(layoutParams);

            // Create and set the question number
            TextView questionNumber = new TextView(this);
            questionNumber.setText("Question #" + (i + 1));  // Display the question number
            questionNumber.setTextColor(ContextCompat.getColor(this, R.color.black));  // Set the text color
            questionNumber.setTextSize(16f);  // Set the text size

            // Create and set the question text
            TextView questionText = new TextView(this);
            questionText.setText("Question: " + quizQuestion.getQuestion());  // Display the question text
            questionText.setTextColor(ContextCompat.getColor(this, R.color.black));  // Set the text color
            questionText.setTextSize(16f);  // Set the text size

            // Create and set the user's answer
            TextView userAnswerText = new TextView(this);
            String userAnswer = quizQuestion.getUserAnswer() != null
                    ? quizQuestion.getUserAnswer()
                    : "No answer selected";  // Default to "No answer selected" if null
            userAnswerText.setText("Your Answer: " + userAnswer);  // Display the user's answer
            userAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));  // Set the text color
            userAnswerText.setTextSize(16f);  // Set the text size

            // Create and set the correct answer
            TextView correctAnswerText = new TextView(this);
            correctAnswerText.setText("Correct Answer: " + quizQuestion.getCorrectAnswer());  // Display the correct answer
            correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.black));  // Set the text color
            correctAnswerText.setTextSize(16f);  // Set the text size

            // Check if the user's answer is correct and adjust the display
            if (quizQuestion.getUserAnswer() != null
                    && quizQuestion.getUserAnswer().equals(quizQuestion.getCorrectAnswer())) {
                correctAnswerText.append(" (You are correct)");  // Add text for correct answer
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.green));  // Set the correct color
            } else {
                correctAnswerText.append(" (You are not correct)");  // Add text for incorrect answer
                correctAnswerText.setTextColor(ContextCompat.getColor(this, R.color.red));  // Set the incorrect color
            }

            // Add the components to the question card
            questionLayout.addView(questionNumber);
            questionLayout.addView(questionText);
            questionLayout.addView(userAnswerText);
            questionLayout.addView(correctAnswerText);

            // Add the question card to the inner layout
            innerResultLayout.addView(questionLayout);  // Add the question card to the inner container
        }
    }
}
