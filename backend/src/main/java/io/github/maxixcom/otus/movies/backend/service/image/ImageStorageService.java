package io.github.maxixcom.otus.movies.backend.service.image;

import java.nio.file.Path;

public interface ImageStorageService {
    void store(byte[] data, Path destination, String mediaType);

    void delete(Path destination);
}
