package io.github.maxixcom.otus.movies.backend.repository.specification;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.domain.Movie_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.util.List;

public class MovieSpecification {
    public static Specification<Movie> findBySearch(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(Movie_.TITLE)),
                        "%" + search.toLowerCase() + "%"
                );
    }

    public static Specification<Movie> findByCategory(List<Category> categories) {
        return (root, query, criteriaBuilder) -> root.get(Movie_.CATEGORY).in(categories);
    }

}
