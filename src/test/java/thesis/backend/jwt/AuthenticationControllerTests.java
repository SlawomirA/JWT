package thesis.backend.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import thesis.backend.jwt.controller.AuthenticationController;
import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;
import thesis.backend.jwt.model.Response.Response;
import thesis.backend.jwt.service.AuthenticationService;
import thesis.backend.jwt.utils.Consts;

import java.lang.reflect.Method;
import java.util.Arrays;

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



    @DisplayName("Confirm that the status code 200 is correctly documented in the @ApiResponse annotation")
    @Test
    public void test_status_code_documentation() throws NoSuchMethodException {
        // Given
        AuthenticationController authenticationController = new AuthenticationController();

        // Retrieve the method
        Method method = AuthenticationController.class.getDeclaredMethod("authenticateUser", AuthenticateRequest.class);

        // Retrieve @ApiResponses annotation from the method
        ApiResponses apiResponses = method.getAnnotation(ApiResponses.class);

        // Check if @ApiResponses is present
        if (apiResponses != null) {
            boolean isStatusCode200Documented = Arrays.stream(apiResponses.value())
                    .anyMatch(apiResponse -> apiResponse.responseCode().equals("200"));

            assertTrue(isStatusCode200Documented, "Status code 200 is not documented in the @ApiResponse annotation");
        } else {
            // Fail the test if @ApiResponses is not present
            assertTrue(false, "@ApiResponses annotation is not present on the method");
        }
    }

}
