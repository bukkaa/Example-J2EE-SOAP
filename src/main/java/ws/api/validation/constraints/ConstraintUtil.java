package ws.api.validation.constraints;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

class ConstraintUtil {

    /**
     * Checks reference {@code value} for null and adds new constraint violation to
     * the context it is true
     *
     * @return {@code false} if {@code value} is null (i.e. 'not valid') and {@code true} otherwise
     */
    static boolean validateMissingRequiredField(Object value,
                                                ConstraintValidatorContext ctx,
                                                String fieldName) {
        if (value == null) {
            addMissingRequiredFieldViolation(ctx, fieldName);
            return false;
        }
        return true;
    }

    /**
     * Checks List {@code value} for empty and adds new constraint violation to
     * the context it is true
     *
     * @return {@code false} if {@code value.size()} is 0 (i.e. 'not valid') and {@code true} otherwise
     */
    static boolean validateEmptyCollection(List value,
                                           ConstraintValidatorContext ctx,
                                           String listName) {
        if (value != null && value.size() == 0) {
            addEmptyCollectionViolation(ctx, listName);
            return false;
        }
        return true;
    }

    /**
     * Checks reference {@code value} for null and adds new constraint to
     * the context if it is false
     *
     * @return {@code false} if {@code value} is not null (i.e. 'not valid') and {@code true} otherwise
     */
    static boolean validateFieldSpecifiedInInappropriateCase(Object value,
                                                             ConstraintValidatorContext ctx,
                                                             String fieldName) {
        if (value != null) {
            addFieldSpecifiedInInappropriateCaseViolation(ctx, fieldName);
            return false;
        }
        return true;
    }

    static boolean validateCommonCondition(boolean condition, ConstraintValidatorContext ctx, String message) {
        if (!condition) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }

        return true;
    }

    static boolean validateChoice(ConstraintValidatorContext ctx, ChoiceItem ... items) {
        List<ChoiceItem> specifiedItems = new ArrayList<>(3);

        int i = 0;

        do {
            if (items[i].isSpecified()) {
                specifiedItems.add(items[i]);
            }
        } while (++i < items.length);

        if (specifiedItems.size() > 1) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Following elements should not be used together: " +
                            specifiedItems
                                    .stream()
                                    .map(choiceItem -> '\'' + choiceItem.getName() + '\'')
                                    .collect(joining(", ")))
                    .addConstraintViolation();
            return false;

        } else if (specifiedItems.isEmpty()) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(
                        "One of the following elements should be set: " +
                                asList(items)
                                        .stream()
                                        .map(choiceItem -> '\'' + choiceItem.getName() + '\'')
                                        .collect(joining(", ")))
                        .addConstraintViolation();
            return false;
        }

        return true;
    }

    private static void addMissingRequiredFieldViolation(ConstraintValidatorContext ctx, String fieldName) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate("Missing required field '" + fieldName + "'")
                .addConstraintViolation();
    }

    private static void addEmptyCollectionViolation(ConstraintValidatorContext ctx, String listName) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate("Empty Collection '" + listName + "' is not allowed")
                .addConstraintViolation();
    }

    private static void addFieldSpecifiedInInappropriateCaseViolation(ConstraintValidatorContext ctx,
                                                                      String fieldName) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate("Field '" + fieldName + "' specified in inappropriate case")
                .addConstraintViolation();
    }

    static class ChoiceItem {

        private final String name;

        private final boolean specified;

        ChoiceItem(String name, boolean specified) {
            this.name = name;
            this.specified = specified;
        }

        String getName() {
            return name;
        }

        boolean isSpecified() {
            return specified;
        }
    }
}
