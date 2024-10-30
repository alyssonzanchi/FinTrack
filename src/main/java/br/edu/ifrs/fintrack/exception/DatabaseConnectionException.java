package br.edu.ifrs.fintrack.exception;

public class DatabaseConnectionException extends FinTrackAppException {
    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}