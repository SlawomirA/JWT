package thesis.backend.jwt.exception.TokenException;

import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class TokenRefreshException extends IOException {
    public TokenRefreshException(String message) {
        super(message);
    }

}
