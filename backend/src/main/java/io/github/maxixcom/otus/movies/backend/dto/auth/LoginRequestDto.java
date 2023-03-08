package io.github.maxixcom.otus.movies.backend.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class LoginRequestDto {
    @NotBlank
    @Length(max = 150)
    private final String username;
    @NotBlank
    @Length(max = 150)
    private final String password;
}
