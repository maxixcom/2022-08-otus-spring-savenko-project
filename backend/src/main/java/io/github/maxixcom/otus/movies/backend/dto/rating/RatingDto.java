package io.github.maxixcom.otus.movies.backend.dto.rating;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingDto {
    private long ratingCount;
    private long ratingSum;
    private BigDecimal rating;
}
