package dev.stranik.coursework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.User;
import dev.stranik.coursework.utils.UserAdapter;
import dev.stranik.coursework.utils.data.StaticWorkData;

public class ChooseUserFragment extends Fragment {
    private final SolutionList _solutionInfo;
    private final List<TaskItem> _tasks;

    private List<User> userList = new ArrayList<>();

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ChooseUserFragment(SolutionList solutionInfo, List<TaskItem> tasks) {
        _solutionInfo = solutionInfo;
        _tasks = tasks;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_user, container, false);

        executor.execute(() -> {
            Pair<Boolean, List<User>> userData = StaticWorkData.getUsers();

            mainHandler.post(() -> {
                if (!userData.first) {
                    Toast.makeText(getContext(), "Ошибка загрузки пользователей", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new TaskListFragment())
                            .addToBackStack(null)
                            .commit();
                    return;
                }

                userList = userData.second;

                RecyclerView rvUsers = view.findViewById(R.id.rv_users);

                UserAdapter adapter = new UserAdapter(new ArrayList<>(userList));
                rvUsers.setAdapter(adapter);
                rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

                EditText etSearch = view.findViewById(R.id.et_search_user);
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<User> filtered = userList.stream()
                                .filter(u -> u.getName().toLowerCase().contains(s.toString().toLowerCase()))
                                .collect(Collectors.toList());
                        adapter.updateList(filtered);
                    }
                });
            });
        });

        Button btnAddTask = view.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener(v -> {
            List<User> selectedUsers = userList.stream()
                    .filter(User::isSelected)
                    .collect(Collectors.toList());

            if (selectedUsers.isEmpty()) {
                Toast.makeText(getContext(), "Выберите хотя бы одного пользователя", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedUsers.size() == userList.size())
                selectedUsers = new ArrayList<>();

            List<User> finalSelectedUsers = selectedUsers;

            executor.execute(() -> {
                boolean result = StaticWorkData.createTask(_solutionInfo, _tasks, finalSelectedUsers);

                mainHandler.post(() -> {
                    if (result) {
                        Toast.makeText(getContext(), "Задача успешно создана", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new TaskListFragment())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Ошибка при создании задачи", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return view;
    }
}