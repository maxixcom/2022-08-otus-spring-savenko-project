package io.github.maxixcom.otus.movies.backend.service.image;

import io.github.maxixcom.otus.movies.backend.dto.UploadFileInfo;
import io.github.maxixcom.otus.movies.backend.dto.movie.MoviePosterUploadDto;

public interface MoviePosterUploadService {
    UploadFileInfo uploadPoster(long movieId, MoviePosterUploadDto uploadDto);

    void deletePoster(long movieId);
}
