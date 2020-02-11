package com.ncec.companion.controller;

import com.ncec.companion.exception.BusinessLogicException;
import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.exception.InnerLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        StringBuilder errorLogMessage = new StringBuilder("Incorrect user input:\n");
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            errorLogMessage.append(fieldName);
            errorLogMessage.append(" - ");
            errorLogMessage.append(errorMessage);
            errorLogMessage.append("\n");
        });
        String resultErrorMessage = errorLogMessage.toString();
        LOGGER.error(resultErrorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleBusinessLogicException(BusinessLogicException exception) {
        LOGGER.error("Exception occurred in business logic", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException exception) {
        LOGGER.error("Authentication error occurred", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
        LOGGER.error("Authentication error occurred", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect user role");
    }

    @ExceptionHandler(InnerLogicException.class)
    public ResponseEntity<String> handleInnerLogicException(InnerLogicException exception) {
        String errorMessage = "Unfortunately, internal server error has occurred. " +
                "Please contact your admin.";
        LOGGER.error("Exception occurred in inner logic", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        LOGGER.error("Such entity already exists exception", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went terribly wrong on the server side.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        LOGGER.error("No such entity exists exception", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went terribly wrong on the server side.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleOtherExceptions(RuntimeException exception) {
        String errorMessage = "Unfortunately, internal server error has occurred. " +
                "Please contact your admin.";
        LOGGER.error("Unpredicted internal server error has occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
