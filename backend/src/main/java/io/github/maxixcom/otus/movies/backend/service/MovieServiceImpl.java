package io.github.maxixcom.otus.movies.backend.service;

import io.github.maxixcom.otus.movies.backend.domain.Movie;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListRequestDto;
import io.github.maxixcom.otus.movies.backend.exception.MovieNotFoundException;
import io.github.maxixcom.otus.movies.backend.mappers.MovieMapper;
import io.github.maxixcom.otus.movies.backend.mappers.PageMapper;
import io.github.maxixcom.otus.movies.backend.repository.CategoryRepository;
import io.github.maxixcom.otus.movies.backend.repository.MovieRepository;
import io.github.maxixcom.otus.movies.backend.repository.specification.MovieSpecification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final MovieMapper movieMapper;
    private final PageMapper pageMapper;

    private final static MovieDto EmptyMovieDto;

    static {
        EmptyMovieDto = new MovieDto();
        EmptyMovieDto.setId(-1L);
        EmptyMovieDto.setTitle("N/A");
        EmptyMovieDto.setDescription("N/A");
        EmptyMovieDto.setEmbedCode("N/A");
        EmptyMovieDto.setRating(BigDecimal.ZERO);
    }

    @Cacheable("movies")
    @CircuitBreaker(name = "getMovies", fallbackMethod = "getMoviesFallback")
    @Transactional(readOnly = true)
    @Override
    public MovieListDto getMovies(MovieListRequestDto requestDto, Pageable pageable) {
        Specification<Movie> searchSpecification = Optional.ofNullable(requestDto.getSearch())
                .filter(search -> !search.isBlank())
                .map(MovieSpecification::findBySearch)
                .orElse(Specification.where(null));

        Specification<Movie> categorySpecification = Optional.ofNullable(requestDto.getCategoryIds())
                .map(categoryRepository::findAllById)
                .map(MovieSpecification::findByCategory)
                .orElse(Specification.where(null));


        Page<Movie> moviePage = movieRepository.findAll(
                Specification.where(searchSpecification.and(categorySpecification))
                , pageable);


        List<MovieDto> items = moviePage.getContent()
                .stream()
                .map(movieMapper::movieToMovieDto)
                .collect(Collectors.toList());

        return new MovieListDto(
                items,
                pageMapper.pageToMeta(moviePage)
        );
    }

    public MovieListDto getMoviesFallback(MovieListRequestDto requestDto, Pageable pageable, Exception exception) {
        return new MovieListDto();
    }

    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(value = "movie", allEntries = true)
    })
    @Transactional
    @Override
    public long createMovie(MovieCreateDto movieCreateDto) {
        final Movie movie = movieMapper.movieCreateDtoToMovie(movieCreateDto);

        Optional.ofNullable(movieCreateDto.getCategoryId())
                .flatMap(categoryRepository::findById)
                .ifPresent(movie::setCategory);

        movie.setEmbedCode(sanitizeEmbedCode(movieCreateDto.getEmbedCode()));

        Movie storedMovie = movieRepository.save(movie);

        return storedMovie.getId();
    }

    @Caching(evict = {
            @CacheEvict(value = "movies", allEntries = true),
            @CacheEvict(value = "movie", allEntries = true)
    })
    @Transactional
    @Override
    public MovieDto editMovie(long id, MovieEditDto movieEditDto) {
        final Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.valueOf(id)));

        movieMapper.updateMovieFromMovieEditDto(movieEditDto, movie);

        Optional.ofNullable(movieEditDto.getCategoryId())
                .flatMap(categoryRepository::findById)
                .ifPresentOrElse(movie::setCategory, () -> movie.setCategory(null));

        movie.setEmbedCode(sanitizeEmbedCode(movieEditDto.getEmbedCode()));

        Movie storedMovie = movieRepository.save(movie);

        return movieMapper.movieToMovieDto(storedMovie);
    }

    @Caching(evict = {
        @CacheEvict(value = "movies", allEntries = true),
        @CacheEvict(value = "movie", allEntries = true)
    })
    @Transactional
    @Override
    public void deleteMovie(Set<Long> ids) {
        movieRepository.deleteAllByIdInBatch(ids);
    }

    @Cacheable("movie")
    @CircuitBreaker(name = "getMovie", fallbackMethod = "getMovieFallback")
    @Transactional(readOnly = true)
    @Override
    public MovieDto getMovie(long id) {
        Movie movie = movieRepository.findWithCategoryOneById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.valueOf(id)));

        return movieMapper.movieToMovieDto(movie);
    }

    public MovieDto getMovieFallback(long id, Exception exception) {
        return EmptyMovieDto;
    }

    private String sanitizeEmbedCode(String code) {
        Document document = Jsoup.parseBodyFragment(code);
        Element iframe = document.selectFirst("iframe");
        if(iframe==null) {
            return code;
        }

        iframe.removeAttr("style");
        iframe.removeAttr("class");
        iframe.attr("width","100%");
        iframe.attr("height","100%");
        iframe.attr("frameborder","0");

        return document.body().html();
    }
}
