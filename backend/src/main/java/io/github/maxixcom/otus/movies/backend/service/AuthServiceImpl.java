package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginRequestDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginResponseDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.RegisterUserRequestDto;
import io.github.maxixcom.otus.movies.backend.exception.UsernameAlreadyExistsException;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.UserRoleConverter;
import io.github.maxixcom.otus.movies.backend.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public LoginResponseDto loginUser(LoginRequestDto authRequestDto) {
        Optional<User> optionalUser = userRepository.findByUsername(authRequestDto.getUsername());
        String token = optionalUser
                .filter(user -> passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword()))
                .map(user -> tokenService.generateToken(user.getId(), UserRoleConverter.toGrantedAuthority(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found or wrong password"));

        return new LoginResponseDto(token);
    }

    @Override
    public LoginResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setUsername(registerUserRequestDto.getUsername());
        user.setFullName(registerUserRequestDto.getFullName());
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        String token = tokenService.generateToken(user.getId(), UserRoleConverter.toGrantedAuthority(user.getRole()));

        return new LoginResponseDto(token);
    }


}
