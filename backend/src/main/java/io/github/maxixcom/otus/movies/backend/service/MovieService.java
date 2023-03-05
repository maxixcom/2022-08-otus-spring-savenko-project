package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface MovieService {
    MovieListDto getMovies(MovieListRequestDto requestDto, Pageable pageable);

    long createMovie(MovieCreateDto movieCreateDto);

    MovieDto editMovie(long id, MovieEditDto movieEditDto);

    void deleteMovie(Set<Long> ids);

    MovieDto getMovie(long id);
}
