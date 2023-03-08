package io.github.maxixcom.otus.movies.backend.service.token;

import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.security.UserRoleConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = {TokenServiceImpl.class})
class TokenServiceImplTest {
    @Autowired
    TokenServiceImpl tokenService;

    @MockBean
    private JwtConfigProperties jwtConfigProperties;

    @BeforeEach
    void setUp() {
        Mockito.when(jwtConfigProperties.getKey()).thenReturn("c2VjdXJpdHlLZXlGb3JITUFDLVNIQUJhc2U2NEVuY29kZWQ=");
        Mockito.when(jwtConfigProperties.getExpirationSec()).thenReturn(3600L);
        Mockito.when(jwtConfigProperties.getIssuer()).thenReturn("issuer");
        Mockito.when(jwtConfigProperties.getAudience()).thenReturn("audience");
    }

    @Test
    void shouldCreateValidToken() {
        long principalId = 1L;
        Set<GrantedAuthority> authorities = UserRoleConverter.toGrantedAuthority(UserRole.ADMIN);

        String token = tokenService.generateToken(principalId, authorities);

        Assertions.assertThat(token).isNotBlank();

        // extract and check

        TokenData tokenData = tokenService.extractData(token);

        Assertions.assertThat(tokenData.getId()).isEqualTo(principalId);
        Assertions.assertThat(tokenData.getAuthorities()).containsAll(authorities);
    }
}