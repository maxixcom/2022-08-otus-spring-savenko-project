package io.github.maxixcom.otus.movies.backend.dto.category;

import io.github.maxixcom.otus.movies.backend.dto.ListDto;
import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CategoryListDto extends ListDto<CategoryDto> {
    public CategoryListDto(List<CategoryDto> items, MetaDto meta) {
        super(items, meta);
    }
}
