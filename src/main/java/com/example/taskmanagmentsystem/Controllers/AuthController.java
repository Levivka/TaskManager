package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Models.Dtos.JwtRequest;
import com.example.taskmanagmentsystem.Models.Dtos.UserDto;
import com.example.taskmanagmentsystem.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDto userDto) {
        return authService.registration(userDto);
    }
}
