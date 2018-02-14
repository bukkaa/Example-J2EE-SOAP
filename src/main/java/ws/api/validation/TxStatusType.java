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
 * TX status
 */
@ReportAsSingleViolation
@Pattern(regexp = "^[AY]$")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface TxStatusType {

    String message() default "Field 'TX.status' should be either 'A' or 'Y', but actual value is '${validatedValue}'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
