package com.example.taskmanagmentsystem.Services;

import com.example.taskmanagmentsystem.Models.Comment;
import com.example.taskmanagmentsystem.Models.Dtos.CommentDto;
import com.example.taskmanagmentsystem.Models.Dtos.CommentResponseDto;
import com.example.taskmanagmentsystem.Models.Task;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.CommentRepository;
import com.example.taskmanagmentsystem.Repositories.TaskRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServices {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> listAllComments(int taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не найдена");
        }

        List<Comment> comments = commentRepository.findAllByTask(taskId).orElse(new ArrayList<>());
        if (comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Список комментариев пуст");
        }

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor(),
                        comment.getTask()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }

    public ResponseEntity<?> createComment(int taskId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не найдена");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        User author = userRepository.findByName(authentication.getName()).orElse(null);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь не найден");
        }

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setTaskRelation(List.of(task));
        comment.setTask(task.getId());
        comment.setAuthor(author.getId());

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    public ResponseEntity<?> updateComment(int taskId, int commentId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElse(null);
         if (task == null) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не найдена");
         }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Комментарий не найден");
        }

        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    public ResponseEntity<?> deleteComment(int taskId, int commentId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Задача не найдена");
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Комментарий не найден");
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).body("Комментарий успешно удален");
    }
}
