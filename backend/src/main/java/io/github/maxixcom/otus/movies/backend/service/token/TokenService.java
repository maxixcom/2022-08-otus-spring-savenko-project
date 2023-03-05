package io.github.maxixcom.otus.movies.backend.service.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface TokenService {
    String generateToken(Long principalId, Set<GrantedAuthority> authorities);
    TokenData extractData(String token);
}
