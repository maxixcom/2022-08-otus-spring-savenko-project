package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.rating.RateDto;
import io.github.maxixcom.otus.movies.backend.dto.rating.RatingDto;
import io.github.maxixcom.otus.movies.backend.exception.MovieNotFoundException;
import io.github.maxixcom.otus.movies.backend.mappers.MovieMapper;
import io.github.maxixcom.otus.movies.backend.repository.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {
    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    private final static RatingDto EmptyRatingDto;

    static {
        EmptyRatingDto = new RatingDto();
    }

    @CircuitBreaker(name="rateMovie", fallbackMethod = "rateMovieFallback")
    @Transactional
    @Override
    public RatingDto rateMovie(long movieId, RateDto rateDto) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    long count = movie.getRatingCount() + 1;
                    long sum = movie.getRatingSum() + rateDto.getValue();

                    BigDecimal rating = BigDecimal.valueOf(sum)
                            .divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);

                    movie.setRatingCount(count);
                    movie.setRatingSum(sum);
                    movie.setRating(rating);

                    return movieMapper.movieToRatingDto(movie);
                })
                .orElseThrow(() -> new MovieNotFoundException(String.valueOf(movieId)));
    }

    public RatingDto rateMovieFallback(long movieId, RateDto rateDto, Exception exception) {
        return EmptyRatingDto;
    }
}
