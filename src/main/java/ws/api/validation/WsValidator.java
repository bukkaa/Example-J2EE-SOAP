package ws.api.validation;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WsValidator {

    public static int DEFAULT_MSG_RESTRICTION = 4000;

    private Validator validator;

    public WsValidator(@Nonnull Validator validator) {
        this.validator = validator;
    }

    @Nonnull
    public String validate2SingleMessage(@Nonnull Object wsObject) {
        return validate2SingleMessage(wsObject, DEFAULT_MSG_RESTRICTION);
    }

    @Nonnull
    public String validate2SingleMessage(@Nonnull Object wsObject, int msgLengthRestriction) {
        String msg = validate(wsObject)
                .stream()
                .filter(s -> !s.isEmpty())
                .collect(joining(". "));

        return msg.length() > msgLengthRestriction ?
                msg.substring(0, msgLengthRestriction) :
                msg;
    }

    @Nonnull
    public List<String> validate(@Nonnull Object wsObject) {
        return validator.validate(wsObject)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(toList());
    }
}
