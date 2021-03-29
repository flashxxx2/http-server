package tech.itpark.http.exception;

public class InvalidRequestLineException extends RuntimeException {
    public InvalidRequestLineException() {
        super();
    }

    public InvalidRequestLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestLineException(Throwable cause) {
        super(cause);
    }

    protected InvalidRequestLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
