package com.secure_sentinel.accounts.accounts_service.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleNotFoundException_WithRuntimeException_ShouldReturnInternalError() {
        // Arrange
        RuntimeException ex = new RuntimeException("foo");

        // Act
        ResponseEntity<ApiError> actualHandleNotFoundExceptionResult =
                globalExceptionHandler.handleNotFoundException(ex,
                new ServletWebRequest(new MockHttpServletRequest()));

        // Assert
        ApiError body = actualHandleNotFoundExceptionResult.getBody();
        assertEquals("foo", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(500, actualHandleNotFoundExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.getStatus());
        assertTrue(actualHandleNotFoundExceptionResult.hasBody());
        assertTrue(actualHandleNotFoundExceptionResult.getHeaders().isEmpty());
    }

    @Test
    void handleNotFoundException_WithCustomRequest_ShouldReturnInternalError() {
        // Arrange
        RuntimeException ex = new RuntimeException("foo");

        // Act
        ResponseEntity<ApiError> actualHandleNotFoundExceptionResult =
                globalExceptionHandler.handleNotFoundException(ex,
                new ServletWebRequest(mock(HttpServletRequest.class)));

        // Assert
        ApiError body = actualHandleNotFoundExceptionResult.getBody();
        assertEquals("foo", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(500, actualHandleNotFoundExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.getStatus());
        assertTrue(actualHandleNotFoundExceptionResult.hasBody());
        assertTrue(actualHandleNotFoundExceptionResult.getHeaders().isEmpty());
    }

    @Test
    void handleBadRequestException_WithRuntimeException_ShouldReturnBadRequest() {
        // Arrange and Act
        ResponseEntity<ApiError> actualHandleBadRequestExceptionResult = globalExceptionHandler
                .handleBadRequestException(new RuntimeException("foo"));

        // Assert
        ApiError body = actualHandleBadRequestExceptionResult.getBody();
        assertEquals("foo", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(400, actualHandleBadRequestExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, body.getStatus());
        assertTrue(actualHandleBadRequestExceptionResult.hasBody());
        assertTrue(actualHandleBadRequestExceptionResult.getHeaders().isEmpty());
    }

    @Test
    void handleMethodArgumentNotValid_WithBindException_ShouldReturnBadRequest() {
        // Arrange and Act
        ResponseEntity<ApiError> actualHandleMethodArgumentNotValidResult = globalExceptionHandler
                .handleMethodArgumentNotValid(
                        new MethodArgumentNotValidException((MethodParameter) null, new BindException("Target",
                                "Object Name")));

        // Assert
        ApiError body = actualHandleMethodArgumentNotValidResult.getBody();
        assertEquals("Validation error", body.getMessage());
        assertEquals(400, actualHandleMethodArgumentNotValidResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, body.getStatus());
        assertTrue(body.getErrorDetails().isEmpty());
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidResult.getHeaders().isEmpty());
    }

    @Test
    void handleMethodArgumentNotValid_WithCustomParameter_ShouldReturnBadRequest() {
        // Arrange
        MethodParameter parameter = mock(MethodParameter.class);

        // Act
        ResponseEntity<ApiError> actualHandleMethodArgumentNotValidResult = globalExceptionHandler
                .handleMethodArgumentNotValid(
                        new MethodArgumentNotValidException(parameter, new BindException("Target", "Object Name")));

        // Assert
        ApiError body = actualHandleMethodArgumentNotValidResult.getBody();
        assertEquals("Validation error", body.getMessage());
        assertEquals(400, actualHandleMethodArgumentNotValidResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, body.getStatus());
        assertTrue(body.getErrorDetails().isEmpty());
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidResult.getHeaders().isEmpty());
    }


    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleAccessDeniedException(AccessDeniedException)}
     */
    @Test
    void testHandleAccessDeniedException() {
        ResponseEntity<ApiError> actualHandleAccessDeniedExceptionResult = globalExceptionHandler
                .handleAccessDeniedException(new AccessDeniedException("Msg"));
        ApiError body = actualHandleAccessDeniedExceptionResult.getBody();
        assertEquals("Access Denied", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(403, actualHandleAccessDeniedExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.FORBIDDEN, body.getStatus());
        assertTrue(actualHandleAccessDeniedExceptionResult.hasBody());
        assertTrue(actualHandleAccessDeniedExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleAuthenticationException(AuthenticationException)}
     */
    @Test
    void testHandleAuthenticationException() {
        ResponseEntity<ApiError> actualHandleAuthenticationExceptionResult = globalExceptionHandler
                .handleAuthenticationException(new AccountExpiredException("Msg"));
        ApiError body = actualHandleAuthenticationExceptionResult.getBody();
        assertEquals("Authentication Failed", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(401, actualHandleAuthenticationExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.UNAUTHORIZED, body.getStatus());
        assertTrue(actualHandleAuthenticationExceptionResult.hasBody());
        assertTrue(actualHandleAuthenticationExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleLockingFailure(Exception)}
     */
    @Test
    void testHandleLockingFailure() {
        ResponseEntity<ApiError> actualHandleLockingFailureResult = globalExceptionHandler
                .handleLockingFailure(new Exception("foo"));
        ApiError body = actualHandleLockingFailureResult.getBody();
        assertEquals("Locking failure: foo", body.getMessage());
        assertNull(body.getErrorDetails());
        assertEquals(409, actualHandleLockingFailureResult.getStatusCodeValue());
        assertEquals(HttpStatus.CONFLICT, body.getStatus());
        assertTrue(actualHandleLockingFailureResult.hasBody());
        assertTrue(actualHandleLockingFailureResult.getHeaders().isEmpty());
    }

    @Test
    void determineHttpStatus_WithRuntimeException_ShouldReturnInternalServerError() {
        // Arrange, Act and Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                globalExceptionHandler.determineHttpStatus(new RuntimeException("foo")));
    }

    @Test
    void determineHttpStatus_WithAccountNotFoundException_ShouldReturnNotFound() {
        // Arrange
        RuntimeException exception = new AccountNotFoundException("Account not found");

        // Act
        HttpStatus result = globalExceptionHandler.determineHttpStatus(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    void determineHttpStatus_WithUserNotFoundException_ShouldReturnNotFound() {
        // Arrange
        RuntimeException exception = new UserNotFoundException("User not found");

        // Act
        HttpStatus result = globalExceptionHandler.determineHttpStatus(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    void determineHttpStatus_WithInsufficientFundsException_ShouldReturnBadRequest() {
        // Arrange
        RuntimeException exception = new InsufficientFundsException("Insufficient funds");

        // Act
        HttpStatus result = globalExceptionHandler.determineHttpStatus(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void determineHttpStatus_WithUnsupportedAccountOperationException_ShouldReturnBadRequest() {
        // Arrange
        RuntimeException exception = new UnsupportedAccountOperationException("Unsupported operation");

        // Act
        HttpStatus result = globalExceptionHandler.determineHttpStatus(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void determineHttpStatus_WithOtherRuntimeException_ShouldReturnInternalServerError() {
        // Arrange
        RuntimeException exception = new RuntimeException("foo");

        // Act
        HttpStatus result = globalExceptionHandler.determineHttpStatus(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }

    @Test
    void extractErrorDetails_WithRuntimeException_ShouldReturnNull() {
        // Arrange, Act and Assert
        assertNull(globalExceptionHandler.extractErrorDetails(new RuntimeException("foo")));
    }

    @Test
    void extractErrorDetails_WithInsufficientFundsException_ShouldReturnErrorDetails() {
        // Arrange
        Map<String, Object> expectedDetails = new HashMap<>();
        expectedDetails.put("detailKey", "detailValue");
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds", expectedDetails);

        // Act
        Map<String, Object> result = globalExceptionHandler.extractErrorDetails(exception);

        // Assert
        assertEquals(expectedDetails, result);
    }

    @Test
    void extractErrorDetails_WithUnsupportedAccountOperationException_ShouldReturnErrorDetails() {
        // Arrange
        Map<String, Object> expectedDetails = new HashMap<>();
        expectedDetails.put("detailKey", "detailValue");
        UnsupportedAccountOperationException exception = new UnsupportedAccountOperationException(
                "Unsupported " + "operation", expectedDetails);

        // Act
        Map<String, Object> result = globalExceptionHandler.extractErrorDetails(exception);

        // Assert
        assertEquals(expectedDetails, result);
    }
}