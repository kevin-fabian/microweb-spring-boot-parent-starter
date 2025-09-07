package com.github.fabiankevin.quickstart.web;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.fabiankevin.quickstart.exceptions.ApiException;
import com.github.fabiankevin.quickstart.web.dto.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(ApiException ex) {
        log.debug("ApiException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.getHttpStatusCode()))
                .body(new ApiErrorResponse("Request Failed", ex.getMessage()));

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
        return new ApiErrorResponse("Invalid Request Parameters", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.debug("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Invalid Request Format", "The request body is not properly formatted or contains invalid JSON");
    }

    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidFormatException(InvalidFormatException ex) {
        log.debug("InvalidFormatException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Data Format Error", ex.getPath().stream()
                .map(ref -> "The value provided for '%s' has an incorrect format" .formatted(ref.getFieldName()))
                .toList());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(MethodArgumentTypeMismatchException ex) {
        log.debug("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        String errorMessage = String.format("Parameter '%s' must be of type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        return new ApiErrorResponse("Type Mismatch", errorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        log.debug("AccessDeniedException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Access Denied",
                "You don't have permission to access this resource");
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.debug("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Method Not Allowed",
                String.format("The %s method is not allowed for this endpoint. Allowed methods are: %s", ex.getMethod(), ex.getSupportedHttpMethods()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(ServletRequestBindingException ex) {
        log.debug("ServletRequestBindingException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Missing Request Parameters", "Required request parameters or headers are missing or invalid");
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handlerMethodValidationException(HandlerMethodValidationException ex) {
        log.debug("HandlerMethodValidationException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Invalid Request",
                ex.getValueResults().getFirst().getResolvableErrors().stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .toList());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception ex) {
        log.debug("handleGenericException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Internal Server Error", "An unexpected error occurred. Please try again later or contact support if the problem persists.");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ApiErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.debug("MaxUploadSizeExceededException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("File Too Large",
                "The uploaded file exceeds the maximum allowed size");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        log.debug("ConstraintViolationException: {}", ex.getMessage(), ex);
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        return new ApiErrorResponse("Validation Failed", errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMissingParams(MissingServletRequestParameterException ex) {
        log.debug("MissingServletRequestParameterException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Missing Parameter",
                String.format("The required parameter '%s' of type '%s' is missing",
                        ex.getParameterName(), ex.getParameterType()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMissingHeader(MissingRequestHeaderException ex) {
        log.debug("MissingRequestHeaderException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Missing Header",
                String.format("The required header '%s' is missing", ex.getHeaderName()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.debug("HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Unsupported Media Type",
                String.format("The content type '%s' is not supported. Supported types are: %s",
                        ex.getContentType(),
                        ex.getSupportedMediaTypes().stream()
                                .map(MediaType::toString)
                                .collect(Collectors.joining(", "))));
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiErrorResponse handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        log.debug("AsyncRequestTimeoutException: {}", ex.getMessage(), ex);
        return new ApiErrorResponse("Request Timeout",
                "The request took too long to process and timed out");
    }
}
