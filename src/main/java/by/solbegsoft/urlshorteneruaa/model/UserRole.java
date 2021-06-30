package by.solbegsoft.urlshorteneruaa.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static by.solbegsoft.urlshorteneruaa.model.Permission.*;

public enum UserRole {
    ADMIN(Set.of(LINK_DELETE, LINK_READ, LINK_WRITE)),
    USER(Set.of(LINK_READ, LINK_WRITE));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}