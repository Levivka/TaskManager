package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Models.Dtos.JwtRequest;
import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Models.Dtos.UserDto;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Services.AuthServices;
import com.example.taskmanagmentsystem.Services.Tasks.TaskServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskServices taskServices;

    @PostMapping("/create")
    public ResponseEntity<?> taskCreate(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskServices.createTask(taskDto));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> taskList() {
//        System.out.println("tasks /list " + tasks);
        return ResponseEntity.ok(taskServices.listAllTasks());
    }
}

