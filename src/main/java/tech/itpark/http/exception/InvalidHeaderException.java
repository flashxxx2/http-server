package tech.itpark.http.exception;

public class InvalidHeaderException extends RuntimeException {
    public InvalidHeaderException() {
        super();
    }

    public InvalidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHeaderException(Throwable cause) {
        super(cause);
    }

    protected InvalidHeaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidHeaderException(String s) {
    }
}
