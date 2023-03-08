package io.github.maxixcom.otus.movies.backend.dto.user;

import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    private final Long id;
    private final String username;
    private final UserRole role;
    private final String fullName;
}
