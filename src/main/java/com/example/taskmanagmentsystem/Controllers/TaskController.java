package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Services.Tasks.TaskServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskServices taskServices;

    @PostMapping("/create")
    public ResponseEntity<?> taskCreate(@RequestBody TaskDto taskDto) {
        return taskServices.createTask(taskDto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> taskList() {
        return taskServices.listAllTasks();
    }

    @GetMapping("/list/executor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> executorTaskList(@PathVariable Integer id) {
        return taskServices.userTasks(id);
    }

    @GetMapping("/list/author/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> authorTaskList(@PathVariable Integer id) {
        return taskServices.userTasks(id.intValue());
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> taskUpdate(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        return taskServices.updateTask(id, taskDto);
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> taskDelete(@PathVariable Integer id) {
        return taskServices.deleteTask(id);
    }
}

