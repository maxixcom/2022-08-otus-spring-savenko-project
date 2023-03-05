package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.auth.LoginRequestDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginResponseDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.RegisterUserRequestDto;
import io.github.maxixcom.otus.movies.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/v1/auth/login")
    ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.loginUser(loginRequestDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(loginResponseDto.getToken()))
                .body(loginResponseDto);
    }

    @PostMapping("/api/v1/auth/register")
    ResponseEntity<LoginResponseDto> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        LoginResponseDto loginResponseDto = authService.registerUser(registerUserRequestDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(loginResponseDto.getToken()))
                .body(loginResponseDto);
    }


}
