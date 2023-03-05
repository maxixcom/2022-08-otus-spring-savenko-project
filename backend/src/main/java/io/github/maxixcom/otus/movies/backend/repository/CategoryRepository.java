package io.github.maxixcom.otus.movies.backend.repository;

import io.github.maxixcom.otus.movies.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
