package edu.java.service.exceptions;

import java.net.URI;

public class AlreadyTrackedLinkException extends ServiceException {
    private static final String DESCRIPTION = "Link is already tracked %s";

    public AlreadyTrackedLinkException(URI link) {
        super(DESCRIPTION.formatted(link));
    }

}
