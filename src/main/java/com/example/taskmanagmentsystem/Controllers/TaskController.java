package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<?> taskCreate(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> taskList() {
        return taskService.listAllTasks();
    }

    @GetMapping("/list/executor/{id}")
    public ResponseEntity<?> executorTaskList(@PathVariable Integer id) {
        return taskService.userTasks(id);
    }

    @GetMapping("/list/author/{id}")
    public ResponseEntity<?> authorTaskList(@PathVariable Integer id) {
        return taskService.userTasks(id.intValue());
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> taskUpdate(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> taskDelete(@PathVariable Integer id) {
        return taskService.deleteTask(id);
    }
}

