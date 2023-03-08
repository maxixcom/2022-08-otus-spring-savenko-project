package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryListDto;
import io.github.maxixcom.otus.movies.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/api/v1/category")
    public ResponseEntity<CategoryListDto> listCategories(
            @RequestParam(required = false) String search, Pageable pageable) {
        CategoryListDto responseDto = categoryService.getCategories(search, pageable);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/v1/category/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable long id) {
        CategoryDto responseDto = categoryService.getCategory(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/v1/category")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryCreateDto createDto) {
        long id = categoryService.createCategory(createDto);
        return ResponseEntity.created(URI.create("/api/v1/category/" + id))
                .build();
    }

    @PutMapping("/api/v1/category/{id}")
    public ResponseEntity<Object> editCategory(@PathVariable long id, @Valid @RequestBody CategoryEditDto editDto) {
        CategoryDto categoryDto = categoryService.editCategory(id, editDto);
        return ResponseEntity.ok(categoryDto);
    }

    @DeleteMapping("/api/v1/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategories(Set.of(id));
        return ResponseEntity.noContent().build();
    }
}
