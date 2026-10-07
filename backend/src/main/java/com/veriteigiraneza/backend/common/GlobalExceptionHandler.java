package com.veriteigiraneza.backend.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.veriteigiraneza.backend.user.EmailInUseException;

// @RestControllerAdvice: applies to every controller in the app.
// When a controller (or the service it calls) throws an exception,
// Spring looks here for a matching @ExceptionHandler method.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Normal case: the service's duplicate check found the email.
    @ExceptionHandler(EmailInUseException.class)
    public ProblemDetail handleEmailInUse(EmailInUseException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Rare race case: two sign-ups with the same email at the same moment.
    // The service check passes for both, and the database's unique
    // constraint rejects the second insert.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        // Generic message on purpose: ex.getMessage() here contains raw SQL and
        // constraint names, which shouldn't be exposed to clients.
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "The request conflicts with existing data.");
    }

    // Invalid input: @Valid failed on the request body.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");

        // Field name -> what's wrong with it, e.g. "email" -> "must be a well-formed email address".
        // LinkedHashMap keeps the fields in the order they were added.
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Adds a custom "errors" field to the standard ProblemDetail JSON.
        problem.setProperty("errors", errors);
        return problem;
    }
}