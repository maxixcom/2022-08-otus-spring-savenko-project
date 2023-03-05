package io.github.maxixcom.otus.movies.backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileChangePasswordDto {
    @NotBlank
    @Length(max = 150)
    private String passwordOld;

    @NotBlank
    @Length(max = 150)
    private String passwordNew;
}
