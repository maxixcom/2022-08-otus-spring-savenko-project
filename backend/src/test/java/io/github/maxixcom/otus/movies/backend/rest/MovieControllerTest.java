package io.github.maxixcom.otus.movies.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.config.SecurityConfig;
import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieEditDto;
import io.github.maxixcom.otus.movies.backend.dto.movie.MovieListDto;
import io.github.maxixcom.otus.movies.backend.exception.MovieNotFoundException;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import io.github.maxixcom.otus.movies.backend.service.MovieService;
import io.github.maxixcom.otus.movies.backend.service.token.TokenServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

@Import({SecurityConfig.class, JwtTokenFilter.class, TokenServiceImpl.class, JwtConfigProperties.class})
@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MovieService movieService;

    private MovieDto sampleMovieDto;

    @BeforeEach
    void setUp() {
        sampleMovieDto = new MovieDto();
        sampleMovieDto.setId(1L);
        sampleMovieDto.setTitle("title");
        sampleMovieDto.setEmbedCode("embed code");
        sampleMovieDto.setDescription("description");
        sampleMovieDto.setCategory(new CategoryDto(2L, "movie"));
    }

    @Test
    void shouldReturnAllMovies() throws Exception {
        List<MovieDto> items = List.of(sampleMovieDto);
        MetaDto metaDto = new MetaDto();
        metaDto.setPage(0);
        metaDto.setSize(20);
        metaDto.setTotalElements(1);
        metaDto.setTotalPages(1);

        MovieListDto movieListDto = new MovieListDto(items, metaDto);

        Mockito.when(movieService.getMovies(Mockito.any(), Mockito.any()))
                .thenReturn(movieListDto);

        String expectedResult = mapper.writeValueAsString(movieListDto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/movie"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

    }

    @Test
    void shouldReturnMovie() throws Exception {
        Mockito.when(movieService.getMovie(Mockito.anyLong()))
                .thenReturn(sampleMovieDto);

        String expectedResult = mapper.writeValueAsString(sampleMovieDto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/movie/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

    @Test
    void shouldNotReturnAnyMovie() throws Exception {
        Mockito.when(movieService.getMovie(Mockito.eq(1L))).thenThrow(new MovieNotFoundException("1"));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/movie/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldCreateMovieWithAdminPermission() throws Exception {
        Mockito.when(movieService.createMovie(Mockito.any())).thenReturn(1L);

        MovieCreateDto expectedDto = new MovieCreateDto();
        expectedDto.setTitle("title");
        expectedDto.setDescription("description");
        expectedDto.setEmbedCode("code");

        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/movie")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/movie/1"))
                .andExpect(MockMvcResultMatchers.content().string(""));

        ArgumentCaptor<MovieCreateDto> captorParam = ArgumentCaptor.forClass(MovieCreateDto.class);
        Mockito.verify(movieService).createMovie(captorParam.capture());

        MovieCreateDto actualDto = captorParam.getValue();

        Assertions.assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldBeForbiddenCreatingMovieByUser() throws Exception {
        MovieCreateDto expectedDto = new MovieCreateDto();
        String requestBody = mapper.writeValueAsString(expectedDto);
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/movie")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldEditMovieWithAdminPermission() throws Exception {
        Mockito.when(movieService.editMovie(Mockito.anyLong(), Mockito.any())).thenReturn(sampleMovieDto);
        String expectedResult = mapper.writeValueAsString(sampleMovieDto);

        MovieEditDto expectedDto = new MovieEditDto();
        expectedDto.setTitle("title");
        expectedDto.setEmbedCode("embed code");

        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/movie/1")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

        ArgumentCaptor<Long> captorParamId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<MovieEditDto> captorParam = ArgumentCaptor.forClass(MovieEditDto.class);

        Mockito.verify(movieService).editMovie(captorParamId.capture(), captorParam.capture());

        MovieEditDto actualDto = captorParam.getValue();

        Assertions.assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldBeForbiddenEditMovieByUser() throws Exception {
        MovieEditDto expectedDto = new MovieEditDto();
        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/movie/1")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldDeleteMovieWithAdminPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/movie/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));

        Mockito.verify(movieService, Mockito.times(1)).deleteMovie(Mockito.eq(Set.of(1L)));
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldFailDeleteMovieWithUserPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/movie/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}