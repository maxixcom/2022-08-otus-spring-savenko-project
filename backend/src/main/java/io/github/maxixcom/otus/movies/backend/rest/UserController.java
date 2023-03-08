package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.user.UserCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserEditDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListRequestDto;
import io.github.maxixcom.otus.movies.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/user")
    public ResponseEntity<UserListDto> listUsers(
            UserListRequestDto requestDto, Pageable pageable) {
        UserListDto responseDto = userService.getUsers(requestDto, pageable);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/v1/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        UserDto responseDto = userService.getUser(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/v1/user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateDto createDto) {
        long id = userService.createUser(createDto);
        return ResponseEntity.created(URI.create("/api/v1/user/" + id))
                .build();
    }

    @PutMapping("/api/v1/user/{id}")
    public ResponseEntity<Object> editUser(@PathVariable long id, @Valid @RequestBody UserEditDto editDto) {
        UserDto categoryDto = userService.editUser(id, editDto);
        return ResponseEntity.ok(categoryDto);
    }

    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        userService.deleteUser(Set.of(id));
        return ResponseEntity.noContent().build();
    }
}
