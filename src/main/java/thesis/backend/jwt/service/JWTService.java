package thesis.backend.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import thesis.backend.jwt.model.MySQL.User;

import java.util.Date;

public interface JWTService {

    public String extractUsername(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);

    public String generateToken(User user);
    public String generateRefreshToken(User user);
}
