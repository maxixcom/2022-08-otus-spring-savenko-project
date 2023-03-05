package io.github.maxixcom.otus.movies.backend.config;

import io.github.maxixcom.otus.movies.backend.domain.UserPermission;
import io.github.maxixcom.otus.movies.backend.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .anonymous().principal("guest")
                .and()
                .authorizeRequests()
                .antMatchers( HttpMethod.POST,
                        "/api/v1/category",
                        "/api/v1/movie",
                        "/api/v1/movie/{^[\\d]$}/poster"
                ).hasAuthority(UserPermission.ADMIN_AREA.name())
                .antMatchers(HttpMethod.PUT,
                        "/api/v1/category/*",
                        "/api/v1/movie/*"
                ).hasAuthority(UserPermission.ADMIN_AREA.name())
                .antMatchers(HttpMethod.DELETE,
                        "/api/v1/category/*",
                        "/api/v1/movie/*",
                        "/api/v1/movie/{^[\\d]$}/poster"
                ).hasAuthority(UserPermission.ADMIN_AREA.name())
                .antMatchers("/api/v1/user/**").hasAuthority(UserPermission.USER_AREA.name())
                .antMatchers(
                        "/api/v1/movie/{^[\\d]$}/rate",
                        "/api/v1/profile"
                ).authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .rememberMe().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
