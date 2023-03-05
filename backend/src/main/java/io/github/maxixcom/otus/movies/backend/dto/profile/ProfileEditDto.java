package io.github.maxixcom.otus.movies.backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileEditDto {
    @NotBlank
    @Length(max = 250)
    private String fullName;
}
