package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("username")
    private String _username;
    @SerializedName("statPass")
    private int _statPass;
    @SerializedName("statError")
    private int _statError;

    public UserInfo(String username, int statPass, int statError) {
        _username = username;
        _statPass = statPass;
        _statError = statError;
    }

    public String getUsername() {
        return _username;
    }

    public int getStatPass() {
        return _statPass;
    }

    public int getStatError() {
        return _statError;
    }
}
