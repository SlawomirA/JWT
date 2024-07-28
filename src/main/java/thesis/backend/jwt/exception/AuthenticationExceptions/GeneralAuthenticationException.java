package thesis.backend.jwt.exception.AuthenticationExceptions;

import org.springframework.security.core.AuthenticationException;

public class GeneralAuthenticationException extends AuthenticationException {
    public GeneralAuthenticationException(String message) {
        super(message);
    }
}
