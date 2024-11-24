package thesis.backend.jwt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thesis.backend.jwt.exception.AuthenticationExceptions.GeneralAuthenticationException;
import thesis.backend.jwt.exception.AuthenticationExceptions.IncorrectEmailPasswordException;
import thesis.backend.jwt.exception.TokenException.TokenRefreshException;
import thesis.backend.jwt.model.MySQL.Token;
import thesis.backend.jwt.enums.TokenType;
import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Request.RegisterRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;
import thesis.backend.jwt.repository.TokenRepository;
import thesis.backend.jwt.repository.UserRepository;
import thesis.backend.jwt.service.AuthenticationService;
import thesis.backend.jwt.service.JWTService;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .user(savedUser)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest)
    {
        //Check if email&password are correct
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateRequest.getEmail(),
                            authenticateRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new IncorrectEmailPasswordException("Invalid email or password");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GeneralAuthenticationException("General authentication failed");
        }

        //Generate token and send it back
        var user = userRepository.findByEmail(authenticateRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public AuthenticationResponse refreshToken(String authHeader) throws TokenRefreshException{
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new TokenRefreshException("Auth header is null or does not start with \"Bearer\" prefix");

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        throw new TokenRefreshException("Invalid refresh token");
    }
}

