package by.solbegsoft.urlshorteneruaa.security;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service("userDetailsServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                user.getUserStatus().equals(UserStatus.ACTIVE),
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().name()))
        );
    }

    public static Optional<String> getCurrentEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    public User getCurrentUser(){
        Optional<String> currentUserEmail = getCurrentEmail();
        if (currentUserEmail.isPresent() & userRepository.existsByEmail(currentUserEmail.get())){
            return userRepository.getByEmail(currentUserEmail.get()).get();
        }
        throw new UserDataException("User doesn't exist");
    }
}