package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

public class AnswerOption {
    @SerializedName("text")
    private String _text;
    @SerializedName("isCorrect")
    private boolean _isCorrect;

    public AnswerOption(String text) {
        _text = text;
        _isCorrect = false;
    }

    public AnswerOption(String text, boolean isCorrect) {
        _text = text;
        _isCorrect = isCorrect;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }

    public boolean isCorrect() {
        return _isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        _isCorrect = isCorrect;
    }
}