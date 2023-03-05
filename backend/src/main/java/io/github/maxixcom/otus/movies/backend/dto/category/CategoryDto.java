package io.github.maxixcom.otus.movies.backend.dto.category;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CategoryDto {
    private final Long id;
    private final String title;
}
