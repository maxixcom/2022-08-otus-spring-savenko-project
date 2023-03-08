package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.rating.RateDto;
import io.github.maxixcom.otus.movies.backend.dto.rating.RatingDto;

public interface RatingService {
    RatingDto rateMovie(long movieId, RateDto rateDto);
}
