package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

public class SolutionInfo {
    @SerializedName("id")
    private final int _solutionId;
    @SerializedName("title")
    private final String _title;
    @SerializedName("description")
    private final String _description;
    @SerializedName("countAttempts")
    private final int _countAttempts;
    @SerializedName("countUserAttempts")
    private final int _countUserAttempts;

    public SolutionInfo(int solutionId, String title, String description, int countAttempts, int countUserAttempts) {
        _solutionId = solutionId;
        _title = title;
        _description = description;
        _countAttempts = countAttempts;
        _countUserAttempts = countUserAttempts;
    }

    public int getSolutionId() {
        return _solutionId;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public int getCountAttempts() {
        return _countAttempts;
    }

    public int getCountUserAttempts() {
        return _countUserAttempts;
    }
}
