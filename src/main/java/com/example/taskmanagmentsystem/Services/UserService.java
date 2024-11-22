package com.example.taskmanagmentsystem.Services;

import com.example.taskmanagmentsystem.Models.Dtos.UserDto;
import com.example.taskmanagmentsystem.Models.Role;
import com.example.taskmanagmentsystem.Models.User;
import com.example.taskmanagmentsystem.Repositories.RoleRepository;
import com.example.taskmanagmentsystem.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public Optional<User> findByUsername(String username) {
//        System.out.println("Looking for user with name: " + username);
        return userRepository.findByName(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException(
                String.format("Пользователь с именем %s не найден", username)
            )
        );
//        System.out.println("Загружен пользователь " + user.getName());
        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public ResponseEntity<User> createUser(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(List.of(roleService.getUserRole()));
//        System.out.println("Created user " + user);

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> deleteUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
        userRepository.delete(userOptional.get());
        return ResponseEntity.ok("Пользователь успешно удалён");
    }

    public ResponseEntity<?> makeUserAdmin(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByName(currentUsername)
                .orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не найден");
        }

        Optional<Role> adminRoleOpt = roleRepository.findByName("ROLE_ADMIN");
        if (adminRoleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Роль ADMIN не найдена в системе");
        }

        Role adminRole = adminRoleOpt.get();

        if (!currentUser.getRoles().contains(adminRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно прав для изменения ролей");
        }

        User targetUser = userRepository.findById(userId)
                .orElse(null);

        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        if (targetUser.getRoles().contains(adminRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь уже является администратором");
        }

        targetUser.getRoles().add(adminRole);
        userRepository.save(targetUser);

        return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно назначен администратором");
    }

}
