package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.user.UserCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserEditDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {
    UserListDto getUsers(UserListRequestDto requestDto, Pageable pageable);

    long createUser(UserCreateDto movieCreateDto);

    UserDto editUser(long id, UserEditDto movieEditDto);

    void deleteUser(Set<Long> ids);

    UserDto getUser(long id);
}
