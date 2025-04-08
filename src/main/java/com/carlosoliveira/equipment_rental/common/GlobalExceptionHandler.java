package com.carlosoliveira.equipment_rental.common;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.put("message", ex.getMessage());

        String[] stackTraceLines = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .map(line -> "    at " + line)
                .toArray(String[]::new);
        String[] causeLines = ex.getCause() != null
                ? Arrays.stream(ex.getCause().getStackTrace())
                .map(StackTraceElement::toString)
                .map(line -> "    at " + line)
                .toArray(String[]::new)
                : new String[0];

        // Combine exception message, stack trace, and cause
        String[] fullTrace = new String[1 + stackTraceLines.length + (ex.getCause() != null ? 1 + causeLines.length : 0)];
        fullTrace[0] = ex.toString();
        System.arraycopy(stackTraceLines, 0, fullTrace, 1, stackTraceLines.length);
        if (ex.getCause() != null) {
            fullTrace[stackTraceLines.length + 1] = "Caused by: " + ex.getCause().toString();
            System.arraycopy(causeLines, 0, fullTrace, stackTraceLines.length + 2, causeLines.length);
        }

        response.put("trace", fullTrace); // Array instead of string
        response.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
