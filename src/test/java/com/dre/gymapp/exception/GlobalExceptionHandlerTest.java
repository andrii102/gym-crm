package com.dre.gymapp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler =  new GlobalExceptionHandler();

    @Test
    public void handleNotFoundException_ShouldReturn404() {
        NotFoundException exception = new NotFoundException("Not Found");

        ResponseEntity<String> response = handler.handleNotFoundError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
    }

    @Test
    public void handleInternalServerError_ShouldReturn500() {
        Exception ex = new Exception("Internal Server Error");

        ResponseEntity<String> response = handler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server error: Internal Server Error", response.getBody());
    }

    @Test
    public void handleBadRequest_ShouldReturn400() {
        BadRequestException exception = new BadRequestException("Bad Request");

        ResponseEntity<String> response = handler.handleBadRequestError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void handleUnauthorized_ShouldReturn401() {
        UnauthorizedException exception = new UnauthorizedException("Unauthorized");

        ResponseEntity<String> response = handler.handleUnauthorizedError(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

}
