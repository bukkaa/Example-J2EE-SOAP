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
 * Some 2-digit field.
 */
@ReportAsSingleViolation
@Pattern(regexp = "^\\d{2}$")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface TxBar {

    String message() default "Invalid value for 2-digit field 'TX.bar': '${validatedValue}'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
