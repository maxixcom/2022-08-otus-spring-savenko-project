package io.github.maxixcom.otus.movies.backend.dto.user;

import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCreateDto {
    @NotBlank
    @Length(max = 150)
    private final String username;
    @NotNull
    private final UserRole role;
    @NotBlank
    @Length(max = 250)
    private final String fullName;
    @NotBlank
    @Length(max = 250)
    private final String password;
}
