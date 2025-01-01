package thesis.backend.jwt.model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Request model for changingg user password and email by email
 */

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserRequest {
    private String oldEmail;
    private String newEmail;
    private String password;
}
