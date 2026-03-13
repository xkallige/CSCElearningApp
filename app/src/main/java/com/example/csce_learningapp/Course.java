package com.example.csce_learningapp;

public class Course {
    private String title;
    private String description;
    private String fileUrl;

    public Course() {
        // Required for Firebase
    }

    public Course(String title, String description, String fileUrl) {
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}