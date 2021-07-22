package by.solbegsoft.urlshorteneruaa.model;

import by.solbegsoft.urlshorteneruaa.util.UserRole;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Schema(name = "User(Unuseful in Controller)")
@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "uuid", nullable = false)
    @GeneratedValue
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid;
    @Column(name = "first_name", nullable = false)
    @Schema(example = "Yuriy")
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @Schema(example = "Musienko")
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    @Schema(example = "97musienko@gmail.com")
    private String email;
    @Column(name = "password", nullable = false)
    @Schema(example = "1111")
    private String password;
    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Schema(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    @Schema(hidden = true)
    @Override
    public String getUsername() {
        return email;
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonLocked() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Schema(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Schema(hidden = true)
    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(userStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                userStatus == user.userStatus &&
                userRole == user.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, userStatus, userRole);
    }
}