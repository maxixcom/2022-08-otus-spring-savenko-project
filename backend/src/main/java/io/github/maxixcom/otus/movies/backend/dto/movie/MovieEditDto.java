package io.github.maxixcom.otus.movies.backend.dto.movie;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class MovieEditDto {
    @NotBlank
    @Length(max = 2400)
    private String embedCode;

    @NotBlank
    @Length(max = 150)
    private String title;

    @Length(max = 5000)
    private String description;

    private Long categoryId;
}
