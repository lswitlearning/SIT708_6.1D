package com.example.pleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RecyclerView Adapter for displaying quiz results
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<QuizQuestion> questions;  // List of quiz questions

    // Constructor to initialize the adapter with a list of quiz questions
    public ResultAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    // This method is called to create new ViewHolder objects
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);  // Load the layout for the RecyclerView item
        return new ViewHolder(view);  // Return a new ViewHolder object
    }

    // This method binds data to the ViewHolder at a specific position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizQuestion question = questions.get(position);  // Get the question at the specified position

        // Set the question number
        holder.questionNumber.setText("Question #" + (position + 1));  // Set the question number label

        // Set the question text
        holder.questionText.setText("Question: " + question.getQuestion());  // Display the question text

        // Create a RadioGroup to display the answer options
        RadioGroup radioGroup = new RadioGroup(holder.itemView.getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);  // Set the orientation to vertical

        // Iterate through the answer options
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(option);  // Set the RadioButton text to the answer option

            // Check if this option is the user's selected answer
            if (question.getUserAnswer() != null && question.getUserAnswer().equals(option)) {
                radioButton.setChecked(true);  // If it matches, set the RadioButton as checked
            }

            radioGroup.addView(radioButton);  // Add the RadioButton to the RadioGroup
        }

        // Clear any existing views and add the RadioGroup to the ViewHolder
        holder.radioGroupContainer.removeAllViews();  // Ensure the container is empty
        holder.radioGroupContainer.addView(radioGroup);  // Add the new RadioGroup
    }

    // Returns the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return questions.size();  // Return the size of the questions list
    }

    // ViewHolder class to hold references to the views for a single item
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;  // TextView to display the question number
        TextView questionText;  // TextView to display the question text
        ViewGroup radioGroupContainer;  // Container to hold the RadioGroup with answer options

        // Constructor to initialize the ViewHolder with the item view
        public ViewHolder(View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);  // Reference to the question number TextView
            questionText = itemView.findViewById(R.id.questionText);  // Reference to the question text TextView
            radioGroupContainer = itemView.findViewById(R.id.radioGroupContainer);  // Reference to the RadioGroup container
        }
    }
}
