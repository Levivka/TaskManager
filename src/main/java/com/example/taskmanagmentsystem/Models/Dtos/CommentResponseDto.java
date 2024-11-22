package com.example.taskmanagmentsystem.Models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private int authorId;
    private int taskId;
}