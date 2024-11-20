package com.example.taskmanagmentsystem.Services.Tasks;

import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Models.Exceptions.ApplicationError;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.TaskRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;
import jakarta.persistence.Entity;
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
public class TaskServices {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> listAllTasks() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> userTasks(Integer executorId) {
        List<Task> tasks = new ArrayList<>(taskRepository.findByExecutor_Id(executorId));
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> userTasks(int authorId) {
        List<Task> tasks = new ArrayList<>(taskRepository.findByAuthor_Id(authorId));
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

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }

    public ResponseEntity<?> updateTask(Integer taskId, TaskDto taskDto) {
        if (taskRepository.existsById(taskId)) {
            Task origin = taskRepository.findById(taskId).orElse(null);

            if (origin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Задача с таким ID не найдена");
            }

            BeanUtils.copyProperties(taskDto, origin, getNullPropertyNames(taskDto));

            if (taskDto.getExecutorId() != null) {
                User executor = userRepository.findById(taskDto.getExecutorId()).orElse(null);
                if (executor == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Исполнитель с таким ID не найден");
                }
                origin.setExecutor(executor);
            }

            if (origin.getAuthor() == null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User author = userRepository.findByName(username).orElse(null);
                if (author == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Автор с таким именем не найден");
                }
                origin.setAuthor(author);
            }

            Task updatedTask = taskRepository.save(origin);

            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задачи с таким ID нету");
        }
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
