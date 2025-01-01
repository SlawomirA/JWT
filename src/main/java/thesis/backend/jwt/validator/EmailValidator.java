package thesis.backend.jwt.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
public class EmailValidator implements Validator<String> {

    public static final String EMAIL_CANNOT_BE_NULL = "Email cannot be null  ";
    public static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty  ";
    public static final String EMAIL_HAS_INVALID_FORMAT = "Email has an invalid format  ";
    public static final String EMAIL_TOO_LONG = "Email cannot exceed 254 characters  ";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private final List<String> validationErrors = new ArrayList<>();

    private Optional<String> validateEmailIsNotNull(String email) {
        return Objects.isNull(email) ? Optional.of(EMAIL_CANNOT_BE_NULL) : Optional.empty();
    }

    private Optional<String> validateEmailIsNotEmpty(String email) {
        return email.isEmpty() ? Optional.of(EMAIL_CANNOT_BE_EMPTY) : Optional.empty();
    }

    private Optional<String> validateEmailFormat(String email) {
        return !EMAIL_PATTERN.matcher(email).matches() ? Optional.of(EMAIL_HAS_INVALID_FORMAT) : Optional.empty();
    }

    private Optional<String> validateEmailLength(String email) {
        return email.length() > 254 ? Optional.of(EMAIL_TOO_LONG) : Optional.empty();
    }

    @Override
    public boolean validate(String email) {
        validationErrors.clear();

        validateEmailIsNotNull(email).ifPresent(validationErrors::add);
        if (!validationErrors.isEmpty()) {
            return false;
        }
        validateEmailIsNotEmpty(email).ifPresent(validationErrors::add);
        validateEmailFormat(email).ifPresent(validationErrors::add);
        validateEmailLength(email).ifPresent(validationErrors::add);

        return validationErrors.isEmpty();
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(sb::append);
            return sb.toString();
        } else {
            return "No validation errors found";
        }
    }
}
