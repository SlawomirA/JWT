package thesis.backend.jwt.model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom object for response class.
 * message - custom message returned by endpoint
 */

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {
    private String email;
    private String password;
}
