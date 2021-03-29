package tech.itpark.http.exception;

public class InvalidUriException extends RuntimeException {
    public InvalidUriException() {
        super();
    }

    public InvalidUriException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUriException(Throwable cause) {
        super(cause);
    }

    protected InvalidUriException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidUriException(String s) {
    }
}
