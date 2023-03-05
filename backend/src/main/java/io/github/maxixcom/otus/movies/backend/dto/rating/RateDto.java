package io.github.maxixcom.otus.movies.backend.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RateDto {

    @Min(1)
    @Max(5)
    private long value;
}