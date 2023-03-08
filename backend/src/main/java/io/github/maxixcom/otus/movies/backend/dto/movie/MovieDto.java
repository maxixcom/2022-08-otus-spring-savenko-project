package io.github.maxixcom.otus.movies.backend.dto.movie;

import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovieDto {
    private Long id;
    private String embedCode;
    private String title;
    private String description;
    private CategoryDto category;
    private long ratingCount;
    private long ratingSum;
    private BigDecimal rating;
    private boolean hasPoster;
}
