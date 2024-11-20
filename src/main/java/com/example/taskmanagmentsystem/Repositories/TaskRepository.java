package com.example.taskmanagmentsystem.Repositories;

import com.example.taskmanagmentsystem.Models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    Optional<Task> findByTitle(String name);
    List<Task> findByExecutor_Id(Integer id);
    List<Task> findByAuthor_Id(Integer id);
}
