package edu.java.bot.exception;

import edu.java.bot.dto.responses.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> methodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String description = Arrays.toString(exception.getDetailMessageArguments());
        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(statusCode, description, exception);

        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> exception(
        Exception exception
    ) {
        HttpStatusCode statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        String description = "Internal Server Error";
        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(statusCode, description, exception);

        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> httpMessageNotReadableException(
        HttpMessageNotReadableException exception
    ) {
        HttpStatusCode statusCode = HttpStatus.BAD_REQUEST;
        String description = "Incorrect message";
        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(statusCode, description, exception);

        return ResponseEntity.status(statusCode).body(errorResponse);
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
