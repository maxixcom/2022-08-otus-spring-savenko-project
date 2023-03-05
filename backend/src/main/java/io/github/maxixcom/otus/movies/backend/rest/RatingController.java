package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.rating.RateDto;
import io.github.maxixcom.otus.movies.backend.dto.rating.RatingDto;
import io.github.maxixcom.otus.movies.backend.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/api/v1/movie/{id}/rate")
    public ResponseEntity<RatingDto> rateMovie(@PathVariable long id, @Valid @RequestBody RateDto rateDto) {
        return ResponseEntity.ok(ratingService.rateMovie(id, rateDto));
    }
}
