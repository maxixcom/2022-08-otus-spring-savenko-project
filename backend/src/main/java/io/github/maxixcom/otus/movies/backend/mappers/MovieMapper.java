package io.github.maxixcom.otus.movies.backend.mappers;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.rating.RatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {
    @Autowired
    protected CategoryMapper categoryMapper;

    @Mapping(target = "category", expression = "java(movie.getCategory()!=null?categoryMapper.categoryToCategoryDto(movie.getCategory()):null)")
    public abstract MovieDto movieToMovieDto(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "ratingSum", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "hasPoster", ignore = true)
    public abstract Movie movieCreateDtoToMovie(MovieCreateDto movieCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "ratingSum", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "hasPoster", ignore = true)
    public abstract void updateMovieFromMovieEditDto(MovieEditDto movieEditDto, @MappingTarget Movie movie);

    public abstract RatingDto movieToRatingDto(Movie movie);
}
