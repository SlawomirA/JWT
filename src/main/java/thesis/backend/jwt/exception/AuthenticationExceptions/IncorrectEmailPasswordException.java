package thesis.backend.jwt.exception.AuthenticationExceptions;

import org.springframework.security.core.AuthenticationException;

public class IncorrectEmailPasswordException extends AuthenticationException {
    public IncorrectEmailPasswordException(String message) {
        super(message);
    }
}
