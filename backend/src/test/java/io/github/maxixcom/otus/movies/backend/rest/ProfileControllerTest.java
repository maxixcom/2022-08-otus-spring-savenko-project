package io.github.maxixcom.otus.movies.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.config.SecurityConfig;
import io.github.maxixcom.otus.movies.backend.dto.profile.ProfileDto;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import io.github.maxixcom.otus.movies.backend.service.ProfileService;
import io.github.maxixcom.otus.movies.backend.service.token.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import({SecurityConfig.class, JwtTokenFilter.class, TokenServiceImpl.class, JwtConfigProperties.class})
@WebMvcTest(ProfileController.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileService profileService;

    private ProfileDto sampleProfileDto;

    @BeforeEach
    void setUp() {
        sampleProfileDto = new ProfileDto();
        sampleProfileDto.setId(1);
        sampleProfileDto.setUsername("username");
        sampleProfileDto.setFullName("User");
    }

    @WithMockUser
    @Test
    void shouldReturnProfile() throws Exception {
        Mockito.when(profileService.getProfile())
                .thenReturn(sampleProfileDto);

        String expectedResult = mapper.writeValueAsString(sampleProfileDto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

    @Test
    void unauthorizedUserShouldFailGettingProfile() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/profile"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}