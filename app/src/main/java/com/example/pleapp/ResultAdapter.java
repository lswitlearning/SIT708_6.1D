package com.example.pleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<QuizQuestion> questions;

    public ResultAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);  // 加载单个 item 的布局
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizQuestion question = questions.get(position);

        // 添加题号
        holder.questionNumber.setText("Question #" + (position + 1));

        holder.questionText.setText("Question: " + question.getQuestion());

        // 创建 `RadioGroup` 来显示选项
        RadioGroup radioGroup = new RadioGroup(holder.itemView.getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(option);

            // 检查是否是用户选择的答案
            if (question.getUserAnswer() != null && question.getUserAnswer().equals(option)) {
                radioButton.setChecked(true);  // 如果是用户选择的答案，选中
            }

            radioGroup.addView(radioButton);  // 将 `RadioButton` 添加到 `RadioGroup`
        }

        // 将 `RadioGroup` 添加到 `ViewHolder`
        holder.radioGroupContainer.removeAllViews();  // 确保每次都清空
        holder.radioGroupContainer.addView(radioGroup);
    }

    @Override
    public int getItemCount() {
        return questions.size();  // 返回项目的数量
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;  // 新增题号
        TextView questionText;
        ViewGroup radioGroupContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionText = itemView.findViewById(R.id.questionText);  // 问题文本
            radioGroupContainer = itemView.findViewById(R.id.radioGroupContainer);  // `RadioGroup` 容器
        }
    }
}