package io.github.maxixcom.otus.movies.backend.repository;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @EntityGraph(attributePaths = {"category"})
    Optional<Movie> findWithCategoryOneById(Long id);
}
