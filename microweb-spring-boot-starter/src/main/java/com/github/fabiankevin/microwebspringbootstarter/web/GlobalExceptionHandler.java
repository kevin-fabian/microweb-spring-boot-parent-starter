package com.github.fabiankevin.microwebspringbootstarter.web;


import com.github.fabiankevin.microwebspringbootstarter.exceptions.ApiException;
import com.github.fabiankevin.microwebspringbootstarter.web.dto.ApiErrorResponse;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(ApiException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.getHttpStatusCode()))
                .body(new ApiErrorResponse("Resource Error", ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ApiErrorResponse("Validation Failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleHttpMessageNotReadableException() {
        return new ApiErrorResponse("Validation Failed", "Malformed JSON request");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Parameter '%s' must be of type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        return new ApiErrorResponse("Type Mismatch", errorMessage);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiErrorResponse handle(HttpRequestMethodNotSupportedException e) {
        return new ApiErrorResponse("Method Not Supported",
                String.format("Method %s is not supported for this request. Supported methods are %s", e.getMethod(), e.getSupportedHttpMethods()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiErrorResponse handle(ServletRequestBindingException e) {
        return new ApiErrorResponse("Request Binding Error", e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception ex) {
        return new ApiErrorResponse("Internal Server Error", "something went wrong");
    }
}
