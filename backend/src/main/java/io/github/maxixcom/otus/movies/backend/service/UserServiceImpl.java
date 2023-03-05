package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.dto.user.UserCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserEditDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserListRequestDto;
import io.github.maxixcom.otus.movies.backend.exception.NotAuthenticatedException;
import io.github.maxixcom.otus.movies.backend.exception.UserNotFoundException;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapper;
import io.github.maxixcom.otus.movies.backend.mappers.UserMapper;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.repository.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserListDto getUsers(UserListRequestDto requestDto, Pageable pageable) {

        Specification<User> searchSpecification = Optional.ofNullable(requestDto.getSearch())
                .filter(search -> !search.isBlank())
                .map(UserSpecification::findBySearch)
                .orElse(Specification.where(null));

        Specification<User> roleSpecification = Optional.ofNullable(requestDto.getRoles())
                .map(UserSpecification::findByRole)
                .orElse(Specification.where(null));


        Page<User> page = userRepository.findAll(
                Specification.where(searchSpecification.and(roleSpecification))
                , pageable);


        List<UserDto> items = page.getContent()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        return new UserListDto(
                items,
                pageMapper.pageToMeta(page)
        );
    }

    @Transactional
    @Override
    public long createUser(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user = userRepository.save(user);

        return user.getId();
    }

    @Transactional
    @Override
    public UserDto editUser(long id, UserEditDto userEditDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));

        userMapper.updateUserFromUserEditDto(userEditDto, user);

        User storedUser = userRepository.save(user);

        return userMapper.userToUserDto(storedUser);

    }

    @Transactional
    @Override
    public void deleteUser(Set<Long> ids) {
        User currentUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (Long) authentication.getPrincipal())
                .flatMap(userRepository::findById)
                .orElseThrow(NotAuthenticatedException::new);

        userRepository.deleteAllByIdInBatch(
                ids.stream()
                        .filter(id -> !id.equals(currentUser.getId()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public UserDto getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));

        return userMapper.userToUserDto(user);
    }
}
