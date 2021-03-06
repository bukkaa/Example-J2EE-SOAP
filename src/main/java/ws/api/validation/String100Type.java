package ws.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ws.api.validation.String100Type.MAX;
import static ws.api.validation.String100Type.MIN;

/**
 * Simple string with a length of 1 to 100 characters.
 */
@ReportAsSingleViolation
@Size(min = MIN, max = MAX)
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface String100Type {

    int MIN = 1;

    int MAX = 100;

    String name();

    String message() default
            "Field '{name}' should contain from " + MIN +
                    " to " + MAX + " characters, but actual length is ${validatedValue.length()}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
