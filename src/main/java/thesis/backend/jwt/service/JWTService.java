package thesis.backend.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JWTService {

    public String extractUsername(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);

    public String generateToken(UserDetails userDetails);
    public String generateRefreshToken(UserDetails userDetails);
}
