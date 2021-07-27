package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.util.UserRole;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserDetailServiceImpl userDetailService;
    private final UserMapper userMapper;

    @Autowired
    public AdminService(UserRepository userRepository,
                        @Qualifier("userDetailsServiceImpl") UserDetailServiceImpl userDetailService,
                        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
        this.userMapper = userMapper;
    }

    public UserCreateResponse updateUserRole(UpdateRoleRequest updateRoleRequest) {
        User user = getByUuidOrThrowException(UUID.fromString(updateRoleRequest.getUuid()));
        user.setUserRole(UserRole.valueOf(updateRoleRequest.getRole()));
        userRepository.save(user);
        log.info("User role was successfully update");
        return userMapper.toDto(user);
    }

    public boolean isCurrentAdmin(String uuid) {
        if (uuid == null){
            log.warn("User uuid is null");
            throw new UserDataException("Email is null.");
        }
        log.debug("Check admin {}", uuid);
        return userDetailService.getCurrentUser()
                .getUuid().equals(UUID.fromString(uuid));
    }

    private User getByUuidOrThrowException(UUID uuid){
        Optional<User> optionalUser = userRepository.findById(uuid);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            log.warn("User with this uuid doesn't exist. Uuid: {}", uuid);
            throw new UserDataException("User with this uuid doesn't exist");
        }
    }
}