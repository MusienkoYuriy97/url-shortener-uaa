package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;

public interface UserMapper {
    User toUser(UserCreateRequest dto);
    UserCreateResponse toDto(User user);
}