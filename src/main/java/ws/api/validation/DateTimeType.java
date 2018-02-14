package ws.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * UTC Date and Time formatted like YYYYMMDDhhmmss
 */
@ReportAsSingleViolation
@Pattern(regexp = "^\\d{14}$")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface DateTimeType {

    String name();

    String message() default "Field '{name}' does not match 'YYYYMMDDhhmmss' pattern: '${validatedValue}'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
