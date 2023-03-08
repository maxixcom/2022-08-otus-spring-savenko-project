package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.UploadFileInfo;
import io.github.maxixcom.otus.movies.backend.dto.movie.MoviePosterUploadDto;
import io.github.maxixcom.otus.movies.backend.service.image.MoviePosterUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MoviePosterController {
    private final MoviePosterUploadService uploadService;

    @PostMapping("/api/v1/movie/{id}/poster")
    public ResponseEntity<UploadFileInfo> uploadPoster(@PathVariable long id, @Valid MoviePosterUploadDto dto) {
        UploadFileInfo result = uploadService.uploadPoster(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/v1/movie/{id}/poster")
    public ResponseEntity<Object> deletePoster(@PathVariable long id) {
        uploadService.deletePoster(id);
        return ResponseEntity.noContent().build();
    }
}
