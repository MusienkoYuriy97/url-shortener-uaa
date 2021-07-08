package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.UserRole;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AdminService {
    private UserRepository userRepository;
    private UserDetailServiceImpl userDetailService;

    @Autowired
    public AdminService(UserRepository userRepository,
                        @Qualifier("userDetailsServiceImpl") UserDetailServiceImpl userDetailService) {
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
    }

    public User updateUserRole(UpdateRoleUserDto dto) {
        String email = dto.getEmail();

        if (!userRepository.existsByEmail(email)){
            log.warn("User with this id does not exist." + "Email:" +dto.getEmail());
            throw new UserDataException("User with this id does not exist");
        }

        User user = userRepository.getByEmail(email).get();
        String userRole = user.getUserRole().name();
        String newRole = dto.getNewRole();

        if (!userRole.equals(newRole)){
            user.setUserRole(UserRole.valueOf(newRole));
            User save = userRepository.save(user);
            log.info("User role was successfully update");
            return save;
        }else {
            log.warn("User already have role " + dto.getNewRole());
            throw new UserDataException("User already have this role");
        }
    }

    public boolean isCurrentAdmin(String email) {
        User currentUser = userDetailService.getCurrentUser();
        return currentUser.getEmail().equals(email);
    }
}