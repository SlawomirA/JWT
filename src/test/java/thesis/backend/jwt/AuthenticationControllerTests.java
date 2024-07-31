package thesis.backend.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import thesis.backend.jwt.controller.AuthenticationController;
import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;
import thesis.backend.jwt.model.Response.Response;
import thesis.backend.jwt.service.AuthenticationService;
import thesis.backend.jwt.utils.Consts;

@SpringBootTest
public class AuthenticationControllerTests {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Authenticate user with valid email and password")
    @Test
    public void test_authenticate_user_with_valid_credentials() {
        // Prepare
        AuthenticateRequest request = AuthenticateRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User user = User.builder()
                .id(1)
                .email("test@example.com")
                .firstname("John")
                .lastname("Doe")
                .build();

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .user(user)
                .accessToken("mockAccessToken")
                .build();

        // Mock the service
        when(authenticationService.authenticate(request)).thenReturn(authResponse);

        // Test
        ResponseEntity<Response<AuthenticationResponse>> responseEntity = controller.authenticateUser(request);

        // Verify
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(Consts.C200, responseEntity.getBody().getMessage());
        assertEquals(200, responseEntity.getBody().getCode());
        assertNotNull(responseEntity.getBody().getContainedObject().getAccessToken());
        assertEquals("mockAccessToken", responseEntity.getBody().getContainedObject().getAccessToken());
        assertNotNull(responseEntity.getBody().getContainedObject().getUser());
        assertEquals("test@example.com", responseEntity.getBody().getContainedObject().getUser().getEmail());
        assertEquals("John", responseEntity.getBody().getContainedObject().getUser().getFirstname());
        assertEquals("Doe", responseEntity.getBody().getContainedObject().getUser().getLastname());
    }
}
