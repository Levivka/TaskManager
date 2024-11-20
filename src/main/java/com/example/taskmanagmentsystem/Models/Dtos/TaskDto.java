package com.example.taskmanagmentsystem.Models.Dtos;

import com.example.taskmanagmentsystem.Models.Enums;
import com.example.taskmanagmentsystem.Models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String title;
    private String description;
    private Enums.Status status;
    private Enums.Priority priority;
    private Integer executorId;
}
