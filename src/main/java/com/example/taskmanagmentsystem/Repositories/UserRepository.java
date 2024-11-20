package com.example.taskmanagmentsystem.Repositories;

import com.example.taskmanagmentsystem.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByName(String name);
    Optional<User> findById(int id);
}

