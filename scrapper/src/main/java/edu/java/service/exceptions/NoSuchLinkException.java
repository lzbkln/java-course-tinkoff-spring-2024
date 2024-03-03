package edu.java.service.exceptions;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class NoSuchLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link isn't tracking: %s by chat %d";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public NoSuchLinkException(URI url, Long id) {
        super(HTTP_STATUS_CODE, DESCRIPTION.formatted(url, id));
    }
}
