package com.nam.demojpa.exception;

import com.nam.demojpa.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  private static final String MIN_ATTRIBUTE = "min";

  @ExceptionHandler(value = Exception.class)
  ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
    log.error("Exception: ", e);
    ApiResponse response = new ApiResponse();
    response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiResponse> handleAppException(AppException e) {
    ApiResponse response = new ApiResponse();
    ErrorCode errorCode = e.getErrorCode();
    response.setCode(errorCode.getCode());
    response.setMessage(errorCode.getMessage());
    return ResponseEntity.status(errorCode.getStatusCode()).body(response);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.error("Access denied: ", e);
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatusCode())
        .body(
            ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    String enumKey = e.getFieldError().getDefaultMessage();
    ErrorCode errorCode = ErrorCode.valueOf(ErrorCode.NOT_FOUND.name());
    Map<String, Object> attributes = Map.of();
    try {
      errorCode = ErrorCode.valueOf(enumKey);
      var constraintViolation =
          e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
      attributes = constraintViolation.getConstraintDescriptor().getAttributes();
    } catch (IllegalArgumentException ex) {
    }
    ApiResponse response = new ApiResponse();
    response.setCode(errorCode.getCode());
    response.setMessage(
        Objects.nonNull(attributes)
            ? mapAttributeToMessage(errorCode.getMessage(), attributes)
            : errorCode.getMessage());
    return ResponseEntity.status(errorCode.getStatusCode()).body(response);
  }

  private String mapAttributeToMessage(String message, Map<String, Object> attributes) {
    String minValue = attributes.get(MIN_ATTRIBUTE).toString();
    message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    return message;
  }
}
