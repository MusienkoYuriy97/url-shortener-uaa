package by.solbegsoft.urlshorteneruaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "USERS")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @JsonIgnore @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    @JsonIgnore @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore @Override
    public boolean isAccountNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @JsonIgnore @Override
    public boolean isAccountNonLocked() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @JsonIgnore @Override
    public boolean isCredentialsNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @JsonIgnore @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(userStatus);
    }
}