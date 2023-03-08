package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.config.SecurityConfig;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginRequestDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.LoginResponseDto;
import io.github.maxixcom.otus.movies.backend.dto.auth.RegisterUserRequestDto;
import io.github.maxixcom.otus.movies.backend.exception.UsernameAlreadyExistsException;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import io.github.maxixcom.otus.movies.backend.service.AuthService;
import io.github.maxixcom.otus.movies.backend.service.token.TokenServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, JwtTokenFilter.class, TokenServiceImpl.class, JwtConfigProperties.class})
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loginFailed() throws Exception {
        Mockito.when(authService.loginUser(Mockito.any())).thenThrow(new UsernameNotFoundException("user not found"));

        mvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"username\", \"password\": \"password\" }")
        ).andExpect(status().isForbidden());
    }

    @Test
    void loginBadRequest() throws Exception {
        mvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"\", \"password\": \"password\" }")
        ).andExpect(status().isBadRequest());
    }


    @Test
    void loginSuccess() throws Exception {
        Mockito.when(authService.loginUser(Mockito.any())).thenReturn(new LoginResponseDto("token"));

        mvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"username\": \"username\", \"password\": \"password\" }")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer token"));

        ArgumentCaptor<LoginRequestDto> captorParam = ArgumentCaptor.forClass(LoginRequestDto.class);
        Mockito.verify(authService).loginUser(captorParam.capture());

        LoginRequestDto requestDto = captorParam.getValue();

        Assertions.assertThat(requestDto)
                .usingRecursiveComparison()
                .isEqualTo(new LoginRequestDto("username", "password"));

    }

    @Test
    void registerFailedUsernameAlreadyExists() throws Exception {
        Mockito.when(authService.registerUser(Mockito.any())).thenThrow(new UsernameAlreadyExistsException("username"));

        mvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"username\", \"password\": \"password\", \"fullName\": \"fullName\" }")
        ).andExpect(status().isConflict());
    }

    @Test
    void registerBadRequest() throws Exception {
        Mockito.when(authService.registerUser(Mockito.any())).thenThrow(new UsernameAlreadyExistsException("username"));

        mvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"username\" }")
        ).andExpect(status().isBadRequest());
    }


    @Test
    void registerSuccess() throws Exception {
        Mockito.when(authService.registerUser(Mockito.any())).thenReturn(new LoginResponseDto("token"));

        mvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"username\": \"username\", \"password\": \"password\", \"fullName\": \"fullName\"  }")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer token"));

        ArgumentCaptor<RegisterUserRequestDto> captorParam = ArgumentCaptor.forClass(RegisterUserRequestDto.class);
        Mockito.verify(authService).registerUser(captorParam.capture());

        RegisterUserRequestDto requestDto = captorParam.getValue();

        Assertions.assertThat(requestDto)
                .usingRecursiveComparison()
                .isEqualTo(new RegisterUserRequestDto("username", "password", "fullName"));
    }

}