package thesis.backend.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface UserDetailsService {

    public String extractUsername(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);
    public String generateToken(UserDetails userDetails);
}
