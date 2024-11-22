package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/make-admin")
    public ResponseEntity<?> makeUserAdmin(@PathVariable int id) {
        return userService.makeUserAdmin(id);
    }
}
