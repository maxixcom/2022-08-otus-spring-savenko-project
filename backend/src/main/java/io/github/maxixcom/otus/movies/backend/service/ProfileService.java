package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileChangePasswordDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileEditDto;

public interface ProfileService {
    ProfileDto getProfile();

    ProfileDto editProfile(ProfileEditDto profileEditDto);

    void changeProfilePassword(ProfileChangePasswordDto profileChangePasswordDto);
}
