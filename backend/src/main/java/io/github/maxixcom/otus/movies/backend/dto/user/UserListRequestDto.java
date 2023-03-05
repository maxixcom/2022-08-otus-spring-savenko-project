package io.github.maxixcom.otus.movies.backend.dto.user;

import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserListRequestDto {
    @Length(max = 150)
    private String search;

    private Set<UserRole> roles;
}
