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
 * Time formatted like YYYYMMDD HH:MM:SS
 */
@ReportAsSingleViolation
@Pattern(regexp = "^\\d{8} \\d{2}:\\d{2}:\\d{2}$")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface TxTime {

    String message() default "Field 'TX.time' does not match pattern 'YYYYMMDD HH:MM:SS': '${validatedValue}'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
