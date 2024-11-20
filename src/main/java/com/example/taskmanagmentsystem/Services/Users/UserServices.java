package com.example.taskmanagmentsystem.Services.Users;

import com.example.taskmanagmentsystem.Models.Dtos.UserDto;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.taskmanagmentsystem.Services.RoleServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final RoleServices roleServices;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> findByUsername(String username) {
//        System.out.println("Looking for user with name: " + username);
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
        System.out.println("Загружен пользователь " + user.getName());
        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public void createUser(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(List.of(roleServices.getUserRole()));
        System.out.println("Created user " + user);

        userRepository.save(user);
    }
}
