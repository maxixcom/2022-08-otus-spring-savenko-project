package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginRequestDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginResponseDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.RegisterUserRequestDto;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.service.token.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJpaTest
@Import({AuthServiceImpl.class})
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    public static class NestedConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    }

    @BeforeEach
    void setUp() {
        Mockito.when(tokenService.generateToken(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn("token");
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldLoginUser() {
        LoginResponseDto responseDto = authService.loginUser(new LoginRequestDto("admin", "pass"));

        Assertions.assertThat(responseDto.getToken()).isEqualTo("token");
    }

    @Test
    void shouldLoginFailed() {
        Assertions.assertThatThrownBy(() -> {
            authService.loginUser(new LoginRequestDto("some user", "some pass"));
        }).isInstanceOf(UsernameNotFoundException.class);
    }

    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldRegisterUser() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("john.doe", "pass", "John Doe");
        LoginResponseDto responseDto = authService.registerUser(requestDto);

        Optional<User> optionalUser = userRepository.findByUsername(requestDto.getUsername());

        Assertions.assertThat(responseDto.getToken()).isEqualTo("token");
        Assertions.assertThat(optionalUser).isPresent().hasValueSatisfying(user -> {
            Assertions.assertThat(user.getFullName()).isEqualTo(requestDto.getFullName());
            Assertions.assertThat(user.getRole()).isEqualTo(UserRole.USER);
            Assertions.assertThat(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).isTrue();
        });
    }
}