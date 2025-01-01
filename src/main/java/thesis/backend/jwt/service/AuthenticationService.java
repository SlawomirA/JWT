package thesis.backend.jwt.service;

import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Request.ChangeUserRequest;
import thesis.backend.jwt.model.Request.RegisterRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;

import java.io.IOException;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest);
    public AuthenticationResponse refreshToken(String authHeader) throws IOException;
    public String refreshTokenById(Long Id) throws IOException;

    public User retrieveUserByEmail(String email);
    public User changeUserByEmail(ChangeUserRequest changeUserRequest);
}
