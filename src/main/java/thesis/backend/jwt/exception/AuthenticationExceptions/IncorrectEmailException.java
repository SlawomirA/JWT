package thesis.backend.jwt.exception.AuthenticationExceptions;

import org.springframework.security.core.AuthenticationException;

public class IncorrectEmailException extends AuthenticationException {
    public IncorrectEmailException(String message) {
        super(message);
    }
}
