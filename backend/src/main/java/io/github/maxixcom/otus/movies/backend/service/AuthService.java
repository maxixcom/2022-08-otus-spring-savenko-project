package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.auth.LoginRequestDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginResponseDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.RegisterUserRequestDto;

public interface AuthService {
    LoginResponseDto loginUser(LoginRequestDto authRequestDto);

    LoginResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto);
}
