package com.github.fabiankevin.microwebspringbootstarter.web;


import com.github.fabiankevin.microwebspringbootstarter.exceptions.ApiException;
import com.github.fabiankevin.microwebspringbootstarter.web.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(ApiException ex) {
        log.debug("ApiException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.getHttpStatusCode()))
                .body(new ApiErrorResponse("Resource Error", ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        log.debug("MethodArgumentNotValidException: {}", ex.getMessage(), ex);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ApiErrorResponse("Validation Failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.debug("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Validation Failed", "Malformed JSON request");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(MethodArgumentTypeMismatchException ex) {
        log.debug("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        String errorMessage = String.format("Parameter '%s' must be of type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        return new ApiErrorResponse("Type Mismatch", errorMessage);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.debug("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Method Not Supported",
                String.format("Method %s is not supported for this request. Supported methods are %s", ex.getMethod(), ex.getSupportedHttpMethods()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(ServletRequestBindingException ex) {
        log.debug("ServletRequestBindingException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Request Binding Error", ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception ex) {
        log.debug("ServletRequestBindingException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Internal Server Error", "something went wrong");
    }
}
