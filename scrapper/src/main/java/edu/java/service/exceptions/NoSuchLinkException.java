package edu.java.service.exceptions;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NoSuchLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link isn't tracking: %s";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public NoSuchLinkException(String message) {
        super(message);
    }

    public NoSuchLinkException(URI url) {
        super(DESCRIPTION.formatted(url));
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
