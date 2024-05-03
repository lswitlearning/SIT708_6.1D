package com.example.pleapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class QuizQuestion implements Parcelable {  // 实现 Parcelable
    private String question;
    private List<String> options;
    private String correct_answer;
    private String userAnswer;


    // Parcelable 实现
    protected QuizQuestion(Parcel in) {
        question = in.readString();
        options = in.createStringArrayList();  // List<String> 使用 createStringArrayList
        correct_answer = in.readString();
        userAnswer = in.readString();
    }

    public static final Parcelable.Creator<QuizQuestion> CREATOR = new Parcelable.Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(options);  // 使用 writeStringList 传递 List<String>
        dest.writeString(correct_answer);
        dest.writeString(userAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter 和 Setter
    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {

        if (correct_answer.equals("A")) {
            return options.get(0);
        } else if (correct_answer.equals("B")) {
            return options.get(1);
        } else if (correct_answer.equals("C")) {
            return options.get(2);
        } else  { // Corrected from 'C' to 'D'
            return options.get(3);
        }
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
