package com.example.taskmanagmentsystem.Repositories;

import com.example.taskmanagmentsystem.Models.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    Optional<List<Comment>> findAllByTask(int taskId);
}
