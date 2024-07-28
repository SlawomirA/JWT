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
import thesis.backend.jwt.service.JWTService;

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
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION_TIME);
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
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
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
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
}
