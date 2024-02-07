package com.example.cs2340_firstproject.ui.dashboard;

import java.util.LinkedHashSet;

public class AssignExamItem {
    private String assignName;
    private String dueDate;
    private String className;
    private boolean isSelected;
    private LinkedHashSet<String> daysOfWeek;

    // Constructor
    public AssignExamItem(String courseName, String time, String instructor, LinkedHashSet<String> daysOfWeek) {
        this.assignName = assignName;
        this.dueDate = dueDate;
        this.className = className;
        this.daysOfWeek = daysOfWeek;
    }

    // Getter for courseName
    public String getAssignName() {
        return assignName;
    }

    // Getter for time
    public String getDueDate() {
        return dueDate;
    }

    // Getter for instructor
    public String getClassName() {
        return className;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public LinkedHashSet<String> getDaysOfWeek() {
        return daysOfWeek;
    }
}

