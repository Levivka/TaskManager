package com.example.taskmanagmentsystem.Models.Dtos;

import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
        private String content;
}
