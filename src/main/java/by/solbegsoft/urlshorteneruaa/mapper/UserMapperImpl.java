package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User toUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setEmail(userCreateRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        return user;
    }

    @Override
    public UserCreateResponse toDto(User user) {
        UserCreateResponse dto = new UserCreateResponse();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserStatus(user.getUserStatus());
        dto.setUserRole(user.getUserRole());
        return dto;
    }
}