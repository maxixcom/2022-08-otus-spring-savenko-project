package io.github.maxixcom.otus.movies.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.github.maxixcom.otus.movies.backend.config.SecurityConfig;
import io.github.maxixcom.otus.movies.backend.dto.MetaDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryCreateDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryEditDto;
import io.github.maxixcom.otus.movies.backend.dto.category.CategoryListDto;
import io.github.maxixcom.otus.movies.backend.exception.CategoryNotFoundException;
import io.github.maxixcom.otus.movies.backend.repository.UserRepository;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import io.github.maxixcom.otus.movies.backend.service.CategoryService;
import io.github.maxixcom.otus.movies.backend.service.token.TokenServiceImpl;
import org.assertj.core.api.Assertions;
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
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() throws Exception {
        List<CategoryDto> items = List.of(new CategoryDto(1L, "title"));
        MetaDto metaDto = new MetaDto();
        metaDto.setPage(0);
        metaDto.setSize(20);
        metaDto.setTotalElements(1);
        metaDto.setTotalPages(1);

        CategoryListDto categoryListDto = new CategoryListDto(items, metaDto);

        Mockito.when(categoryService.getCategories(Mockito.any(), Mockito.any()))
                .thenReturn(categoryListDto);

        String expectedResult = mapper.writeValueAsString(categoryListDto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

    }

    @Test
    void shouldReturnCategory() throws Exception {
        CategoryDto dto = new CategoryDto(1L, "title");

        Mockito.when(categoryService.getCategory(Mockito.anyLong()))
                .thenReturn(dto);

        String expectedResult = mapper.writeValueAsString(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

    @Test
    void shouldNotReturnAnyCategory() throws Exception {
        Mockito.when(categoryService.getCategory(Mockito.eq(1L))).thenThrow(new CategoryNotFoundException("1"));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldCreateCategoryWithAdminPermission() throws Exception {
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(1L);

        CategoryCreateDto expectedDto = new CategoryCreateDto("title");
        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/category")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/category/1"))
                .andExpect(MockMvcResultMatchers.content().string(""));

        ArgumentCaptor<CategoryCreateDto> captorParam = ArgumentCaptor.forClass(CategoryCreateDto.class);
        Mockito.verify(categoryService).createCategory(captorParam.capture());

        CategoryCreateDto actualDto = captorParam.getValue();

        Assertions.assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldBeForbiddenCreatingCategoryByUser() throws Exception {
        CategoryCreateDto expectedDto = new CategoryCreateDto("title");
        String requestBody = mapper.writeValueAsString(expectedDto);
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/category")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldEditCategoryWithAdminPermission() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1L, "title");
        String expectedResult = mapper.writeValueAsString(categoryDto);

        Mockito.when(categoryService.editCategory(Mockito.anyLong(), Mockito.any())).thenReturn(categoryDto);

        CategoryEditDto expectedDto = new CategoryEditDto("title");
        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/category/1")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

        ArgumentCaptor<Long> captorParamId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryEditDto> captorParam = ArgumentCaptor.forClass(CategoryEditDto.class);

        Mockito.verify(categoryService).editCategory(captorParamId.capture(), captorParam.capture());

        CategoryEditDto actualDto = captorParam.getValue();

        Assertions.assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldBeForbiddenEditCategoryByUser() throws Exception {
        CategoryEditDto expectedDto = new CategoryEditDto("title");
        String requestBody = mapper.writeValueAsString(expectedDto);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/category/1")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN_AREA"})
    @Test
    void shouldDeleteCategoryWithAdminPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));

        Mockito.verify(categoryService, Mockito.times(1)).deleteCategories(Mockito.eq(Set.of(1L)));
    }

    @WithMockUser(authorities = {"USER_AREA"})
    @Test
    void shouldFailDeleteCategoryWithUserPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}