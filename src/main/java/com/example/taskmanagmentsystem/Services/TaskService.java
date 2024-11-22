package com.example.taskmanagmentsystem.Services;

import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.TaskRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.beans.FeatureDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ResponseEntity<?> listAllTasks() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Список задач пуст");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> userTasks(Integer executorId) {
        List<Task> tasks = new ArrayList<>(taskRepository.findByExecutor_Id(executorId));
        if (tasks.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Список задач пуст");
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> userTasks(int authorId) {
        List<Task> tasks = new ArrayList<>(taskRepository.findByAuthor_Id(authorId));
        if (tasks.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Список задач пуст");
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> createTask(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> authorOptional = userRepository.findByName(currentUsername);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Автор не найден");
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

        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task));
    }

    public ResponseEntity<?> updateTask(Integer taskId, TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByName(currentUsername)
                .orElse(null);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Пользователь не авторизован.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача с ID " + taskId + " не найдена."));

        authService.validateAccess(currentUser, task);

        if (authService.isAdmin(currentUser)) {
            updateTaskForAdmin(task, taskDto);
        } else {
            updateTaskForUser(task, taskDto);
        }

        taskRepository.save(task);
        return ResponseEntity.ok("Задача успешно обновлена.");
    }

    public void updateTaskForUser(Task task, TaskDto taskDto) {
        if (taskDto.getExecutorId() != null) {
            User executor = userRepository.findById(taskDto.getExecutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Исполнитель с ID " + taskDto.getExecutorId() + " не найден."));
            task.setExecutor(executor);
        }
        if (taskDto.getComments() != null && !taskDto.getComments().isEmpty()) {
            task.setComments(taskDto.getComments());
        }
    }

    public void updateTaskForAdmin(Task task, TaskDto taskDto) {
        BeanUtils.copyProperties(taskDto, task, getNullPropertyNames(taskDto));
        if (taskDto.getExecutorId() != null) {
            User executor = userRepository.findById(taskDto.getExecutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Исполнитель с ID " + taskDto.getExecutorId() + " не найден."));
            task.setExecutor(executor);
        }
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }

    public ResponseEntity<?> deleteTask(Integer taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.ok("Задача удалена");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не найдена");
        }
    }

}
