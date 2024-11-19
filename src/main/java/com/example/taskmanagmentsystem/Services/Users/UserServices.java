package com.example.taskmanagmentsystem.Services.Users;

import com.example.taskmanagmentsystem.Models.Role;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.RoleRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        System.out.println("Looking for user with name: " + username);
        return userRepository.findByName(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException(
                String.format("Пользователь с почтой %s не найден", username)
            )
        );
        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public void createUser(User user) {
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }
}
