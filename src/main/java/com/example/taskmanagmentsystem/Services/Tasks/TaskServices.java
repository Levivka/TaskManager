package com.example.taskmanagmentsystem.Services.Tasks;

import com.example.taskmanagmentsystem.Models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskServices {
    private final RestTemplate restTemplate;
    private String baseUrl = "http://localhost:9040/tasks";

    @Autowired
    public TaskServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> createTask(Task task) {
        String endpoint = baseUrl + "/create";
        return restTemplate.postForEntity(endpoint, task, String.class);
    }
}
