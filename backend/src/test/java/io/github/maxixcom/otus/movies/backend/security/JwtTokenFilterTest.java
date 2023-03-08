package io.github.maxixcom.otus.movies.backend.security;

import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.service.token.TokenData;
import io.github.maxixcom.otus.movies.backend.service.token.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = {JwtTokenFilter.class})
class JwtTokenFilterTest {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @MockBean
    private TokenService tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @Test
    void shouldCallDoFilter() throws ServletException, IOException {
        jwtTokenFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldCreateAuthentication() throws ServletException, IOException {
        long principalId = 1L;
        Set<GrantedAuthority> authorities = UserRoleConverter.toGrantedAuthority(UserRole.ADMIN);

        Mockito.when(tokenService.extractData("token"))
                .thenReturn(new TokenData(principalId, authorities));

        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");

        jwtTokenFilter.doFilter(request, response, filterChain);

        org.junit.jupiter.api.Assertions.assertAll(() -> {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Assertions.assertThat(securityContext).isNotNull();

            Authentication authentication = securityContext.getAuthentication();
            Assertions.assertThat(authentication).isNotNull();
            Assertions.assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);

            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            Assertions.assertThat(authenticationToken.getPrincipal()).isEqualTo(1L);
            Assertions.assertThat(authenticationToken.getCredentials()).isEqualTo("token");
            Assertions.assertThat(authenticationToken.getAuthorities()).containsAll(authorities);
        });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldNotCreateAuthentication() throws ServletException, IOException {
        jwtTokenFilter.doFilter(request, response, filterChain);

        org.junit.jupiter.api.Assertions.assertAll(() -> {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Assertions.assertThat(securityContext).isNotNull();
            Assertions.assertThat(securityContext.getAuthentication()).isNull();
        });
    }
}