package thesis.backend.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import thesis.backend.jwt.model.MySQL.User;
import thesis.backend.jwt.model.Request.AuthenticateRequest;
import thesis.backend.jwt.model.Request.ChangeUserRequest;
import thesis.backend.jwt.model.Request.RegisterRequest;
import thesis.backend.jwt.model.Response.AuthenticationResponse;
import thesis.backend.jwt.model.Response.Response;
import thesis.backend.jwt.service.AuthenticationService;
import thesis.backend.jwt.utils.Consts;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
@RestController
@RequestMapping("authenticationController")
@Scope("request")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registers user to the application.",
            description = "Creates account in the application, returns created user and its token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<AuthenticationResponse>> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        authenticationService.register(registerRequest)));
    }


    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate user",
            description = "Authenticate user with e-mail and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<AuthenticationResponse>> authenticateUser(@RequestBody AuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        authenticationService.authenticate(authenticateRequest)));
    }


    @GetMapping(value = "/retrieve-user-by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieves user by email",
            description = "Retrieves user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<User>> retrieveUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(new Response<>(
                Consts.C200,
                200,
                "",
                authenticationService.retrieveUserByEmail(email)));
    }

    @PostMapping(value = "/change-user-password-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Changes user password",
            description = "Changes user password and email by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<User>> changeUserPasswordAndEmailByEmail(@RequestBody ChangeUserRequest changeUserRequest) {
        return ResponseEntity.ok(new Response<>(
                Consts.C200,
                200,
                "",
                authenticationService.changeUserByEmail(changeUserRequest)));
    }

}
