package ws.api.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import path.to.utils.XsBase64;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The Base64 string for 20 bytes only.
 * Our own specific type.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {XsBase64Type.Validator.class})
public @interface XsBase64Type {

    String ERROR_WHEN_DECODING_MSG = "Base64 field '{name}' cannot be decoded: ${validatedValue.getException().getMessage()}";

    String WRONG_LENGTH_MSG = "Field '{name}' should be 20 bytes encoded to Base64, but actual length is ${validatedValue.getBytesLength()} bytes";

    String name();

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<XsBase64Type, XsBase64> {

        @Override
        public void initialize(XsBase64Type constraintAnnotation) {
            // ignored
        }

        @Override
        public boolean isValid(XsBase64 value, ConstraintValidatorContext ctx) {
            if (value == null) {
                return true;
            }

            if (!value.isValid()) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(ERROR_WHEN_DECODING_MSG).addConstraintViolation();
                return false;
            }

            if (value.getBytes().length != 20) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(WRONG_LENGTH_MSG).addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
