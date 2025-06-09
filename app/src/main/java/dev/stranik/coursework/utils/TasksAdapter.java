package dev.stranik.coursework.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.stranik.coursework.R;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private final List<TaskItem> tasks;
    private final Runnable onTaskListChanged;

    public TasksAdapter(List<TaskItem> tasks, Runnable onTaskListChanged) {
        this.tasks = tasks;
        this.onTaskListChanged = onTaskListChanged;
    }

    public void addTask() {
        tasks.add(new TaskItem(""));
        notifyItemInserted(tasks.size() - 1);
        onTaskListChanged.run();
    }

    public void removeTask(int position) {
        if (position >= 0 && position < tasks.size()) {
            tasks.remove(position);
            notifyItemRemoved(position);
            onTaskListChanged.run();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskItem task = tasks.get(position);
        holder.etQuestion.setText(task.getQuestion());

        holder.etQuestion.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                task.setQuestion(holder.etQuestion.getText().toString());
            }
        });

        AnswerOptionsAdapter optionsAdapter = new AnswerOptionsAdapter(task.getOptions());
        holder.rvAnswerOptions.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvAnswerOptions.setAdapter(optionsAdapter);

        holder.btnAddOption.setOnClickListener(v -> {
            optionsAdapter.addOption();
        });

        holder.btnDeleteTask.setOnClickListener(v -> removeTask(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etQuestion;
        RecyclerView rvAnswerOptions;
        Button btnAddOption, btnDeleteTask;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            etQuestion = itemView.findViewById(R.id.et_question);
            rvAnswerOptions = itemView.findViewById(R.id.rv_answer_options);
            btnAddOption = itemView.findViewById(R.id.btn_add_option);
            btnDeleteTask = itemView.findViewById(R.id.btn_delete_task);
        }
    }
}