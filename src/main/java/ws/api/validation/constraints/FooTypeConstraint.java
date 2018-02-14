package ws.api.validation.constraints;


import ws.api.Foo;

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
@Constraint(validatedBy = {FooTypeConstraint.Validator.class})
public @interface FooTypeConstraint {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<FooTypeConstraint, Foo> {

        @Override
        public void initialize(FooTypeConstraint constraintAnnotation) {
            // ignored
        }

        @Override
        public boolean isValid(Foo value, ConstraintValidatorContext context) {
            return value == null
                    || (validateMissingRequiredField(value.time, context, "Foo.time")
                        & validateMissingRequiredField(value.status, context, "Foo.status")
                        & validateMissingRequiredField(value.bar, context, "Foo.bar"));

        }
    }
}
