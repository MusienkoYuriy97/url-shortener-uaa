package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.util.UserRole;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {
    private UserRepository userRepository;
    private UserDetailServiceImpl userDetailService;
    private AuthenticationService authenticationService;
    private UserMapper userMapper;

    @Autowired
    public AdminService(UserRepository userRepository,
                        @Qualifier("userDetailsServiceImpl") UserDetailServiceImpl userDetailService,
                        AuthenticationService authenticationService,
                        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    public UserResponseDto updateUserRole(UpdateRoleUserDto updateRoleUserDto, UserRole newRole) {
        User user = authenticationService.getByEmailOrThrowException(updateRoleUserDto.getEmail());

        user.setUserRole(newRole);
        userRepository.save(user);
        log.info("User role was successfully update");
        return userMapper.toDto(user);
    }

    public boolean isCurrentAdmin(String email) {
        if (email == null){
            throw new UserDataException("Email is null.");
        }
        return userDetailService.getCurrentUser()
                .getEmail().equals(email);
    }
}