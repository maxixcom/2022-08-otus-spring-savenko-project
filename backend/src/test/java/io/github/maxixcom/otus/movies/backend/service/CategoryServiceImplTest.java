package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryListDto;
import io.github.maxixcom.otus.movies.backend.mappers.CategoryMapperImpl;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapperImpl;
import io.github.maxixcom.otus.movies.backend.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@Import({CategoryServiceImpl.class, CategoryMapperImpl.class, PageMapperImpl.class})
class CategoryServiceImplTest {
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Sql(scripts = "/sql/category/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/category/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnCategory() {
        long categoryId = 1L;

        CategoryDto dto = categoryService.getCategory(categoryId);

        CategoryDto expected = new CategoryDto(categoryId, "title");

        Assertions.assertThat(dto).usingRecursiveComparison().isEqualTo(expected);
    }

    @Sql(scripts = "/sql/category/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/category/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldReturnListOfCategories() {
        CategoryListDto dto = categoryService.getCategories("xxx", PageRequest.of(0, 10));

        Assertions.assertThat(dto.getItems()).size().isGreaterThan(0);
    }

    @Sql(scripts = "/sql/category/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldCreateCategory() {
        CategoryCreateDto createDto = new CategoryCreateDto("new category");

        long categoryId = categoryService.createCategory(createDto);

        Optional<Category> expectedCategoryOptional = categoryRepository.findById(categoryId);

        Assertions.assertThat(expectedCategoryOptional).isPresent().hasValueSatisfying(category -> {
            Assertions.assertThat(category.getTitle()).isEqualTo("new category");
        });
    }

    @Sql(scripts = "/sql/category/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/category/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldEditCategory() {
        long categoryId = 1L;

        categoryService.editCategory(categoryId, new CategoryEditDto("new title"));

        Optional<Category> expectedCategoryOptional = categoryRepository.findById(categoryId);

        Assertions.assertThat(expectedCategoryOptional).isPresent().hasValueSatisfying(category -> {
            Assertions.assertThat(category.getTitle()).isEqualTo("new title");
        });
    }

    @Sql(scripts = "/sql/category/tests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/category/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldDeleteCategory() {
        long categoryId = 1L;

        categoryService.deleteCategories(Set.of(categoryId));

        Optional<Category> expectedCategoryOptional = categoryRepository.findById(categoryId);

        Assertions.assertThat(expectedCategoryOptional).isEmpty();
    }

}