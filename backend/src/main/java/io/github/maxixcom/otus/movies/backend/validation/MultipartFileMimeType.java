package io.github.maxixcom.otus.movies.backend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileMimeTypeValidator.class)
@Documented
public @interface MultipartFileMimeType {
    String message() default "{validation.MultipartFileMimeType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] mimeTypes() default {
            "image/png",
            "image/jpeg",
            "image/gif"
    };
}
