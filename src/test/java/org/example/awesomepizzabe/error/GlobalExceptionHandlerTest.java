package org.example.awesomepizzabe.error;

import com.matteo.openapi.model.ApiError;
import org.example.awesomepizzabe.error.exceptions.NoInPreparationOrderException;
import org.example.awesomepizzabe.error.exceptions.NoPendingOrdersException;
import org.example.awesomepizzabe.error.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptionReturnsBadRequest() {
        var response = handler.handleValidationException(mock(MethodArgumentNotValidException.class));

        assertApiError(response.getStatusCode(), response.getBody(), HttpStatus.BAD_REQUEST,
                "The input provided is not valid",
                "The input provided must contain: customerName, customerPhoneNumber and at least one item with name and quantity");
    }

    @Test
    void handleValidationTypeExceptionReturnsBadRequest() {
        var response = handler.handleValidationTypeException(
                new HttpMessageNotReadableException("Invalid JSON", null)
        );

        assertApiError(response.getStatusCode(), response.getBody(), HttpStatus.BAD_REQUEST,
                "The input provided is not valid",
                "The input provided must have the right type format for each field");
    }

    @Test
    void handleRecordNotFoundExceptionReturnsNotFound() {
        var response = handler.handleRecordNotFoundException(
                new RecordNotFoundException("Missing order")
        );

        assertApiError(response.getStatusCode(), response.getBody(), HttpStatus.NOT_FOUND,
                "Order not found", "Order not found");
    }

    @Test
    void handleNoPendingOrdersExceptionReturnsNotFound() {
        var response = handler.handleNoPendingOrdersException(
                new NoPendingOrdersException("No pending orders")
        );

        assertApiError(response.getStatusCode(), response.getBody(), HttpStatus.NOT_FOUND,
                "No pending orders", "No pending orders");
    }

    @Test
    void handleNoInPreparationOrderExceptionReturnsBadRequest() {
        var response = handler.handleNoInPreparationOrderException(
                new NoInPreparationOrderException("Order is not being prepared")
        );

        assertApiError(response.getStatusCode(), response.getBody(), HttpStatus.BAD_REQUEST,
                "The order is not being prepared", "The order is not being prepared");
    }

    private void assertApiError(
            HttpStatusCode responseStatus,
            ApiError body,
            HttpStatus expectedStatus,
            String expectedMessage,
            String expectedDetails
    ) {
        assertAll(
                () -> assertEquals(expectedStatus.value(), responseStatus.value()),
                () -> assertNotNull(body),
                () -> assertNotNull(body.getTimestamp()),
                () -> assertEquals(expectedStatus.value(), body.getStatus()),
                () -> assertEquals(expectedStatus.getReasonPhrase(), body.getError()),
                () -> assertEquals(expectedMessage, body.getMessage()),
                () -> assertEquals(expectedDetails, body.getDetails())
        );
    }
}
