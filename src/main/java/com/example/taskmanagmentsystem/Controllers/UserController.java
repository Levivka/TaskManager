package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Services.Users.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }
    // @RequestMapping("/login")
    // public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
    //     return userServices.login(email, password);
    // }
}
