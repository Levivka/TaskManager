package com.example.taskmanagmentsystem.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToMany()
    @JoinTable(
            name = "task_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> taskRelation;

    @Column(name = "task_id", nullable = false)
    private int task;

    @Column(name = "author_id", nullable = false)
    private int author;
}
