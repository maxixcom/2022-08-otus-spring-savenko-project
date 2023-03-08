package io.github.maxixcom.otus.movies.backend.rest;

import io.github.maxixcom.otus.movies.backend.exception.CategoryNotFoundException;
import io.github.maxixcom.otus.movies.backend.exception.IncorrectPasswordException;
import io.github.maxixcom.otus.movies.backend.exception.MovieNotFoundException;
import io.github.maxixcom.otus.movies.backend.exception.NotAuthenticatedException;
import io.github.maxixcom.otus.movies.backend.exception.UserNotFoundException;
import io.github.maxixcom.otus.movies.backend.exception.UsernameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public void usernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        log.warn("Username already exists: %s".formatted(exception.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void categoryNotFoundException(CategoryNotFoundException exception) {
        log.warn("Category not found: %s".formatted(exception.getMessage()));
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void movieNotFoundException(MovieNotFoundException exception) {
        log.warn("Movie not found: %s".formatted(exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void userNotFoundException(UserNotFoundException exception) {
        log.warn("User not found: %s".formatted(exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public void dataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.warn("Constraint violation: %s".formatted(exception.getMessage()));
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public void notAuthenticatedException() {
        log.warn("User is not Authenticated");
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void incorrectPasswordException() {
        log.warn("Incorrect password");
    }
}
