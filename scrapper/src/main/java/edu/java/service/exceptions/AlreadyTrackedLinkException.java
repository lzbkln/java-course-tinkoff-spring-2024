package edu.java.service.exceptions;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AlreadyTrackedLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link is already tracked %s";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public AlreadyTrackedLinkException(String message) {
        super(message);
    }

    public AlreadyTrackedLinkException(URI link) {
        super(DESCRIPTION.formatted(link));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return HTTP_STATUS_CODE;
    }
}
