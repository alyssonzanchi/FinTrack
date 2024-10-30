package br.edu.ifrs.fintrack.exception;

public class FinTrackAppException extends RuntimeException {
    public FinTrackAppException(String message) {
        super(message);
    }

    public FinTrackAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
