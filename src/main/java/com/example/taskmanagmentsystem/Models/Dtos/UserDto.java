package com.example.taskmanagmentsystem.Models.Dtos;

import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String email;
    private String password;
    private String role;

    public UserDto(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
