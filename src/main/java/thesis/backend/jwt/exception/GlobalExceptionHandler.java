package thesis.backend.jwt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thesis.backend.jwt.exception.AuthenticationExceptions.GeneralAuthenticationException;
import thesis.backend.jwt.exception.AuthenticationExceptions.IncorrectEmailPasswordException;
import thesis.backend.jwt.exception.TokenException.TokenRefreshException;
import thesis.backend.jwt.model.Response.Response;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response<Void>> handleNullPointerException(NullPointerException e) {
        log.error("Null Pointer Exception occurred: ", e);
        Response<Void> response = new Response<>("Null Pointer Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response<Void>> handleNoSuchElementException(NoSuchElementException e) {
        log.error("No such element Exception occurred: ", e);
        Response<Void> response = new Response<>("No such element exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<Response<Void>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        log.error("Array Index Out Of Bounds Exception occurred: ", e);
        Response<Void> response = new Response<>("Array Index Out Of Bounds Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal Argument Exception occurred: ", e);
        Response<Void> response = new Response<>("Illegal Argument Exception occurred", HttpStatus.BAD_REQUEST.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralAuthenticationException.class)
    public ResponseEntity<Response<Void>> handleGeneralAuthenticationException(GeneralAuthenticationException e) {
        log.error("A general authenticatton exception occured: ", e);
        Response<Void> response = new Response<>("A general authenticaton exception occured", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IncorrectEmailPasswordException.class)
    public ResponseEntity<Response<Void>> handleIncorrectEmailPasswordException(IncorrectEmailPasswordException e) {
        log.error("A general authentication exception occured: ", e);
        Response<Void> response = new Response<>("A general authenticaton exception occured", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<Response<Void>> handleTokenRefreshException(TokenRefreshException ex) {
        Response<Void> response = new Response<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGenericException(Exception e) {
        log.error("An exception occurred: ", e);
        Response<Void> response = new Response<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}