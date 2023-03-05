package io.github.maxixcom.otus.movies.backend.security;

import io.github.maxixcom.otus.movies.backend.domain.UserPermission;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class UserRoleConverter {
    public static Set<GrantedAuthority> toGrantedAuthority(UserRole userRole) {
        Set<GrantedAuthority> authorities = userRole.getPermissions().stream()
                .map(UserPermission::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.name()));

        return authorities;
    }
}
