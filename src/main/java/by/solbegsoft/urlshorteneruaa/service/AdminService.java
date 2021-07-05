package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.UserRole;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AdminService {
    UserRepository userRepository;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminService(UserRepository userRepository,
                        JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User updateUserRole(String token, UpdateRoleUserDto dto) {
        long userId = dto.getUserId();
        if (!userRepository.existsById(userId)){
            log.warn("User with this id does not exist." + "Id:" +dto.getUserId());
            throw new UserDataException("User with this id does not exist");
        }

        String currentAdminEmail = jwtTokenProvider.getEmail(token);
        User currentAdmin = userRepository.getByEmail(currentAdminEmail).get();

        User user = userRepository.getById(userId);
        String userRole = user.getUserRole().name();
        String newRole = dto.getNewRole();
        boolean userNotCurrentAdmin = currentAdmin.getId() != userId;

        if (userNotCurrentAdmin & !userRole.equals(newRole)){
            user.setUserRole(UserRole.valueOf(newRole));
            log.info("User role was successfully update");
            userRepository.save(user);
            return user;
        }else {
            log.warn("User already have role " + dto.getNewRole());
            throw new UserDataException("User already have this role");
        }
    }
}
