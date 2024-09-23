package edu.java.configuration.retry;

public enum HttpStatusCodes {
    CLIENT_ERROR,
    SERVER_ERROR,
    OTHER;

    @SuppressWarnings("MagicNumber")
    public static HttpStatusCodes getGroupByStatusCode(int statusCode) {
        if (statusCode >= 100 && statusCode < 400) {
            return OTHER;
        } else if (statusCode >= 400 && statusCode < 500) {
            return CLIENT_ERROR;
        } else if (statusCode >= 500 && statusCode < 600) {
            return SERVER_ERROR;
        } else {
            throw new IllegalArgumentException("Invalid status code: " + statusCode);
        }
    }
}


