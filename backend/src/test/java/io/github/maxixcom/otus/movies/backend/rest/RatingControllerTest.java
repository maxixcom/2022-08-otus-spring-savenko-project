package io.github.maxixcom.otus.movies.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.config.SecurityConfig;
import io.github.maxixcom.otus.movies.backend.dto.rating.RateDto;
import io.github.maxixcom.otus.movies.backend.dto.rating.RatingDto;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import io.github.maxixcom.otus.movies.backend.service.RatingService;
import io.github.maxixcom.otus.movies.backend.service.token.TokenServiceImpl;
import org.junit.jupiter.api.Test;
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

import java.math.BigDecimal;

@Import({SecurityConfig.class, JwtTokenFilter.class, TokenServiceImpl.class, JwtConfigProperties.class})
@WebMvcTest(RatingController.class)
class RatingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RatingService ratingService;

    @WithMockUser
    @Test
    void shouldRateMovie() throws Exception {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setRatingCount(1);
        ratingDto.setRatingSum(5);
        ratingDto.setRating(BigDecimal.valueOf(5));

        Mockito.when(ratingService.rateMovie(Mockito.anyLong(), Mockito.any()))
                .thenReturn(ratingDto);

        String expectedResult = mapper.writeValueAsString(ratingDto);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/movie/1/rate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(new RateDto(5)))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

    @Test
    void unauthorizedUserShouldFailRateMovie() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/movie/1/rate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(new RateDto(5)))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}