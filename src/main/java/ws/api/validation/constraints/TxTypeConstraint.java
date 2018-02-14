package ws.api.validation.constraints;


import ws.api.TX;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ws.api.validation.constraints.ConstraintUtil.validateMissingRequiredField;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {TxTypeConstraint.Validator.class})
public @interface TxTypeConstraint {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<TxTypeConstraint, PayerAuthentication.TX> {

        @Override
        public void initialize(TxTypeConstraint constraintAnnotation) {
            // ignored
        }

        @Override
        public boolean isValid(TX value, ConstraintValidatorContext context) {
            return value == null
                    || (validateMissingRequiredField(value.time, context, "TX.time")
                        & validateMissingRequiredField(value.status, context, "TX.status")
                        & validateMissingRequiredField(value.bar, context, "TX.bar"));

        }
    }
}
