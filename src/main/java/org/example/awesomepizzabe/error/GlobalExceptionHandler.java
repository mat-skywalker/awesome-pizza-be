package org.example.awesomepizzabe.error;

import com.matteo.openapi.model.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String STANDARD_ERROR_LOG = "{} because of {}";
    private static final String INPUT_PROVIDED_NOT_VALID = "The input provided is not valid";
    private static final String INPUT_PROVIDED_NOT_VALID_DETAILS = "The input provided must contain: customerName, customerPhoneNumber and at least one item with name and quantity";
    private static final String INPUT_PROVIDED_NOT_VALID_WRONG_TYPE_DETAILS = "The input provided must have the right type format for each field";

    //Spring Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(STANDARD_ERROR_LOG, INPUT_PROVIDED_NOT_VALID, ex.getCause(), ex.getCause());
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getApiErrorBody(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), INPUT_PROVIDED_NOT_VALID, INPUT_PROVIDED_NOT_VALID_DETAILS));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleValidationException(HttpMessageNotReadableException ex) {
        log.error(STANDARD_ERROR_LOG, INPUT_PROVIDED_NOT_VALID, ex.getMessage(), ex.getCause());
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getApiErrorBody(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), INPUT_PROVIDED_NOT_VALID, INPUT_PROVIDED_NOT_VALID_WRONG_TYPE_DETAILS));
    }

    private ApiError getApiErrorBody(LocalDateTime timestamp, Integer status, String error, String message, String details) {
        return new ApiError().timestamp(timestamp).status(status).error(error).message(message).details(details);
    }

}
