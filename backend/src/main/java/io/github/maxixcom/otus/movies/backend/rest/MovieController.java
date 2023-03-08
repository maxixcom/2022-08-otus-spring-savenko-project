package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListRequestDto;
import io.github.maxixcom.otus.movies.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/api/v1/movie")
    public ResponseEntity<MovieListDto> listMovies(
            MovieListRequestDto requestDto, Pageable pageable) {
        MovieListDto responseDto = movieService.getMovies(requestDto, pageable);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/v1/movie/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable long id) {
        MovieDto responseDto = movieService.getMovie(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/v1/movie")
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieCreateDto createDto) {
        long id = movieService.createMovie(createDto);
        return ResponseEntity.created(URI.create("/api/v1/movie/" + id))
                .build();
    }

    @PutMapping("/api/v1/movie/{id}")
    public ResponseEntity<Object> editMovie(@PathVariable long id, @Valid @RequestBody MovieEditDto editDto) {
        MovieDto categoryDto = movieService.editMovie(id, editDto);
        return ResponseEntity.ok(categoryDto);
    }

    @DeleteMapping("/api/v1/movie/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable long id) {
        movieService.deleteMovie(Set.of(id));
        return ResponseEntity.noContent().build();
    }
}
