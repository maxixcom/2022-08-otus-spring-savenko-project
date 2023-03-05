package io.github.maxixcom.otus.movies.backend.mappers;

import io.github.maxixcom.otus.movies.backend.domain.User;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileEditDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserDto;
import io.github.maxixcom.otus.movies.backend.dto.user.UserEditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ProfileDto userToProfileDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromProfileEditDto(ProfileEditDto profileEditDto, @MappingTarget User user);

    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User userCreateDtoToUser(UserCreateDto userCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromUserEditDto(UserEditDto userEditDto, @MappingTarget User user);
}
