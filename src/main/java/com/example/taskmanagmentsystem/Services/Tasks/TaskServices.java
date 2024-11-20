package com.example.taskmanagmentsystem.Services.Tasks;

import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.TaskRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServices {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> listAllTasks() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
//        System.out.println(tasks);
        return tasks;
    }

    public Task createTask(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> authorOptional = userRepository.findByName(currentUsername);

        if (authorOptional.isEmpty()) {
            throw new RuntimeException("Автор не найден");
        }

        User author = authorOptional.get();

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAuthor(author);

        if (taskDto.getExecutorId() != null) {
            userRepository.findById(taskDto.getExecutorId()).ifPresent(task::setExecutor);
        }

        return taskRepository.save(task);
    }
}
