package com.example.taskmanagmentsystem.Controllers;

import com.example.taskmanagmentsystem.Models.Dtos.CommentDto;
import com.example.taskmanagmentsystem.Models.Dtos.TaskDto;
import com.example.taskmanagmentsystem.Services.CommentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentServices commentServices;

    @GetMapping("/list")
    public ResponseEntity<?> commentList(@PathVariable int taskId) {
        return commentServices.listAllComments(taskId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> commentCreate(@PathVariable int taskId, @RequestBody CommentDto commentDto) {
        return commentServices.createComment(taskId, commentDto);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> commentUpdate(@PathVariable int taskId, @PathVariable int id, @RequestBody CommentDto commentDto) {
        return commentServices.updateComment(taskId, id, commentDto);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> commentDelete(@PathVariable int taskId, @PathVariable int id) {
        return commentServices.deleteComment(taskId, id);
    }
}
