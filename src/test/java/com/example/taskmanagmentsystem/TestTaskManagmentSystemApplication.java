package com.example.taskmanagmentsystem;

import org.springframework.boot.SpringApplication;

public class TestTaskManagmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(TaskManagmentSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
