package io.github.maxixcom.otus.movies.backend.dto.movie;

import io.github.maxixcom.otus.movies.backend.dto.ListDto;
import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class MovieListDto extends ListDto<MovieDto> {
    public MovieListDto(List<MovieDto> items, MetaDto meta) {
        super(items, meta);
    }
}
