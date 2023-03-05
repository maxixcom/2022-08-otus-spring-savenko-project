package io.github.maxixcom.otus.movies.backend.service.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class TokenData {
    private final Long id;
    private final Set<GrantedAuthority> authorities;
}
