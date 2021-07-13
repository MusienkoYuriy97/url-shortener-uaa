package by.solbegsoft.urlshorteneruaa.model;

import by.solbegsoft.urlshorteneruaa.util.UserRole;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Builder
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "uuid", nullable = false)
    @GeneratedValue
    private UUID uuid;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(userStatus);
    }
}