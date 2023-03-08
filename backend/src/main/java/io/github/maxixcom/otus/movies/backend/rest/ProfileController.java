package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileChangePasswordDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileEditDto;
import io.github.maxixcom.otus.movies.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/api/v1/profile")
    public ResponseEntity<ProfileDto> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping("/api/v1/profile")
    public ResponseEntity<ProfileDto> editProfile(@Valid @RequestBody ProfileEditDto profileEditDto) {
        return ResponseEntity.ok(profileService.editProfile(profileEditDto));
    }

    @PutMapping("/api/v1/profile/password")
    public ResponseEntity<ProfileDto> changeProfilePassword(@Valid @RequestBody ProfileChangePasswordDto profileChangePasswordDto) {
        profileService.changeProfilePassword(profileChangePasswordDto);
        return ResponseEntity.noContent().build();
    }
}
