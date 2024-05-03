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

    private List<QuizQuestion> quizQuestions;  // List of quiz questions
    private int currentQuestionIndex = 0;  // Index of the current question
    private LinearLayout parentLayout;  // Layout that holds the question cards
    private LinearLayout questionLayout;  // The original layout for questions
    private Button nextButton;  // The "NEXT" button for navigating questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_page);  // Set the activity's layout

        // Initialize key UI elements
        parentLayout = findViewById(R.id.parentLayout);  // Parent layout containing question cards
        questionLayout = findViewById(R.id.question);  // The original question layout
        nextButton = findViewById(R.id.nextButton);  // The "NEXT" button

        // Retrieve JSON quiz data from the Intent
        Intent intent = getIntent();
        String quizData = intent.getStringExtra("quiz_data");

        // Check if quiz data is valid
        if (quizData == null) {
            Toast.makeText(this, "No quiz data available.", Toast.LENGTH_SHORT).show();
            return;  // Exit if no quiz data
        }

        // Parse the JSON data to get quiz questions
        Gson gson = new Gson();
        QuizResponse quizResponse = gson.fromJson(quizData, QuizResponse.class);

        if (quizResponse == null || quizResponse.getQuiz().isEmpty()) {
            Toast.makeText(this, "Invalid quiz data.", Toast.LENGTH_SHORT).show();
            return;  // Exit if the parsed data is empty or invalid
        }

        // Store the list of quiz questions
        quizQuestions = quizResponse.getQuiz();  // Get the list of quiz questions

        // Hide the original question layout
        questionLayout.setVisibility(View.GONE);

        // Display the first question card
        addQuestionCard(currentQuestionIndex);

        // Set click listener for the "NEXT" button
        nextButton.setOnClickListener(view -> {
            currentQuestionIndex++;  // Move to the next question

            if (currentQuestionIndex < quizQuestions.size()) {
                addQuestionCard(currentQuestionIndex);  // Add a new question card
            }

            // If it's the last question, change the button text to "Submit"
            if (currentQuestionIndex == quizQuestions.size() - 1) {
                nextButton.setText("Submit");  // Change button text to "Submit"

                // Change the button click logic to submit quiz results
                nextButton.setOnClickListener(submitView -> submitQuizResults());
            }
        });
    }

    private void addQuestionCard(int questionIndex) {
        // Create a new card layout for the question
        LinearLayout cardView = new LinearLayout(this);
        cardView.setOrientation(LinearLayout.VERTICAL);
        cardView.setPadding(16, 16, 16, 16);
        cardView.setBackgroundResource(R.drawable.questionbg);  // Set the background

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 16;  // Set bottom margin for the card

        cardView.setLayoutParams(params);

        // Get the current quiz question
        QuizQuestion currentQuestion = quizQuestions.get(questionIndex);

        // Set question number
        TextView questionNumber = new TextView(this);
        questionNumber.setText("Question #" + (questionIndex + 1));
        questionNumber.setTextColor(ContextCompat.getColor(this, R.color.black));
        questionNumber.setTextSize(18f);

        // Set question text
        TextView questionText = new TextView(this);
        questionText.setText(currentQuestion.getQuestion());
        questionText.setTextColor(ContextCompat.getColor(this, R.color.black));
        questionText.setTextSize(18f);

        // Create a RadioGroup for the quiz options
        RadioGroup radioGroupOptions = new RadioGroup(this);
        radioGroupOptions.setId(questionIndex + 1001);  // Unique ID for the RadioGroup
        radioGroupOptions.setOrientation(LinearLayout.VERTICAL);

        // Add quiz options to the RadioGroup
        for (String option : currentQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroupOptions.addView(radioButton);  // Add the RadioButton to the RadioGroup
        }

        // Add components to the card
        cardView.addView(questionNumber);
        cardView.addView(questionText);
        cardView.addView(radioGroupOptions);

        // Add the card to the parent layout
        parentLayout.addView(cardView, parentLayout.getChildCount() - 1);  // Insert before the "NEXT" button
    }

    private void submitQuizResults() {
        ArrayList<QuizQuestion> allQuestions = new ArrayList<>();  // List of all quiz questions
        int i = 1001;  // Starting ID for RadioGroup
        for (QuizQuestion question : quizQuestions) {
            // Get the RadioGroup for the current question
            RadioGroup radioGroup = parentLayout.findViewById(i);
            int selectedId = radioGroup.getCheckedRadioButtonId();  // Get the selected RadioButton ID
            Log.d("answer index", String.valueOf(selectedId));

            // If an answer is selected, get its text
            if (selectedId != -1) {
                RadioButton selectedButton = parentLayout.findViewById(selectedId);
                question.setUserAnswer(selectedButton.getText().toString());
            } else {
                question.setUserAnswer(null);  // No answer selected
            }

            allQuestions.add(question);  // Add the question to the list
            i++;  // Increment the ID
        }

        // Create an Intent to navigate to the ResultPageActivity
        Intent intent = new Intent(this, ResultPageActivity.class);
        intent.putParcelableArrayListExtra("all_questions", allQuestions);  // Pass the list of quiz questions

        startActivity(intent);  // Start the ResultPageActivity
    }
}
