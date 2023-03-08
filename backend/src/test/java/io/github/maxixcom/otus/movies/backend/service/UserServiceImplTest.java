package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.dto.user.UserCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserEditDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListRequestDto;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapperImpl;
import io.github.maxixcom.otus.movies.backend.mappers.UserMapperImpl;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.UserRoleConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@Import({UserServiceImpl.class, UserMapperImpl.class, PageMapperImpl.class})
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
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

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnUser() {
        UserDto expected = new UserDto(1L, "user", UserRole.USER, "User");

        Assertions.assertThat(userService.getUser(1))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnListOfUsers() {
        UserListDto listDto = userService.getUsers(new UserListRequestDto(), PageRequest.of(0, 10));
        Assertions.assertThat(listDto.getItems().size()).isGreaterThanOrEqualTo(2);
    }

    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldCreateUser() {
        UserCreateDto createDto = new UserCreateDto("john.doe", UserRole.ADMIN, "John Doe", "pass");

        long userId = userService.createUser(createDto);

        Optional<User> userOptional = userRepository.findById(userId);

        Assertions.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    Assertions.assertThat(user.getUsername()).isEqualTo(createDto.getUsername());
                    Assertions.assertThat(user.getRole()).isEqualTo(createDto.getRole());
                    Assertions.assertThat(user.getFullName()).isEqualTo(createDto.getFullName());
                    Assertions.assertThat(passwordEncoder.matches(createDto.getPassword(), user.getPassword())).isTrue();
                });

    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldEditUser() {
        UserEditDto editDto = new UserEditDto("john.doe", UserRole.ADMIN, "John Doe");

        userService.editUser(1L, editDto);

        Optional<User> userOptional = userRepository.findById(1L);

        Assertions.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    Assertions.assertThat(user.getUsername()).isEqualTo(editDto.getUsername());
                    Assertions.assertThat(user.getRole()).isEqualTo(editDto.getRole());
                    Assertions.assertThat(user.getFullName()).isEqualTo(editDto.getFullName());
                });
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldDeleteUser() throws Exception {
        User user = userRepository.findById(2L).orElseThrow(() -> new Exception("User not found. Check Test Setup"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getId(), "", UserRoleConverter.toGrantedAuthority(user.getRole())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        userService.deleteUser(Set.of(1L));

        Optional<User> userOptional = userRepository.findById(1L);

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Sql(scripts = "/sql/user/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldNotDeleteOneself() throws Exception {
        User user = userRepository.findById(2L).orElseThrow(() -> new Exception("User not found. Check Test Setup"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getId(), "", UserRoleConverter.toGrantedAuthority(user.getRole())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        userService.deleteUser(Set.of(user.getId()));

        Optional<User> userOptional = userRepository.findById(user.getId());

        Assertions.assertThat(userOptional).isPresent();
    }

}