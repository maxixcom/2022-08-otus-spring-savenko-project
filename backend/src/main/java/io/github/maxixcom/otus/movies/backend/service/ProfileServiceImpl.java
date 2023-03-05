package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileChangePasswordDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileEditDto;
import io.github.maxixcom.otus.movies.backend.exception.IncorrectPasswordException;
import io.github.maxixcom.otus.movies.backend.exception.NotAuthenticatedException;
import io.github.maxixcom.otus.movies.backend.mappers.UserMapper;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileDto getProfile() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (Long) authentication.getPrincipal())
                .flatMap(userRepository::findById)
                .map(userMapper::userToProfileDto)
                .orElseThrow(NotAuthenticatedException::new);
    }

    @Transactional
    @Override
    public ProfileDto editProfile(ProfileEditDto profileEditDto) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (Long) authentication.getPrincipal())
                .flatMap(userRepository::findById)
                .map(user -> {
                    userMapper.updateUserFromProfileEditDto(profileEditDto, user);
                    return userRepository.save(user);
                })
                .map(userMapper::userToProfileDto)
                .orElseThrow(NotAuthenticatedException::new);
    }

    @Transactional
    @Override
    public void changeProfilePassword(ProfileChangePasswordDto profileChangePasswordDto) {
        User user = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (Long) authentication.getPrincipal())
                .flatMap(userRepository::findById)
                .orElseThrow(NotAuthenticatedException::new);

        if (!passwordEncoder.matches(profileChangePasswordDto.getPasswordOld(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        user.setPassword(passwordEncoder.encode(profileChangePasswordDto.getPasswordNew()));
        userRepository.save(user);
    }
}
