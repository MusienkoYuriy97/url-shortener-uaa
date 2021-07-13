package by.solbegsoft.urlshorteneruaa.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_USER;

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> permissions = new HashSet<>();
        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}