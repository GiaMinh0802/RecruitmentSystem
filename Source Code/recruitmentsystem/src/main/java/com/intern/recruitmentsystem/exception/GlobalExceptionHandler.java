package com.fpt.recruitmentsystem.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fpt.recruitmentsystem.util.ResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseMessage> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseMessage> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseMessage> handleConflictException(ConflictException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseMessage> handleForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<ResponseMessage> handleNotAcceptableException(NotAcceptableException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseMessage> handleBindException(BindException e) {
        String errorMessage = "Invalid request";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return new ResponseEntity<>(new ResponseMessage(errorMessage), HttpStatus.BAD_REQUEST);
    }

    // thrown by Spring Security
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseMessage> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        Throwable cause = e.getCause(); // Get the original cause of the exception
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default status

        // Check if the cause is an instance of a known exception type
        if (cause instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } 
        // Add other kinds of exception if any

        return new ResponseEntity<>(new ResponseMessage(cause.getMessage()), status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseMessage> handleBadCredentialsException() {
        return new ResponseEntity<>(new ResponseMessage("Password is wrong!"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GoneException.class)
    public ResponseEntity<ResponseMessage> handleGoneException(GoneException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.GONE);
    }

    // Exception handler for all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleUncaughtException(Exception e) {
        // Store the stack trace to message
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String errorDetails = sw.toString();
    
        // Print the stack trace to the terminal
        e.printStackTrace();

        return new ResponseEntity<>(new ResponseMessage(errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
