package dev.stranik.coursework.utils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SolutionList {
    @SerializedName("id")
    private int _id;
    @SerializedName("title")
    private String _title;
    @SerializedName("description")
    private String _description;
    @SerializedName("fullDescription")
    private String _fullDescription;
    @SerializedName("solutions")
    private List<Boolean> _solutions;

    public SolutionList(int Id, String title, String description, String fullDescription, List<Boolean> solutions) {
        _id = Id;
        _title = title;
        _description = description;
        _fullDescription = fullDescription;
        _solutions = solutions;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public String getFullDescription() {
        return _fullDescription;
    }

    public List<Boolean> getSolutions() {
        return _solutions;
    }
}
