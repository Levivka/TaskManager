package com.example.taskmanagmentsystem.Models;

import lombok.Data;

@Data
public class Task {
    private String id;
    private String title;
    private String description;
    private Enums.Priority priority;
    private Enums.Status status;
    private String[] comments;
    private User owner;
    private User executor;

}
