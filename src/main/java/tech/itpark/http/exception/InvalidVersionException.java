package tech.itpark.http.exception;

public class InvalidVersionException extends RuntimeException {
    public InvalidVersionException() {
        super();
    }

    public InvalidVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidVersionException(Throwable cause) {
        super(cause);
    }

    protected InvalidVersionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidVersionException(String s) {
    }
}
