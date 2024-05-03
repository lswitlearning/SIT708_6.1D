package com.example.pleapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

// This class represents a quiz question and implements the Parcelable interface
public class QuizQuestion implements Parcelable {
    // Class properties
    private String question;  // The question text
    private List<String> options;  // A list of possible answer options
    private String correct_answer;  // The correct answer (represented by a letter)
    private String userAnswer;  // The user's selected answer

    // Parcelable implementation - constructor to create from a Parcel
    protected QuizQuestion(Parcel in) {
        question = in.readString();  // Read the question from the Parcel
        options = in.createStringArrayList();  // Read the list of options from the Parcel
        correct_answer = in.readString();  // Read the correct answer
        userAnswer = in.readString();  // Read the user's answer
    }

    // Creator to generate instances of the Parcelable class from a Parcel
    public static final Parcelable.Creator<QuizQuestion> CREATOR = new Parcelable.Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);  // Create a new instance from the Parcel
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];  // Create an array of QuizQuestion
        }
    };

    // Write the object to a Parcel (to serialize the object)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);  // Write the question to the Parcel
        dest.writeStringList(options);  // Write the list of options to the Parcel
        dest.writeString(correct_answer);  // Write the correct answer
        dest.writeString(userAnswer);  // Write the user's answer
    }

    // Describe the contents of the Parcelable (usually 0)
    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters for class properties

    public String getQuestion() {
        return question;  // Get the question text
    }

    public List<String> getOptions() {
        return options;  // Get the list of answer options
    }

    public String getCorrectAnswer() {
        // Return the correct answer based on its corresponding letter
        if (correct_answer.equals("A")) {
            return options.get(0);
        } else if (correct_answer.equals("B")) {
            return options.get(1);
        } else if (correct_answer.equals("C")) {
            return options.get(2);
        } else {
            return options.get(3);  // 'D' is default (corrected from 'C' to 'D')
        }
    }

    public String getUserAnswer() {
        return userAnswer;  // Get the user's selected answer
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;  // Set the user's selected answer
    }
}
