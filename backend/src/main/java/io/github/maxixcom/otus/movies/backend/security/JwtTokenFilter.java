package io.github.maxixcom.otus.movies.backend.security;


import io.github.maxixcom.otus.movies.backend.service.token.TokenData;
import io.github.maxixcom.otus.movies.backend.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                    .filter(header -> header.startsWith("Bearer "))
                    .map(header -> header.replace("Bearer ", ""))
                    .ifPresent(token -> {
                        TokenData tokenData = tokenService.extractData(token);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                tokenData.getId(),
                                token,
                                tokenData.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        } catch (Exception e) {
            log.error("Token is wrong: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
