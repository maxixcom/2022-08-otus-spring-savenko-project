package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileChangePasswordDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileEditDto;
import io.github.maxixcom.otus.movies.backend.mappers.UserMapperImpl;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.UserRoleConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJpaTest
@Import({ProfileServiceImpl.class, UserMapperImpl.class})
class ProfileServiceImplTest {
    @Autowired
    private ProfileServiceImpl profileService;
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
    void setUp() throws Exception {
        User user = userRepository.findById(1L).orElseThrow(() -> new Exception("User not found. Check Test Setup"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getId(), "", UserRoleConverter.toGrantedAuthority(user.getRole())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnProfile() throws Exception {
        User user = userRepository.findById(1L).orElseThrow(() -> new Exception("User not found. Check Test Setup"));

        ProfileDto dto = profileService.getProfile();

        org.junit.jupiter.api.Assertions.assertAll(() -> {
            Assertions.assertThat(dto.getId()).isEqualTo(user.getId());
            Assertions.assertThat(dto.getUsername()).isEqualTo(user.getUsername());
            Assertions.assertThat(dto.getFullName()).isEqualTo(user.getFullName());
        });
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldEditProfile() {
        profileService.editProfile(new ProfileEditDto("new full name"));

        Optional<User> userOptional = userRepository.findById(1L);

        Assertions.assertThat(userOptional).isPresent().hasValueSatisfying((user) -> {
            Assertions.assertThat(user.getFullName()).isEqualTo("new full name");
        });
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldChangePassword() {
        ProfileChangePasswordDto changePasswordDto = new ProfileChangePasswordDto("pass", "new password");
        profileService.changeProfilePassword(changePasswordDto);

        Optional<User> userOptional = userRepository.findById(1L);

        Assertions.assertThat(userOptional).isPresent().hasValueSatisfying((user) -> {
            Assertions.assertThat(passwordEncoder.matches("new password", user.getPassword()))
                    .isTrue();
        });

    }
}