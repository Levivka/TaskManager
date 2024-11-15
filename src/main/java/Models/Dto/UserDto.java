package Models.Dto;

import Models.Enums;
import Models.User;

public class UserDto extends User {
    public UserDto(String name, String email, String password, String role) {
        super.setName(name);
        super.setEmail(email);
        super.setPassword(password);
        super.setRole(new Enums().fromString(role, Enums.Role.class));
    }
}
