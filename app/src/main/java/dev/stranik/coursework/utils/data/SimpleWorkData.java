package dev.stranik.coursework.utils.data;

import androidx.core.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dev.stranik.coursework.utils.AnswerOption;
import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.User;
import dev.stranik.coursework.utils.UserInfo;
import dev.stranik.coursework.utils.UserSolution;
import kotlin.Triple;

public class SimpleWorkData extends WorkData {
    @Override
    public List<SolutionList> getTasks(int userId) throws IOException {
        if (userId == -1) {
            return Arrays.asList(
                new SolutionList(
                        1,
                        "Task 1",
                        "Test view not user task",
                        null,
                        null
                )
            );
        }

        return Arrays.asList(
                new SolutionList(
                        1,
                        "Task 1",
                        "Description of Task 1",
                        null,
                        Collections.emptyList()
                ),
                new SolutionList(
                        2,
                        "Task 2",
                        "Description of Task 2",
                        null,
                        Collections.emptyList()
                ),
                new SolutionList(
                        3,
                        "Task 3",
                        "Description of Task 3",
                        null,
                        Collections.emptyList()
                )
        );
    }

    @Override
    public List<SolutionList> getSolutionTasks(int userId) throws IOException {
        if (userId == -1) {
            return Arrays.asList(
                new SolutionList(
                        1,
                        "Task 1",
                        "Test view not user task",
                        null,
                        null
                )
            );
        }

        return Arrays.asList(
                new SolutionList(
                        1,
                        "Task 1",
                        "Solution for Task 1",
                        null,
                        List.of(true, true, false, true, true)
                ),
                new SolutionList(
                        2,
                        "Task 2",
                        "Solution for Task 2",
                        null,
                        List.of(false, false, true)
                )
        );
    }

    @Override
    public Pair<Boolean, String> loginUser(String username, String password) throws IOException {
        if (username.equals("2") && password.equals("2")) {
            StaticWorkData.setAdminAndSave(true);
            StaticWorkData.setIdAndSave(1);

            _token = "1";

            return new Pair<>(true, "Login successful");
        } else if (username.equals("1") && password.equals("1")) {
            StaticWorkData.setAdminAndSave(false);
            StaticWorkData.setIdAndSave(2);

            _token = "2";

            return new Pair<>(true, "Login successful");
        } else {
            return new Pair<>(false, "Invalid username or password");
        }
    }

    @Override
    public Pair<Boolean, String> registerUser(String username, String password) throws IOException {
        if (username.isEmpty() || password.isEmpty()) {
            return new Pair<>(false, "Username and password cannot be empty");
        }

        if (username.equals("1") || username.equals("2")) {
            return new Pair<>(false, "Username already exists");
        }

        StaticWorkData.setAdminAndSave(false);
        StaticWorkData.setIdAndSave(3);
        _token = "3";

        return new Pair<>(true, "Registration successful");
    }

    @Override
    public Pair<Boolean, UserInfo> getUserInfo() throws IOException {
        if (_token == null || _token.isEmpty() || StaticWorkData.getId() == -1) {
            return new Pair<>(false, new UserInfo("", -1, -1));
        }

        if (_token.equals("1")) {
        return new Pair<>(true, new UserInfo("Admin", 100, 0));
        } else if (_token.equals("2")) {
            return new Pair<>(true, new UserInfo("User", 50, 50));
        } else {
            return new Pair<>(true, new UserInfo("User 2", 60, 25));
        }
    }

    @Override
    public Pair<Boolean, SolutionInfo> getSolutionInfo(int solutionId) throws IOException {
        if (solutionId == -1) {
            return new Pair<>(true, new SolutionInfo(-1, "", "", 0, 0));
        }

        if (solutionId == 1) {
            return new Pair<>(true, new SolutionInfo(1, "Task 1", "Подробное описать задания. Я хз что тут писать", 5, 3));
        } else if (solutionId == 2) {
            return new Pair<>(true, new SolutionInfo(2, "Task 2", "Solution for Task 2", 3, 1));
        } else {
            return new Pair<>(false, new SolutionInfo(-1, "", "", 0, 0));
        }
    }

