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
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminService(UserRepository userRepository,
                        JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User updateUserRole(UpdateRoleUserDto dto) {
//        long userId = dto.getUserId();
//        if (!userRepository.existsById(userId)){
//            log.warn("User with this id does not exist." + "Id:" +userId);
//            throw new UserDataException("User with this id does not exist");
//        }
//
//        User user = userRepository.getById(userId);
//        String userRole = user.getUserRole().name();
//        String newRole = dto.getNewRole();
//
//        if (!userRole.equals(newRole)){
//            user.setUserRole(UserRole.valueOf(newRole));
//            userRepository.save(user);
//            log.info("User role was successfully update");
//            return user;
//        }else {
//            log.warn("User already have role " + dto.getNewRole());
//            throw new UserDataException("User already have this role");
//        }
        return null;
    }
}