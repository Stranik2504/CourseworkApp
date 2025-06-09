package dev.stranik.coursework.utils.data;

import android.content.SharedPreferences;

import androidx.core.util.Pair;

import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.User;
import dev.stranik.coursework.utils.UserInfo;
import dev.stranik.coursework.utils.UserSolution;
import kotlin.Triple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.stranik.coursework.utils.SolutionList;


public abstract class WorkData {
    protected String _token;

    public abstract List<SolutionList> getTasks(int userId) throws IOException;
    public abstract List<SolutionList> getSolutionTasks(int userId) throws IOException;
    public abstract Pair<Boolean, String> loginUser(String username, String password) throws IOException;
    public abstract Pair<Boolean, String> registerUser(String username, String password) throws IOException;
    public abstract Pair<Boolean, UserInfo> getUserInfo() throws IOException;
    public abstract Pair<Boolean, SolutionInfo> getSolutionInfo(int solutionId) throws IOException;
    public abstract Pair<Boolean, List<User>> getUsers() throws IOException;
    public abstract Pair<Boolean, List<TaskItem>> getTaskItems(int solutionId) throws IOException;
    public abstract Pair<Boolean, List<UserSolution>> getUsersSolution(int solutionId) throws IOException;
    public abstract Boolean submitAnswers(int solutionId, List<Integer> answers) throws IOException;
    public abstract Boolean deleteTask(int solutionId) throws IOException;
    public abstract Boolean createTask(SolutionList solutionInfo, List<TaskItem> tasks, List<User> userList) throws IOException;
    public abstract String hashPassword(String password);

    public void saveToken(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", _token);
        editor.apply();
    }

    public void loadToken(SharedPreferences preferences) {
        _token = preferences.getString("token", null);
    }

    public void resetToken(SharedPreferences preferences) {
        _token = null;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();
    }
}
