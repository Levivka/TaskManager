package com.example.taskmanagmentsystem.Models.Dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
