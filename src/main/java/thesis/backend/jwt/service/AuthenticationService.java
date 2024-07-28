package thesis.backend.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Request.RegisterRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;

import java.io.IOException;
import java.util.Date;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest);
    public AuthenticationResponse refreshToken(String authHeader) throws IOException;

}
