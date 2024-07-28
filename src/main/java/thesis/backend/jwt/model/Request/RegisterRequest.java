package thesis.backend.jwt.model.Request;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import thesis.backend.jwt.enums.Role;

/**
 * Custom object for response class.
 * message - custom message returned by endpoint
 */

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
