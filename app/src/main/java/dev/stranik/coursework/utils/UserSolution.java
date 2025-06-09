package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSolution {
    @SerializedName("userId")
    private final int _userId;
    @SerializedName("username")
    private final String _username;
    @SerializedName("isAdmin")
    private final boolean _isAdmin;
    @SerializedName("solutions")
    private final List<Boolean> _solutions;

    public UserSolution(int userId, String username, boolean isAdmin, List<Boolean> solutions) {
        _userId = userId;
        _username = username;
        _isAdmin = isAdmin;
        _solutions = solutions;
    }

    public int getUserId() {
        return _userId;
    }

    public String getUsername() {
        return _username;
    }

    public boolean isAdmin() {
        return _isAdmin;
    }

    public List<Boolean> getSolutions() {
        return _solutions;
    }
}
