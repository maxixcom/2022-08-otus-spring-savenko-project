package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.dto.rating.RateDto;
import io.github.maxixcom.otus.movies.backend.mappers.CategoryMapperImpl;
import io.github.maxixcom.otus.movies.backend.mappers.MovieMapperImpl;
import io.github.maxixcom.otus.movies.backend.repository.MovieRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@Import({RatingServiceImpl.class, MovieMapperImpl.class, CategoryMapperImpl.class})
class RatingServiceImplTest {
    @Autowired
    private RatingServiceImpl ratingService;
    @Autowired
    private MovieRepository movieRepository;

    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void rateMovie() {
        long movieId = 1;
        RateDto rateDto = new RateDto(3);

        ratingService.rateMovie(movieId, rateDto);

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        Assertions.assertThat(movieOptional)
                .isPresent()
                .hasValueSatisfying(movie -> {
                    Assertions.assertThat(movie.getRatingCount()).isEqualTo(1);
                    Assertions.assertThat(movie.getRatingSum()).isEqualTo(rateDto.getValue());
                    Assertions.assertThat(movie.getRating()).isEqualByComparingTo(BigDecimal.valueOf(rateDto.getValue()));
                });
    }
}