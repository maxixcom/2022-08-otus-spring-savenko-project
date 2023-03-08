package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryListDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CategoryService {
    CategoryListDto getCategories(String search, Pageable pageable);

    long createCategory(CategoryCreateDto categoryCreateDto);

    CategoryDto editCategory(long id, CategoryEditDto categoryEditDto);

    void deleteCategories(Set<Long> ids);

    CategoryDto getCategory(long id);
}
