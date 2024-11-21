package com.example.taskmanagmentsystem.Services;

import com.example.taskmanagmentsystem.Models.Dtos.JwtRequest;
import com.example.taskmanagmentsystem.Models.Dtos.JwtResponse;
import com.example.taskmanagmentsystem.Models.Dtos.UserDto;
import com.example.taskmanagmentsystem.Models.Exceptions.ApplicationError;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Services.Users.UserServices;
import com.example.taskmanagmentsystem.Utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final UserServices userServices;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
//        System.out.println("Received Username: " + authRequest.getUsername());
//        System.out.println("Received Password: " + authRequest.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userServices.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registration(@RequestBody UserDto userDto) {
        if (userServices.findByUsername(userDto.getName()).isPresent()) {
            return new ResponseEntity<>(new ApplicationError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Пользователь с таким именем уже существует"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return userServices.createUser(userDto);
    }
    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    public boolean isExecutor(User user, Task task) {
        return task.getExecutor() != null && task.getExecutor().getId().equals(user.getId());
    }

    public ResponseEntity<String> validateAccess(User user, Task task) {
        if (!isAdmin(user) && !isExecutor(user, task)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на выполнение этого действия.");
        }
        return null;
    }
}
