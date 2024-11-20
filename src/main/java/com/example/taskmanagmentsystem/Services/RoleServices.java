package com.example.taskmanagmentsystem.Services;

import com.example.taskmanagmentsystem.Models.Role;
import com.example.taskmanagmentsystem.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServices {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_EMPLOYEE").get();
    }
}
