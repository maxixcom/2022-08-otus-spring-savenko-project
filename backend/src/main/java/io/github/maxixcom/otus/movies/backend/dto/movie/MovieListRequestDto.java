package io.github.maxixcom.otus.movies.backend.dto.movie;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import java.util.Set;

@NoArgsConstructor
@Data
public class MovieListRequestDto {
    @Length(max = 150)
    private String search;

    @Positive
    private Set<Long> categoryIds;
}
