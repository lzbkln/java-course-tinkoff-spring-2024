package edu.java.exception;

import edu.java.dto.responses.ApiErrorResponse;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

    @ExceptionHandler(AlreadyTrackedLinkException.class)
    public ResponseEntity<ApiErrorResponse> alreadyTrackedLinkException(AlreadyTrackedLinkException exception) {
        String description = "Link is already tracked";

        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(HttpStatus.CONFLICT, description, exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NonRegisterChatException.class)
    public ResponseEntity<ApiErrorResponse> nonRegisterChatException(NonRegisterChatException exception) {
        String description = "Telegram chat doesnt register";

        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(HttpStatus.NOT_FOUND, description, exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoSuchLinkException.class)
    public ResponseEntity<ApiErrorResponse> noSuchLinkException(NoSuchLinkException exception) {
        String description = "Link isn't tracking";

        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(HttpStatus.CONFLICT, description, exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ReRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> reRegistrationException(ReRegistrationException exception) {
        String description = "Double registration";

        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(HttpStatus.CONFLICT, description, exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
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
