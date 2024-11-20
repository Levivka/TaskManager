package com.example.taskmanagmentsystem.Repositories;

import com.example.taskmanagmentsystem.Models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    Optional<Task> findByTitle(String name);
}
