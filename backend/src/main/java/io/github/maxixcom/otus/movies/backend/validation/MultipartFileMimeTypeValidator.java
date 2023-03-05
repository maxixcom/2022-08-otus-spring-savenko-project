package io.github.maxixcom.otus.movies.backend.validation;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MultipartFileMimeTypeValidator implements ConstraintValidator<MultipartFileMimeType, MultipartFile> {
    private Set<MimeType> mimeTypes = Collections.emptySet();

    @Override
    public void initialize(MultipartFileMimeType constraintAnnotation) {
        this.mimeTypes = Arrays.stream(constraintAnnotation.mimeTypes())
                .map(MimeTypeUtils::parseMimeType)
                .collect(Collectors.toSet());

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String contentType = value.getContentType();
        if (contentType == null) {
            return false;
        }

        Optional.ofNullable(context).ifPresent(c -> {
            c.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter("mimetype", contentType)
                    .addMessageParameter("file", value.getOriginalFilename());
        });

        return mimeTypes.contains(MimeType.valueOf(contentType));
    }
}
