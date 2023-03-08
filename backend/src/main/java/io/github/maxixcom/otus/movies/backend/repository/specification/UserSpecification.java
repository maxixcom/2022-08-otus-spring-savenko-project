package io.github.maxixcom.otus.movies.backend.repository.specification;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.domain.UserRole;
import io.github.maxixcom.otus.movies.backend.domain.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class UserSpecification {
    public static Specification<User> findBySearch(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(User_.USERNAME)),
                                "%" + search.toLowerCase() + "%"
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(User_.FULL_NAME)),
                                "%" + search.toLowerCase() + "%"
                        )
                );
    }

    public static Specification<User> findByRole(Set<UserRole> userRoles) {
        return (root, query, criteriaBuilder) -> root.get(User_.ROLE).in(userRoles);
    }

}
