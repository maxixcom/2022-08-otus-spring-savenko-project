package io.github.maxixcom.otus.movies.backend.repository.specification;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import io.github.maxixcom.otus.movies.backend.domain.Category_;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> findBySearch(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(Category_.TITLE)),
                        "%" + search.toLowerCase() + "%"
                );
    }
}
