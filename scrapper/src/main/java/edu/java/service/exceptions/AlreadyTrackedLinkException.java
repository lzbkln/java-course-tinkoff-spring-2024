package edu.java.service.exceptions;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class AlreadyTrackedLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link is already tracked %s by chat %d";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public AlreadyTrackedLinkException(URI link, Long id) {
        super(HTTP_STATUS_CODE, DESCRIPTION.formatted(link, id));
    }
}
