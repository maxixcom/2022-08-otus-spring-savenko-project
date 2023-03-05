package io.github.maxixcom.otus.movies.backend.dto.movie;

import io.github.maxixcom.otus.movies.backend.validation.MultipartFileMimeType;
import io.github.maxixcom.otus.movies.backend.validation.MultipartFileSize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class MoviePosterUploadDto {
    @MultipartFileSize(min = 512, max = 10485760)
    @MultipartFileMimeType
    @NotNull
    private MultipartFile file;
}
