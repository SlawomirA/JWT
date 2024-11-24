package thesis.backend.jwt.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.DefaultMessageCodesResolver;
import thesis.backend.jwt.model.Data.CustomUserDetails;
import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.service.JWTService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of JWTService interface providing methods for JWT token generation, extraction, and validation.
 */
@Service
public class JWTServiceImpl implements JWTService {


    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time}")
    private long JWT_EXPIRATION_TIME;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, mapToCustomUserDetails(user), JWT_EXPIRATION_TIME);
    }

    public CustomUserDetails mapToCustomUserDetails(User user) {
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRole().getAuthorities(),
                true, // accountNonExpired
                true, // accountNonLocked
                true, // credentialsNonExpired
                true, // enabled
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }

    /**
     * Generates a JWT token based on the provided extra claims, user details, and expiration time.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails the user details for whom the token is generated
     * @param expiration the expiration time for the token
     * @return the generated JWT token
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            CustomUserDetails userDetails,
            long expiration
    ) {
        // Create a claims map and populate it with userDetails data
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("firstname", userDetails.getFirstname());
        claims.put("lastname", userDetails.getLastname());
        claims.put("email", userDetails.getEmail());
        claims.put("authorities", userDetails.getAuthorities());
        claims.put("accountNonExpired", userDetails.isAccountNonExpired());
        claims.put("accountNonLocked", userDetails.isAccountNonLocked());
        claims.put("credentialsNonExpired", userDetails.isCredentialsNonExpired());
        claims.put("enabled", userDetails.isEnabled());

        // Build the JWT with all the claims
        return Jwts.builder()
                .setClaims(claims)
                .claim("iat", System.currentTimeMillis() / 1000) // Issued at in seconds
                .claim("exp", (System.currentTimeMillis() + expiration) / 1000) // Expiration in seconds
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Retrieves the signing key used for HMAC-SHA signature generation by decoding the secret key from
     * BASE64 format.
     *
     * @return the signing key for JWT token generation
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    private SecretKey getSecretSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public long getExpirationTime() {
        return JWT_EXPIRATION_TIME;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public String generateRefreshToken(
            User user
    ) {
        return buildToken(new HashMap<>(), mapToCustomUserDetails(user), refreshExpiration);
    }
}
