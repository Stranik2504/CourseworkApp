package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int _id;
    @SerializedName("name")
    private String _name;
    private boolean _selected;

    public User(int id, String name) {
        _id = id;
        _name = name;
        _selected = false;
    }

    public int getId() { return _id; }
    public String getName() { return _name; }
    public boolean isSelected() { return _selected; }
    public void setSelected(boolean selected) { _selected = selected; }
}