package io.github.maxixcom.otus.movies.backend.validation;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class MultipartFileSizeValidator implements ConstraintValidator<MultipartFileSize, MultipartFile> {
    private long min;
    private long max;

    @Override
    public void initialize(MultipartFileSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Optional.ofNullable(context).ifPresent(c -> {
            c.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter("min", this.min)
                    .addMessageParameter("max", this.max)
                    .addMessageParameter("size", value.getSize())
                    .addMessageParameter("file", value.getOriginalFilename());
        });

        if (value.getSize() < this.min) {
            return false;
        }

        return value.getSize() <= this.max;
    }
}
