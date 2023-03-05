package io.github.maxixcom.otus.movies.backend.service.image;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.dto.UploadFileInfo;
import io.github.maxixcom.otus.movies.backend.dto.movie.MoviePosterUploadDto;
import io.github.maxixcom.otus.movies.backend.exception.MovieNotFoundException;
import io.github.maxixcom.otus.movies.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class MoviePosterUploadServiceImpl implements MoviePosterUploadService {
    private static final String POSTERS_PATH = "/posters";
    private final ImageStorageService imageStorageService;
    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public UploadFileInfo uploadPoster(long movieId, MoviePosterUploadDto uploadDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(String.valueOf(movieId)));

        try {
            MultipartFile multipartFile = uploadDto.getFile();
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            Path path = Paths.get(POSTERS_PATH);
            String filename = String.valueOf(movieId);

            int size = resizeAndStore(image, filename, path, 1024);

            UploadFileInfo uploadFileInfo = new UploadFileInfo();
            uploadFileInfo.setName(filename);
            uploadFileInfo.setSize(size);
            uploadFileInfo.setMimetype(MimeTypeUtils.IMAGE_JPEG_VALUE);
            uploadFileInfo.setOriginalName(multipartFile.getOriginalFilename());
            uploadFileInfo.setOriginalSize(multipartFile.getSize());
            uploadFileInfo.setOriginalMimetype(multipartFile.getContentType());
            uploadFileInfo.setPath(path.toString());

            movie.setHasPoster(true);

            return uploadFileInfo;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void deletePoster(long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(String.valueOf(movieId)));

        imageStorageService.delete(Paths.get(POSTERS_PATH).resolve("%d.jpg".formatted(movieId)));

        movie.setHasPoster(false);
    }

    private int resizeAndStore(BufferedImage image, String name, Path path, Integer size) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage resultImage;

            if (size == null) {
                resultImage = ImageUtils.copyImage(image);
            } else {
                resultImage = ImageUtils.resizeToSquare(image, size, size);
            }

            ImageIO.write(resultImage, "JPG", outputStream);

            imageStorageService.store(
                    outputStream.toByteArray(),
                    path.resolve(name + ".jpg"),
                    MimeTypeUtils.IMAGE_JPEG_VALUE
            );

            return outputStream.size();
        }
    }

}
