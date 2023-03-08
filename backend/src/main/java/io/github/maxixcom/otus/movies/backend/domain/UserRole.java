package io.github.maxixcom.otus.movies.backend.domain;

import java.util.Set;

public enum UserRole {
    ADMIN(Set.of(
            UserPermission.ADMIN_AREA,
            UserPermission.USER_AREA
    )),
    USER(Set.of(
            UserPermission.USER_AREA
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

}
