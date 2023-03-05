package io.github.maxixcom.otus.movies.backend.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryEditDto {
    @NotBlank
    @Length(max = 150)
    private String title;
}
