package io.github.maxixcom.otus.movies.backend.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDto {
    private final String token;
}
