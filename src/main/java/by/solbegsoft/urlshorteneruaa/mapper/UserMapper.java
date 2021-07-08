package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserResponseDto;

public interface UserMapper {
    User toUser(UserCreateDto dto);
    UserResponseDto toDto(User user);
}
