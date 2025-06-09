package dev.stranik.coursework;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.TasksAdapter;

public class CreateTaskFragment extends Fragment {
    private List<TaskItem> tasks = new ArrayList<>();
    private TasksAdapter tasksAdapter;

    public CreateTaskFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.layout_tasks);
        Button btnAddTask = view.findViewById(R.id.BtnAddTask);
        Button next = view.findViewById(R.id.BtnNext);

        tasksAdapter = new TasksAdapter(tasks, () -> tasksAdapter.notifyDataSetChanged());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tasksAdapter);

        btnAddTask.setOnClickListener(v -> tasksAdapter.addTask());
        next.setOnClickListener(v -> {
            EditText etTitle = view.findViewById(R.id.ETTitle);
            EditText etDescription = view.findViewById(R.id.ETDescription);
            EditText etFullDescription = view.findViewById(R.id.ETFullDescription);

            if (etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() || etFullDescription.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getContext(), "Задание заполнено", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ChooseUserFragment(
                        new SolutionList(
                            -1,
                            etTitle.getText().toString(),
                            etDescription.getText().toString(),
                            etFullDescription.getText().toString(),
                            List.of()
                        ),
                        tasks
                    ))
                    .addToBackStack(null)
                    .commit();
        });
    }
}