package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TaskItem {
    @SerializedName("question")
    private String _question;
    @SerializedName("options")
    private List<AnswerOption> _options;

    public TaskItem(String question) {
        _question = question;
        _options = new ArrayList<>();
    }

    public TaskItem(String question, List<AnswerOption> options) {
        _question = question;
        _options= options;
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String question) {
        _question = question;
    }

    public List<AnswerOption> getOptions() {
        return _options;
    }

    public void setOptions(List<AnswerOption> options) {
        _options = options;
    }
}