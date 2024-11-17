package thesis.backend.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesis.backend.jwt.model.Response.AuthenticationResponse;
import thesis.backend.jwt.model.Response.Response;
import thesis.backend.jwt.service.AuthenticationService;
import thesis.backend.jwt.utils.Consts;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("jwtController")
@Scope("request")
public class JWTController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refreshes token of user",
            description = "Refreshes token of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed"),
    })
    public ResponseEntity<Response<AuthenticationResponse>> refreshTokenOfUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws IOException {
        return ResponseEntity.ok(new Response<>(
                Consts.C200,
                200,
                "",
                authenticationService.refreshToken(authHeader)));
    }


}
