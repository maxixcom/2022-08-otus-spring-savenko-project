package io.github.maxixcom.otus.movies.backend.dto;

import lombok.Data;

@Data
public class UploadFileInfo {
    private String name;
    private Integer size;
    private String mimetype;
    private String originalName;
    private Long originalSize;
    private String originalMimetype;
    private String path;
    private String link;
}
