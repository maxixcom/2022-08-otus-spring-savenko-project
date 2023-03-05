package io.github.maxixcom.otus.movies.backend.mappers;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto categoryToCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category categoryCreateDtoToCategory(CategoryCreateDto categoryCreateDto);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromCategoryEditDto(CategoryEditDto categoryEditDto, @MappingTarget Category category);
}
