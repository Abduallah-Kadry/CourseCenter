package com.app.coursecenter.Validators;

import com.app.coursecenter.Annotaions.ValidImageFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {


    private boolean allowEmpty;

    @Override
    public void initialize(ValidImageFile constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }


    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return allowEmpty; // if it's allowed, pass
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only JPEG or PNG images are allowed")
                    .addConstraintViolation();
            return false;
        }

        // Validate size (e.g. max 5MB)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Image must be less than 5MB")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
