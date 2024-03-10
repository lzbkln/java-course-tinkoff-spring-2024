package edu.java.exception;

import edu.java.dto.responses.ApiErrorResponse;
import edu.java.service.exceptions.ServiceException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperServiceException(ServiceException exception) {
        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(exception);

        return ResponseEntity.status(exception.getHttpStatusCode()).body(errorResponse);
    }

    private ApiErrorResponse buildDefaultErrorResponse(ServiceException exception) {
        HttpStatusCode statusCode = exception.getHttpStatusCode();
        String description = exception.getDescription();
        return buildDefaultErrorResponse(statusCode, description, exception);
    }

    private ApiErrorResponse buildDefaultErrorResponse(
        HttpStatusCode statusCode,
        String description,
        Exception exception
    ) {
        String exceptionName = exception.getClass().getSimpleName();
        String exceptionMessage = exception.getMessage();
        List<String> stacktrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();

        return new ApiErrorResponse(
            description,
            statusCode.toString(),
            exceptionName,
            exceptionMessage,
            stacktrace
        );
    }
}
