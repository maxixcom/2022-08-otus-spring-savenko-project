package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListRequestDto;
import io.github.maxixcom.otus.movies.backend.mappers.CategoryMapperImpl;
import io.github.maxixcom.otus.movies.backend.mappers.MovieMapperImpl;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapperImpl;
import io.github.maxixcom.otus.movies.backend.repository.MovieRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@Import({MovieServiceImpl.class, MovieMapperImpl.class, CategoryMapperImpl.class, PageMapperImpl.class})
class MovieServiceImplTest {
    @Autowired
    private MovieServiceImpl movieService;
    @Autowired
    private MovieRepository movieRepository;

    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnMovie() throws Exception {
        long movieId = 1L;

        MovieDto dto = movieService.getMovie(movieId);

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found. Check test configuration"));

        org.junit.jupiter.api.Assertions.assertAll(() -> {
            Assertions.assertThat(dto.getTitle()).isEqualTo(movie.getTitle());
            Assertions.assertThat(dto.getDescription()).isEqualTo(movie.getDescription());
            Assertions.assertThat(dto.getEmbedCode()).isEqualTo(movie.getEmbedCode());
            Assertions.assertThat(dto.getRating()).isEqualTo(movie.getRating());
            Assertions.assertThat(dto.getRatingSum()).isEqualTo(movie.getRatingSum());
            Assertions.assertThat(dto.getRatingCount()).isEqualTo(movie.getRatingCount());
            Assertions.assertThat(dto.isHasPoster()).isEqualTo(movie.isHasPoster());
        });
    }

    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnListOfMovies() {
        MovieListRequestDto requestDto = new MovieListRequestDto();
        MovieListDto listDto = movieService.getMovies(requestDto, PageRequest.of(0, 10));

        Assertions.assertThat(listDto.getItems().size()).isGreaterThan(0);
    }

    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldCreateMovie() {
        MovieCreateDto createDto = new MovieCreateDto();
        createDto.setTitle("new title");
        createDto.setDescription("new description");
        createDto.setEmbedCode("embed code");
        createDto.setCategoryId(1L);

        long movieId = movieService.createMovie(createDto);

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        Assertions.assertThat(movieOptional)
                .isPresent()
                .hasValueSatisfying(movie -> {
                    Assertions.assertThat(movie.getTitle()).isEqualTo(createDto.getTitle());
                    Assertions.assertThat(movie.getDescription()).isEqualTo(createDto.getDescription());
                    Assertions.assertThat(movie.getEmbedCode()).isEqualTo(createDto.getEmbedCode());
                    Assertions.assertThat(movie.getCategory()).isNotNull();
                    Assertions.assertThat(movie.getCategory().getId()).isEqualTo(createDto.getCategoryId());
                });
    }

    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldEditMovie() {
        long movieId = 1L;

        MovieEditDto editDto = new MovieEditDto();
        editDto.setTitle("new title");
        editDto.setDescription("new description");
        editDto.setEmbedCode("embed code");
        editDto.setCategoryId(2L);

        movieService.editMovie(movieId, editDto);

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        Assertions.assertThat(movieOptional)
                .isPresent()
                .hasValueSatisfying(movie -> {
                    Assertions.assertThat(movie.getTitle()).isEqualTo(editDto.getTitle());
                    Assertions.assertThat(movie.getDescription()).isEqualTo(editDto.getDescription());
                    Assertions.assertThat(movie.getEmbedCode()).isEqualTo(editDto.getEmbedCode());
                    Assertions.assertThat(movie.getCategory()).isNotNull();
                    Assertions.assertThat(movie.getCategory().getId()).isEqualTo(editDto.getCategoryId());
                });
    }


    @Sql(scripts = "/sql/movie/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/movie/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldDeleteMovie() {
        long movieId = 1L;

        movieService.deleteMovie(Set.of(movieId));

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        Assertions.assertThat(movieOptional).isEmpty();
    }
}