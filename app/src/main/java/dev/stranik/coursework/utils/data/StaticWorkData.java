package dev.stranik.coursework.utils.data;

import android.content.SharedPreferences;

import androidx.core.util.Pair;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dev.stranik.coursework.MainActivity;
import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.User;
import dev.stranik.coursework.utils.UserInfo;
import dev.stranik.coursework.utils.UserSolution;
import kotlin.Triple;

public class StaticWorkData {
    private static final ApiWorkData _workData;
    private static boolean _isAdmin;
    private static int _id;
    private static List<SharedPreferences> _sharedPreferences;

    static {
        _workData = new ApiWorkData();
    }

    public static List<SolutionList> getTasks(int userId) {
        try {
            return _workData.getTasks(userId);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static Pair<Boolean, String> loginUser(String username, String password) {
        try {
            return _workData.loginUser(username, _workData.hashPassword(password));
        }
        catch (IOException ex) {
            return new Pair<>(false, "Ошибка подключения к серверу");
        }
    }

    public static Pair<Boolean, String> registerUser(String username, String password) {
        try {
            return _workData.registerUser(username, _workData.hashPassword(password));
        }
        catch (IOException ex) {
            return new Pair<>(false, "Ошибка подключения к серверу");
        }
    }

    public static Pair<Boolean, UserInfo> getUserInfo() {
        SharedPreferences sharedUser = _sharedPreferences.getLast();

        if (!sharedUser.contains("stat_pass") ||
            !sharedUser.contains("stat_error")
        ) {
            Pair<Boolean, UserInfo> userInfo = null;

            try {
                userInfo = _workData.getUserInfo();
            } catch (IOException e) {
                return new Pair<>(false, new UserInfo("error", -1, -1));
            }

            if (!userInfo.first)
                return userInfo;

            SharedPreferences.Editor editor = sharedUser.edit();
            editor.putString("username", userInfo.second.getUsername());
            editor.putInt("stat_pass", userInfo.second.getStatPass());
            editor.putInt("stat_error", userInfo.second.getStatError());
            editor.apply();

            return userInfo;
        }

        String username = sharedUser.getString("username", "error");
        int pass = sharedUser.getInt("stat_pass", -1);
        int error = sharedUser.getInt("stat_error", -1);
        return new Pair<>(true, new UserInfo(username, pass, error));
    }

    public static List<SolutionList> getSolutionTasks(int userId) {
        try {
            return _workData.getSolutionTasks(userId);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static Pair<Boolean, SolutionInfo> getSolution(int solutionId) {
        if (solutionId == -1) {
            return new Pair<>(true, new SolutionInfo(-1, "", "", 0, 0));
        }

        try {
            return _workData.getSolutionInfo(solutionId);
        } catch (IOException e) {
            return new Pair<>(false, new SolutionInfo(-1, "", "", 0, 0));
        }
    }

    public static Pair<Boolean, List<TaskItem>> getTaskItems(int solutionId) {
        if (solutionId == -1) {
            return new Pair<>(true, Collections.emptyList());
        }

        try {
            return _workData.getTaskItems(solutionId);
        } catch (IOException e) {
            return new Pair<>(false, Collections.emptyList());
        }
    }

    public static boolean submitAnswers(int solutionId, List<Integer> answers) {
        if (solutionId == -1 || answers.isEmpty()) {
            return false;
        }

        clearStats();

        try {
            return _workData.submitAnswers(solutionId, answers);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean deleteTask(int solutionId) {
        if (solutionId == -1) {
            return false;
        }

        clearStats();

        try {
            return _workData.deleteTask(solutionId);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean createTask(SolutionList solutionInfo, List<TaskItem> tasks, List<User> userList) {
        if (solutionInfo == null || tasks == null || tasks.isEmpty()) {
            return false;
        }

        try {
            return _workData.createTask(solutionInfo, tasks, userList);
        }
        catch (IOException e) {
            return false;
        }
    }

    public static Pair<Boolean, List<User>> getUsers() {
        try {
            return _workData.getUsers();
        } catch (IOException e) {
            return new Pair<>(false, Collections.emptyList());
        }
    }

    public static Pair<Boolean, List<UserSolution>> getUsersSolution(int solutionId) {
        if (solutionId == -1) {
            return new Pair<>(true, Collections.emptyList());
        }

        try {
            return _workData.getUsersSolution(solutionId);
        } catch (IOException e) {
            return new Pair<>(false, Collections.emptyList());
        }
    }

    public static boolean isAdmin() {
        return _isAdmin;
    }

    public static void setAdminAndSave(boolean isAdmin) {
        _isAdmin = isAdmin;

        SharedPreferences.Editor editor = _sharedPreferences.getFirst().edit();
        editor.putBoolean("admin", isAdmin);
        editor.apply();
    }

    public static void setAdmin(boolean isAdmin) {
        _isAdmin = isAdmin;
    }

    public static int getId() {
        return _id;
    }

    public static void setId(int id) {
        _id = id;
    }

    public static void setIdAndSave(int id) {
        _id = id;

        SharedPreferences.Editor editor = _sharedPreferences.getLast().edit();
        editor.putInt("userId", id);
        editor.apply();
    }

    public static void setSharedPreferences(List<SharedPreferences> sharedPreferences) {
        _sharedPreferences = sharedPreferences;
    }

    public static void saveToken() {
        _workData.saveToken(_sharedPreferences.getLast());
    }

    public static void loadToken() {
        _workData.loadToken(_sharedPreferences.getLast());
    }

    public static void reset() {
        _isAdmin = false;
        _id = -1;

        SharedPreferences.Editor editor = _sharedPreferences.getFirst().edit();
        editor.remove("admin");
        editor.apply();

        editor = _sharedPreferences.getLast().edit();
        editor.remove("userId");
        editor.apply();

        clearStats();

        _workData.resetToken(_sharedPreferences.getLast());
    }

    private static void clearStats() {
        SharedPreferences sharedUser = _sharedPreferences.getLast();
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.remove("username");
        editor.remove("stat_pass");
        editor.remove("stat_error");
        editor.apply();
    }
}
