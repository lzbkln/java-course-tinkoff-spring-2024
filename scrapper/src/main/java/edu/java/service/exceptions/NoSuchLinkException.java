package edu.java.service.exceptions;

import java.net.URI;

public class NoSuchLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link isn't tracking: %s";

    public NoSuchLinkException(URI url) {
        super(DESCRIPTION.formatted(url));
    }

}