    @Override
    public Pair<Boolean, List<User>> getUsers() throws IOException {
        List<User> users = Arrays.asList(
                new User(1, "Иван Иванов"),
                new User(2, "Петр Петров"),
                new User(3, "Сергей Сергеев"),
                new User(4, "Анна Смирнова")
        );

        return new Pair<>(true, users);
    }

    @Override
    public Pair<Boolean, List<TaskItem>> getTaskItems(int solutionId) throws IOException {
        if (solutionId == -1) {
            return new Pair<>(true, Collections.emptyList());
        }

        if (solutionId == 1) {
            return new Pair<>(true, Arrays.asList(
                    new TaskItem(
                        "Кто такие фиксики?",
                        List.of(
                            new AnswerOption("Маленькие человечки"),
                            new AnswerOption("Большие человечки"),
                            new AnswerOption("Маленькие человечки, которые живут на улице")
                        )
                    ),
                    new TaskItem(
                        "Какой цвет у фиксиков?",
                        List.of(
                            new AnswerOption("Красный"),
                            new AnswerOption("Синий"),
                            new AnswerOption("Желтый"),
                            new AnswerOption("Все перечисленные")
                        )
                    )
            ));
        } else if (solutionId == 2) {
            return new Pair<>(true, Arrays.asList(
                    new TaskItem(
                        "Что такое решение?",
                        List.of(
                            new AnswerOption("Ответ на задачу"),
                            new AnswerOption("Способ решения задачи"),
                            new AnswerOption("Ничего из перечисленного")
                        )
                    ),
                    new TaskItem(
                        "Какое решение лучше?",
                        List.of(
                            new AnswerOption("Быстрое"),
                            new AnswerOption("Эффективное"),
                            new AnswerOption("Оба варианта хороши")
                        )
                    )
            ));
        } else {
            return new Pair<>(true, Arrays.asList(
                    new TaskItem(
                            "Что такое решение?",
                            List.of(
                                    new AnswerOption("Ответ на задачу"),
                                    new AnswerOption("Способ решения задачи"),
                                    new AnswerOption("Ничего из перечисленного")
                            )
                    ),
                    new TaskItem(
                            "Какое решение лучше?",
                            List.of(
                                    new AnswerOption("Быстрое"),
                                    new AnswerOption("Эффективное"),
                                    new AnswerOption("Оба варианта хороши")
                            )
                    )
            ));
        }
    }

    @Override
    public Pair<Boolean, List<UserSolution>> getUsersSolution(int solutionId) throws IOException {
        if (solutionId == -1) {
            return new Pair<>(true, Collections.emptyList());
        }

        if (solutionId == 1) {
            return new Pair<>(true, Arrays.asList(
                    new UserSolution(1, "Иван Иванов", true, List.of(true, false)),
                    new UserSolution(2, "Петр Петров", false, List.of(true, true)),
                    new UserSolution(3, "Сергей Сергеев", false, List.of(false, true))
            ));
        } else if (solutionId == 2) {
            return new Pair<>(true, Arrays.asList(
                    new UserSolution(1, "Иван Иванов", true, List.of(true)),
                    new UserSolution(2, "Петр Петров", false, List.of(false))
            ));
        } else {
            return new Pair<>(false, Collections.emptyList());
        }
    }

    @Override
    public Boolean submitAnswers(int solutionId, List<Integer> answers) throws IOException {
        return true;
    }

    @Override
    public Boolean deleteTask(int solutionId) throws IOException {
        return true;
    }

    @Override
    public Boolean createTask(SolutionList solutionInfo, List<TaskItem> tasks, List<User> userList) throws IOException {
        return true;
    }

    @Override
    public String hashPassword(String password) {
        return password;
    }
}
