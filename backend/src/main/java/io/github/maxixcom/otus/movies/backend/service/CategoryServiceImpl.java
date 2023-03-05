package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryListDto;
import io.github.maxixcom.otus.movies.backend.exception.CategoryNotFoundException;
import io.github.maxixcom.otus.movies.backend.mappers.CategoryMapper;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapper;
import io.github.maxixcom.otus.movies.backend.repository.CategoryRepository;
import io.github.maxixcom.otus.movies.backend.repository.specification.CategorySpecification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PageMapper pageMapper;

    @Cacheable("categories")
    @CircuitBreaker(name = "getCategories", fallbackMethod = "getCategoriesFallback")
    @Transactional(readOnly = true)
    @Override
    public CategoryListDto getCategories(String search, Pageable pageable) {
        Specification<Category> specification = Optional.ofNullable(search)
                .filter(s -> !s.isBlank())
                .map(CategorySpecification::findBySearch)
                .orElse(Specification.where(null));

        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);

        List<CategoryDto> items = categoryPage.getContent()
                .stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());

        return new CategoryListDto(
                items,
                pageMapper.pageToMeta(categoryPage)
        );
    }

    public CategoryListDto getCategoriesFallback(String search, Pageable pageable, Exception exception) {
        return new CategoryListDto();
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Transactional
    @Override
    public long createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = categoryMapper.categoryCreateDtoToCategory(categoryCreateDto);
        category = categoryRepository.save(category);

        return category.getId();
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Transactional
    @Override
    public CategoryDto editCategory(long id, CategoryEditDto categoryEditDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(id)));

        categoryMapper.updateCategoryFromCategoryEditDto(categoryEditDto, category);

        category = categoryRepository.save(category);

        return categoryMapper.categoryToCategoryDto(category);
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Transactional
    @Override
    public void deleteCategories(Set<Long> ids) {
        categoryRepository.deleteAllByIdInBatch(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(id)));

        return categoryMapper.categoryToCategoryDto(category);
    }
}
